package com.example.nagoyameshi.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.PasswordResetToken;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ForgotPasswordForm;
import com.example.nagoyameshi.form.ResetPasswordForm;
import com.example.nagoyameshi.service.MailService;
import com.example.nagoyameshi.service.PasswordResetTokenService;
import com.example.nagoyameshi.service.UserService;

@Controller
@RequestMapping("/password-reset")
public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetTokenService tokenService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(UserService userService,
                                   PasswordResetTokenService tokenService,
                                   MailService mailService,
                                   PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * パスワードを忘れた方の入力画面(メールアドレスのみ入力するフォーム)
     */
    @GetMapping("/forgot")
    public String forgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordForm", new ForgotPasswordForm());
        return "password-reset/forgot"; 
        // ↑ 後ほど作成するThymeleafテンプレート (templates/password-reset/forgot.html 等) を想定
    }

    /**
     * 「リセットメール送信」処理
     */
    @PostMapping("/forgot")
    public String sendResetMail(
            @Valid ForgotPasswordForm forgotPasswordForm,
            BindingResult bindingResult,
            Model model
    ) {
        // 入力チェック
        if (bindingResult.hasErrors()) {
            return "password-reset/forgot";
        }

        // ユーザ存在確認
        User user = userService.findByEmail(forgotPasswordForm.getMailAddress());
        if (user == null) {
            bindingResult.rejectValue("mailAddress", "notfound", "メールアドレスが見つかりません");
            return "password-reset/forgot";
        }

        // トークン生成＆DB登録 などはそのまま
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
        tokenService.create(user, token, expiresAt);

        // メール送信
        mailService.sendPasswordResetMail(user, token);

        // ★ 画面表示用フラグや成功メッセージをセットする
        model.addAttribute("successMessage", "パスワードリセット用のメールを送信しました。メール本文のURLをクリックしてください。");

        // ★ フォーム入力欄を非表示にするためのフラグ
        model.addAttribute("mailSent", true);

        // ★ リダイレクトではなく、同じテンプレートを返す
        return "password-reset/forgot";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(
            @RequestParam("token") String token,
            Model model
    ) {
        // 1. トークンがDBに存在するかチェック
        PasswordResetToken prt = tokenService.findByToken(token);
        if (prt == null) {
            // 該当トークンが無ければエラー扱い
            model.addAttribute("errorMessage", "無効なトークンです。再度お手続きをお願いします。");
            return "password-reset/error"; 
            // ※ このテンプレートは後で作成する想定
        }

        // 2. トークンが期限切れ or 既に使用済みでないかをチェック
        // 有効期限の設定をしていないなら条件文を省略してもOK
        if (prt.getExpiresAt() != null && LocalDateTime.now().isAfter(prt.getExpiresAt())) {
            model.addAttribute("errorMessage", "トークンの有効期限が切れています。再度お手続きをお願いします。");
            return "password-reset/error";
        }
        if (Boolean.TRUE.equals(prt.getUsed())) {
            model.addAttribute("errorMessage", "このトークンは既に使用されています。再度お手続きをお願いします。");
            return "password-reset/error";
        }

        // 3. 問題なければ、新パスワードを入力するフォームを表示させる
        ResetPasswordForm form = new ResetPasswordForm();
        form.setToken(token);  // hiddenで持たせる用
        model.addAttribute("resetPasswordForm", form);

        return "password-reset/reset"; 
    }
    
    @PostMapping("/reset-password")
    public String updatePassword(
        @Valid ResetPasswordForm resetPasswordForm,
        BindingResult bindingResult,
        Model model
    ) {
        // 1) 入力チェック（newPassword と newPasswordConfirmation が空でない等）
        if (bindingResult.hasErrors()) {
            // バリデーションNGの場合 → 再度入力画面に戻す
            return "password-reset/reset";
        }

        // 2) トークンで PasswordResetToken を検索
        PasswordResetToken prt = tokenService.findByToken(resetPasswordForm.getToken());
        if (prt == null) {
            model.addAttribute("errorMessage", "無効なトークンです。再度お手続きをお願いします。");
            return "password-reset/error";
        }

        // トークン期限・usedフラグチェック
        if (prt.getExpiresAt() != null && LocalDateTime.now().isAfter(prt.getExpiresAt())) {
            model.addAttribute("errorMessage", "トークンの有効期限が切れています。再度お手続きをお願いします。");
            return "password-reset/error";
        }
        if (Boolean.TRUE.equals(prt.getUsed())) {
            model.addAttribute("errorMessage", "このトークンは既に使用されています。再度お手続きをお願いします。");
            return "password-reset/error";
        }

        // 3) newPassword と newPasswordConfirmation の一致チェック（任意）
        if (!resetPasswordForm.getNewPassword().equals(resetPasswordForm.getNewPasswordConfirmation())) {
            // 不一致ならエラー表示 → パスワード再設定画面に戻す
            bindingResult.rejectValue("newPasswordConfirmation", "mismatch", "パスワードが一致しません");
            return "password-reset/reset";
        }

        // 4) Userエンティティのパスワードを更新
        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordForm.getNewPassword()));
        userService.save(user);

        // 5) Tokenを「使用済みに更新」して再度保存
        prt.setUsed(true);
        tokenService.markUsed(prt); // ← tokenServiceで prt.setUsed(true) & save()

        // 6) 完了後の画面遷移
        //   - 「ログイン画面」か「ログイン済み画面」にリダイレクトさせる場合が多い
        return "redirect:/login?resetSuccess";
    }
    
    
}
