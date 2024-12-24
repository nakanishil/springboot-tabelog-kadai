package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.FavoriteForm;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;
import com.example.nagoyameshi.service.ReviewService;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final ReviewService reviewService;
    private final FavoriteService favoriteService;
    private final CategoryRepository categoryRepository;

    public RestaurantController(
            RestaurantRepository restaurantRepository,
            ReviewService reviewService,
            FavoriteService favoriteService,
            CategoryRepository categoryRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.reviewService = reviewService;
        this.favoriteService = favoriteService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "area", required = false) String area,
                        @RequestParam(name = "priceRange", required = false) Integer priceRange,
                        @RequestParam(name = "order", required = false) String order,
                        @RequestParam(name = "categoryId", required = false) Integer categoryId, // ← カテゴリIDを受け取る
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model)
    {
        Page<Restaurant> restaurantPage;

        // ======================================
        // (A) カテゴリ検索が指定されている場合
        // ======================================
        if (categoryId != null && categoryId != 0) {
            // 価格の安い順 or 高い順 or 新着順
            if ("priceRangeAsc".equals(order)) {
                restaurantPage = restaurantRepository.findByCategoryIdOrderByPriceRangeAsc(categoryId, pageable);
            } else if ("priceRangeDesc".equals(order)) {
                restaurantPage = restaurantRepository.findByCategoryIdOrderByPriceRangeDesc(categoryId, pageable);
            } else {
                // デフォルト: 新着順
                restaurantPage = restaurantRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);
            }

        // ======================================
        // (B) キーワード検索
        // ======================================
        } else if (keyword != null && !keyword.isEmpty()) {
            if ("priceRangeAsc".equals(order)) {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByPriceRangeAsc(
                    "%" + keyword + "%",
                    "%" + keyword + "%",
                    pageable
                );
            } else if ("priceRangeDesc".equals(order)) {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByPriceRangeDesc(
                    "%" + keyword + "%",
                    "%" + keyword + "%",
                    pageable
                );
            } else {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc(
                    "%" + keyword + "%",
                    "%" + keyword + "%",
                    pageable
                );
            }

        // ======================================
        // (C) 地域検索
        // ======================================
        } else if (area != null && !area.isEmpty()) {
            if ("priceRangeAsc".equals(order)) {
                restaurantPage = restaurantRepository.findByAddressLikeOrderByPriceRangeAsc(
                    "%" + area + "%",
                    pageable
                );
            } else if ("priceRangeDesc".equals(order)) {
                restaurantPage = restaurantRepository.findByAddressLikeOrderByPriceRangeDesc(
                    "%" + area + "%",
                    pageable
                );
            } else {
                restaurantPage = restaurantRepository.findByAddressLikeOrderByCreatedAtDesc(
                    "%" + area + "%",
                    pageable
                );
            }

        // ======================================
        // (D) 価格範囲検索
        // ======================================
        } else if (priceRange != null) {
            if ("priceRangeAsc".equals(order)) {
                restaurantPage = restaurantRepository.findByPriceRangeLessThanEqualOrderByPriceRangeAsc(
                    priceRange,
                    pageable
                );
            } else if ("priceRangeDesc".equals(order)) {
                restaurantPage = restaurantRepository.findByPriceRangeLessThanEqualOrderByPriceRangeDesc(
                    priceRange,
                    pageable
                );
            } else {
                restaurantPage = restaurantRepository.findByPriceRangeLessThanEqualOrderByCreatedAtDesc(
                    priceRange,
                    pageable
                );
            }

        // ======================================
        // (E) 条件なし（全件表示）
        // ======================================
        } else {
            if ("priceRangeAsc".equals(order)) {
                restaurantPage = restaurantRepository.findAllByOrderByPriceRangeAsc(pageable);
            } else if ("priceRangeDesc".equals(order)) {
                restaurantPage = restaurantRepository.findAllByOrderByPriceRangeDesc(pageable);
            } else {
                restaurantPage = restaurantRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        }

        // カテゴリ一覧を取得して Model に詰める（既存実装）
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);

        // 画面に渡す
        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("area", area);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("order", order);

        // 選択中のカテゴリIDを戻す
        model.addAttribute("categoryId", categoryId);

        return "restaurants/index";
    }


    /**
     * 店舗詳細
     */
    @GetMapping("/{id}")
    public String show(
            @PathVariable("id") Integer id,
            Model model,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 1. 店舗を取得
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        if (restaurant == null) {
            return "redirect:/restaurants";
        }

        // 2. 新着レビュー4件取得 (enabled=true のみ)
        List<Review> latestReviews = reviewService.getLatestPublicReviewsTop4(id);

        // 平均評価を計算
        double averageRating = 0.0;
        if (!latestReviews.isEmpty()) {
            averageRating = latestReviews.stream()
                .mapToInt(r -> r.getPoint())
                .average()
                .orElse(0.0);
        }
        int flooredRating = (int) Math.floor(averageRating);

        // 3. お気に入り登録チェック
        boolean favoriteRegistered = false;
        Integer favoriteId = null;
        if (userDetails != null) {
            User currentUser = userDetails.getUser();
            Favorite existingFavorite = favoriteService.findByUserAndRestaurant(currentUser, restaurant);
            if (existingFavorite != null) {
                favoriteRegistered = true;
                favoriteId = existingFavorite.getId();
            }
        }

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("latestReviews", latestReviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("flooredRating", flooredRating);
        model.addAttribute("favoriteRegistered", favoriteRegistered);
        model.addAttribute("favoriteId", favoriteId);

        // 予約フォーム用に空のオブジェクトを Model に渡す (必須)
        model.addAttribute("reservationInputForm", new ReservationInputForm());

        // お気に入りフォーム
        model.addAttribute("favoriteForm", new FavoriteForm());

        return "restaurants/show";
    }
}
