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

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.FavoriteForm;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;
import com.example.nagoyameshi.service.ReviewService;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final ReviewService reviewService;
    private final FavoriteService favoriteService;  // 追加

    public RestaurantController(
            RestaurantRepository restaurantRepository,
            ReviewService reviewService,
            FavoriteService favoriteService
    ) {
        this.restaurantRepository = restaurantRepository;
        this.reviewService = reviewService;
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword, // キーワード検索
                        @RequestParam(name = "area", required = false) String area, // 地域フィルタ
                        @RequestParam(name = "priceRange", required = false) Integer priceRange, //価格フィルタ
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, // ページングとソート設定
                        Model model)
    {
        Page<Restaurant> restaurantPage;
        
        if (keyword != null && !keyword.isEmpty()) {
            if (order != null && order.equals("priceRangeAsc")) {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByPriceRangeAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
            } else {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
            }   
        } else if (area != null && !area.isEmpty()) {
            if (order != null && order.equals("priceRangeAsc")) {
                restaurantPage = restaurantRepository.findByAddressLikeOrderByPriceRangeAsc("%" + area + "%", pageable);
            } else {
                restaurantPage = restaurantRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
            }  
        } else if (priceRange != null) {
            if (order != null && order.equals("priceRangeAsc")) {
                restaurantPage = restaurantRepository.findByPriceRangeLessThanEqualOrderByPriceRangeAsc(priceRange, pageable);
            } else {
                restaurantPage = restaurantRepository.findByPriceRangeLessThanEqualOrderByCreatedAtDesc(priceRange, pageable);
            }     
        } else {
            if (order != null && order.equals("priceRangeAsc")) {
                restaurantPage = restaurantRepository.findAllByOrderByPriceRangeAsc(pageable);
            } else {
                restaurantPage = restaurantRepository.findAllByOrderByCreatedAtDesc(pageable);   
            }     
        }
        
        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("area", area);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("order", order);
        
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
        // 1. 店舗取得
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        if (restaurant == null) {
            return "redirect:/restaurants";
        }

        // 2. 新着レビュー
        List<Review> latestReviews = reviewService.getLatestReviews(id);
        // 平均評価を計算
        double averageRating = 0.0;
        if (!latestReviews.isEmpty()) {
            averageRating = latestReviews.stream()
                .mapToInt(r -> r.getPoint())
                .average()
                .orElse(0.0);
        }
        int flooredRating = (int)Math.floor(averageRating);

        // 3. お気に入り登録チェック (サービス層利用)
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

        // 4. Modelに詰める
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("favoriteForm", new FavoriteForm());
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("flooredRating", flooredRating);
        model.addAttribute("latestReviews", latestReviews);
        model.addAttribute("reservationInputForm", new ReservationInputForm());
        model.addAttribute("favoriteRegistered", favoriteRegistered);
        model.addAttribute("favoriteId", favoriteId);

        return "restaurants/show";
    }
}
