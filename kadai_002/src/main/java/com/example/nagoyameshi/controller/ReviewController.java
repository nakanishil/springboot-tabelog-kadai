package com.example.nagoyameshi.controller;

import java.security.Principal;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewForm;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.ReviewService;
import com.example.nagoyameshi.service.UserService;

@Controller
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final RestaurantService restaurantService;

    public ReviewController(ReviewService reviewService, UserService userService, RestaurantService restaurantService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants/{id}/reviews/form")
    public String showReviewForm(@PathVariable("id") Integer restaurantId, Model model, Principal principal) {
        // ユーザー情報の取得
        User user = null;
        if (principal != null) {
            user = userService.findByEmail(principal.getName());
        }

        // 未ログインユーザーへの制限
        if (user == null) {
            return "redirect:/login";
        }

        // レストラン情報を取得
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isEmpty()) {
            model.addAttribute("errorMessage", "指定されたレストランが見つかりません。");
            return "error";
        }

        // モデルにデータを追加
        model.addAttribute("restaurant", restaurant.get());
        model.addAttribute("reviewForm", new ReviewForm());
        model.addAttribute("restaurantId", restaurantId);

        return "restaurants/reviewForm";
    }

    // レビュー投稿処理
    @PostMapping("/restaurants/{id}/reviews/add")
    public String submitReview(@PathVariable("id") Integer restaurantId, 
                               @ModelAttribute("reviewForm") @Valid ReviewForm reviewForm,
                               BindingResult bindingResult,
                               Principal principal,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurantId", restaurantId);
            return "restaurants/reviewForm";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }

        Review review = new Review();
        review.setPoint(reviewForm.getPoint());
        review.setReviewComment(reviewForm.getReviewComment());
        reviewService.saveReview(restaurantId, user, review);

        return "redirect:/restaurants/" + restaurantId + "/reviews";
    }
    
    @GetMapping("/restaurants/{id}/reviews")
    public String showReviews(
        @PathVariable("id") Integer restaurantId,
        @RequestParam(name="page", defaultValue="0") int page,
        Model model
    ) {
        // 1. レストラン情報取得
        Restaurant restaurant = restaurantService.findById(restaurantId)
            .orElseThrow(() -> new IllegalArgumentException("指定されたレストランが見つかりません: " + restaurantId));

        // 2. ページング設定（10件ずつ, 更新日時降順）
        Pageable pageable = PageRequest.of(page, 10, Sort.by("updatedAt").descending());

        // 3. レビュー取得（★差し替え！）
        // Page<Review> reviewsPage = reviewService.findReviewsByRestaurantIdWithPaging(restaurantId, pageable);
        Page<Review> reviewsPage = reviewService.findPublicReviewsByRestaurantIdWithPaging(restaurantId, pageable);

        // 4. モデルにセット
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reviewsPage", reviewsPage);

        return "restaurants/reviews";
    }
}
