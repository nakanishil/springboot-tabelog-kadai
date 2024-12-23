package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.FavoriteForm;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;

@Controller
@RequestMapping("/myFavorites")
@PreAuthorize("hasRole('ROLE_PAYING_MEMBER')")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * お気に入り一覧表示 (ページングなし)
     */
    @GetMapping
    public String listFavorites(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Model model,
            // ↓ @PageableDefault 等でページサイズ、ソートを指定
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        User user = userDetails.getUser();

        // サービスのページング対応メソッドを呼び出す
        Page<Favorite> favoritesPage = favoriteService.getFavoritesDesc(user, pageable);

        // テンプレートで使えるようにmodelに詰める
        model.addAttribute("favoritesPage", favoritesPage);
        model.addAttribute("user", user);

        // ページング以外の入力フォームがあれば追加
        model.addAttribute("favoriteForm", new FavoriteForm());

        return "user/myFavorites";
    }

    /**
     * お気に入り追加
     */
    @PostMapping
    public String addFavorite(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            FavoriteForm form
    ) {
        User currentUser = userDetails.getUser();

        // サービスで追加
        favoriteService.addFavorite(currentUser, form.getRestaurantId(), form.getFavoriteComment());

        return "redirect:/restaurants/" + form.getRestaurantId();
    }

    /**
     * お気に入り削除
     */
    @PostMapping("/{favoriteId}/delete")
    public String deleteFavorite(
            @PathVariable("favoriteId") Integer favoriteId,
            @RequestParam(name="returnTo", required=false) String returnTo, // 追加
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User currentUser = userDetails.getUser();

        // 1. favorite を取得
        Favorite favorite = favoriteService.findFavoriteById(currentUser, favoriteId);
        Integer restaurantId = favorite.getRestaurant().getId();

        // 2. 削除実行
        favoriteService.deleteFavorite(currentUser, favoriteId);

        // 3. リダイレクト先を分岐
        if ("list".equals(returnTo)) {
            // お気に入り一覧に戻る
            return "redirect:/myFavorites";
        } else if ("detail".equals(returnTo)) {
            // 店舗詳細に戻る
            return "redirect:/restaurants/" + restaurantId;
        } else {
            // 万一パラメータが無ければ、デフォルトで一覧へ
            return "redirect:/myFavorites";
        }
    }



    /**
     * お気に入りコメント編集画面
     */
    @GetMapping("/{favoriteId}/edit")
    public String editFavorite(
            @PathVariable("favoriteId") Integer favoriteId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Model model
    ) {
        User currentUser = userDetails.getUser();

        // サービス層でお気に入り1件取得
        Favorite favorite;
        try {
            favorite = favoriteService.findFavoriteById(currentUser, favoriteId);
        } catch (IllegalArgumentException e) {
            // 所有権が無いか存在しない → 一覧へ
            return "redirect:/myFavorites";
        }

        // フォームに詰め替え
        FavoriteForm form = new FavoriteForm();
        form.setRestaurantId(favorite.getRestaurant().getId());
        form.setFavoriteComment(favorite.getFavoriteComment());

        model.addAttribute("favoriteId", favoriteId);
        model.addAttribute("favoriteForm", form);

        return "user/myFavoriteEdit";
    }

    /**
     * お気に入りコメント更新
     */
    @PostMapping("/{favoriteId}/update")
    public String updateFavorite(
            @PathVariable("favoriteId") Integer favoriteId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            FavoriteForm form
    ) {
        User currentUser = userDetails.getUser();

        // サービスでコメントを更新
        favoriteService.updateFavoriteComment(currentUser, favoriteId, form.getFavoriteComment());

        // 更新後は一覧へ
        return "redirect:/myFavorites";
    }
}
