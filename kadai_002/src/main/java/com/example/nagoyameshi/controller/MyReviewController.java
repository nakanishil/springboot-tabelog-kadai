package com.example.nagoyameshi.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewForm;
import com.example.nagoyameshi.service.ReviewService;
import com.example.nagoyameshi.service.UserService;

@Controller
public class MyReviewController {
    private final UserService userService;
    private final ReviewService reviewService;

    public MyReviewController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }
    
    @GetMapping("/myReviews")
    public String showMyReviews(Principal principal,
                                @RequestParam(defaultValue="0") int page,
                                Model model) {
        // ログイン中のユーザ取得
        User user = userService.findByEmail(principal.getName());

        // Pageable作成(1ページあたり10件、更新日降順など)
        Pageable pageable = PageRequest.of(page, 10, Sort.by("updatedAt").descending());

        // ユーザが投稿したレビュー一覧取得（ページネーション対応）
        Page<Review> myReviewsPage = reviewService.getReviewsByUser(user, pageable);

        // モデルに詰める
        model.addAttribute("myReviewsPage", myReviewsPage);

        return "user/myReviews"; // resources/templates/user/myReviews.html
    }
    
 // レビュー編集フォーム表示
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer reviewId, Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());

        Optional<Review> reviewOpt = reviewService.getReviewById(reviewId);
        if (reviewOpt.isEmpty()) {
            model.addAttribute("errorMessage", "レビューが見つかりません。");
            return "error";
        }

        Review review = reviewOpt.get();
        // ログインユーザがレビュー主でなければエラー
        if (!review.getUser().getId().equals(user.getId())) {
            model.addAttribute("errorMessage", "このレビューを編集する権限がありません。");
            return "error";
        }

        // 編集フォームに使用するFormオブジェクトを用意
        ReviewForm form = new ReviewForm();
        form.setPoint(review.getPoint());
        form.setReviewComment(review.getReviewComment());

        model.addAttribute("reviewForm", form);
        model.addAttribute("reviewId", review.getId());
        model.addAttribute("restaurantName", review.getRestaurant().getName());

        return "user/myReviewEdit"; // レビュー編集フォーム画面(別途作成)
    }

    // レビュー編集処理(POST)
    @PostMapping("/edit/{id}")
    public String editReview(
            @PathVariable("id") Integer reviewId,
            @ModelAttribute("reviewForm") ReviewForm reviewForm,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(principal.getName());

        Optional<Review> reviewOpt = reviewService.getReviewById(reviewId);
        if (reviewOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "レビューが見つかりません。");
            return "redirect:/myReviews";
        }

        Review review = reviewOpt.get();
        // ログインユーザがレビュー主でなければエラー
        if (!review.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "このレビューを編集する権限がありません。");
            return "redirect:/myReviews";
        }

        // フォームの内容でレビューを更新
        review.setPoint(reviewForm.getPoint());
        review.setReviewComment(reviewForm.getReviewComment());
        reviewService.updateReview(review);

        redirectAttributes.addFlashAttribute("successMessage", "レビューを更新しました。");
        return "redirect:/myReviews";
    }

    // レビュー削除処理(POST)
    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable("id") Integer reviewId, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(principal.getName());

        Optional<Review> reviewOpt = reviewService.getReviewById(reviewId);
        if (reviewOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "レビューが見つかりません。");
            return "redirect:/myReviews";
        }

        Review review = reviewOpt.get();
        // ログインユーザがレビュー主でなければエラー
        if (!review.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "このレビューを削除する権限がありません。");
            return "redirect:/myReviews";
        }

        // 削除実行
        reviewService.deleteReview(reviewId);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
        return "redirect:/myReviews";
    }

    
}