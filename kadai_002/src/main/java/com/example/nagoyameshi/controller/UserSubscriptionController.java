package com.example.nagoyameshi.controller;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Controller
public class UserSubscriptionController {
    
    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    
 // Stripeシークレットキー
    @Value("${stripe.api-key}")
    private String stripeApiKey;
    
    // priceIdは定数化
    private static final String PRICE_ID = "price_1QWY0uKfB0cyZLWctCT4RNmO";
    
    public UserSubscriptionController(UserRepository userRepository, UserService userService,
    		RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    /**
     * 有料会員登録を開始する
     * 無料会員がこのURLにアクセスした場合、StripeのCheckoutを開始するためのSessionを作成
     * Session IDを返して、ビューでJSを使ってstripeのCheckoutページへリダイレクトする
     */
    @GetMapping("/user/subscribe")
    public String startSubscription(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) throws Exception {
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
        
        // すでに有料会員ならこの処理は不要
        if (userService.isPayingMember(user)) {
            // すでに有料会員の場合はメッセージを出して終了
            model.addAttribute("errorMessage", "すでに有料会員です。");
            return "user/index";
        }
        
        // Stripe APIキー設定
        Stripe.apiKey = stripeApiKey;
        
        SessionCreateParams params = SessionCreateParams.builder()
        		  .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
        		  .setSuccessUrl(System.getenv("STRIPE_SUCCESS_URL") + "?session_id={CHECKOUT_SESSION_ID}") // 環境変数を取得
        		  .setCancelUrl(System.getenv("STRIPE_CANCEL_URL")) // 環境変数を取得
        		  .addLineItem(
        		      SessionCreateParams.LineItem.builder()
        		        .setPrice(PRICE_ID)
        		        .setQuantity(1L)
        		        .build()
        		  )
        		  .setCustomerEmail(user.getMailAddress())
        		  .build();

//        // サブスク用のセッションパラメータを設定
//        // success_url, cancel_urlは実際のドメインや環境に合わせて修正予定
//        // 仮でhttp://localhost:8080/userを成功・キャンセルともに指定
//        SessionCreateParams params = SessionCreateParams.builder()
//          .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
//          .setSuccessUrl("http://localhost:8080/user?session_id={CHECKOUT_SESSION_ID}")
//          .setCancelUrl("http://localhost:8080/user?canceled=true")
//          .addLineItem(
//              SessionCreateParams.LineItem.builder()
//                .setPrice(PRICE_ID)
//                .setQuantity(1L)
//                .build()
//          )
//          .setCustomerEmail(user.getMailAddress()) // Stripe上でカスタマーを紐づけるのにメールアドレスを利用
//          .build();

        // セッション作成
        Session session = Session.create(params);

        // 作成されたセッションIDをモデルに詰めて、ビュー側でJSでCheckoutページへリダイレクト
        // この時点ではまだDB更新はしない(決済成功後のWebhookでrole変更やsubscription_idの保存)
        model.addAttribute("sessionId", session.getId());
        
        // user/subscribe.html などに遷移してJSでstripe.redirectToCheckoutを行うイメージ
        return "user/subscribe";
    }
    
    /**
     * サブスクリプション(有料会員)解約処理
     * 有料会員がこのURLにPOSTすると、Stripe側でサブスクをキャンセルし、ロールを無料会員に戻す
     */
    @PostMapping("/user/cancel")
    @Transactional
    public String cancelSubscription(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes, Model model) {
    	User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());

        if (!userService.isPayingMember(user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "現在、有料会員ではありません。");
            return "redirect:/user/index";
        }

        String subscriptionId = user.getSubscriptionId();
        if (subscriptionId == null || subscriptionId.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "サブスクリプション情報が見つかりません。サポートへご連絡ください。");
            return "redirect:/user/index";
        }

        Stripe.apiKey = stripeApiKey;

        try {
            Subscription subscription = Subscription.retrieve(subscriptionId);
            Subscription canceledSubscription = subscription.cancel();

            if ("canceled".equals(canceledSubscription.getStatus())) {
                Role freeRole = roleRepository.findByName("ROLE_FREE_MEMBER");
                user.setRole(freeRole);
                user.setSubscriptionId(null);
                userRepository.save(user);

                // 新しい UserDetailsImpl を生成し、SecurityContext にセット
                UserDetailsImpl newUserDetails = new UserDetailsImpl(user, userDetailsImpl.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(newUserDetails, null, newUserDetails.getAuthorities())
                );

                redirectAttributes.addFlashAttribute("successMessage", "有料会員の解約が完了しました。");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "解約処理中に問題が発生しました。");
            }
        } catch (StripeException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Stripe APIの呼び出しでエラーが発生しました。");
        }

        return "redirect:/user";
    }
    
    @GetMapping("/success")
    public String handleSubscriptionSuccess(@RequestParam("session_id") String sessionId, Model model) {
        try {
            Stripe.apiKey = stripeApiKey;
            Session session = Session.retrieve(sessionId);

            String customerEmail = session.getCustomerEmail();
            User user = userRepository.findByMailAddress(customerEmail);

            if (user != null) {
                user.setSubscriptionId(session.getSubscription());
                Role payingRole = roleRepository.findByName("ROLE_PAYING_MEMBER");
                if (payingRole != null) {
                    user.setRole(payingRole);
                    userRepository.save(user);
                    model.addAttribute("message", "有料会員登録が完了しました！");
                } else {
                    model.addAttribute("message", "有料会員のロールが見つかりませんでした。");
                }

                // ★ ここで user をモデルに詰める
                model.addAttribute("user", user);
            } else {
                model.addAttribute("message", "ユーザーが見つかりませんでした。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "決済情報の処理中にエラーが発生しました。");
        }

        // そのまま "user/index" を返す場合、テンプレートで user を参照できるよう上でモデルに追加しておく
        return "user/index";
    }


}