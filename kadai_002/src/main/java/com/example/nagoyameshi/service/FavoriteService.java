package com.example.nagoyameshi.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final RestaurantRepository restaurantRepository;

    /**
     * コンストラクタインジェクション
     */
    public FavoriteService(FavoriteRepository favoriteRepository,
                           RestaurantRepository restaurantRepository) {
        this.favoriteRepository = favoriteRepository;
        this.restaurantRepository = restaurantRepository;
    }
    
    /**
     * ページング対応：ユーザに紐づくお気に入り一覧を取得(作成日降順)
     */
    @Transactional(readOnly = true)
    public Page<Favorite> getFavoritesDesc(User user, Pageable pageable) {
        return favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    /**
     * 【1】ユーザに紐づくお気に入り一覧を「作成日降順」で取得
     *      （ページングは使わず、Listで全件を返す）
     * 
     *  リポジトリ側には以下のようなメソッドを定義しておく想定です:
     *  
     *    List<Favorite> findByUserOrderByCreatedAtDesc(User user);
     */
    @Transactional(readOnly = true)
    public List<Favorite> getFavoritesDesc(User user) {
        return favoriteRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * 【2】お気に入りを追加
     * 
     *  - Restaurant をIDから取得し
     *  - 既にお気に入り登録されていないかチェック
     *  - 無ければ新規登録
     */
    @Transactional
    public void addFavorite(User user, Integer restaurantId, String favoriteComment) {
        // 1. Restaurant を取得
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            throw new IllegalArgumentException("レストランが見つかりません (ID=" + restaurantId + ")");
        }

        // 2. 既にお気に入り登録されていないか
        Favorite existing = favoriteRepository.findByUserAndRestaurant(user, restaurant);
        if (existing == null) {
            // 3. 新規作成
            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            newFavorite.setRestaurant(restaurant);
            newFavorite.setFavoriteComment(favoriteComment);
            favoriteRepository.save(newFavorite);
        }
    }

    /**
     * 【3】お気に入り削除
     * 
     *  - favoriteId で検索
     *  - 所有者が自分なら削除
     */
    @Transactional
    public void deleteFavorite(User user, Integer favoriteId) {
        // 1. favoriteId から検索
        Favorite favorite = favoriteRepository.findById(favoriteId).orElse(null);

        // 2. 存在＆所有者チェック
        if (favorite != null && favorite.getUser().getId().equals(user.getId())) {
            favoriteRepository.delete(favorite);
        } else {
            throw new IllegalArgumentException("お気に入りが存在しない、または所有権がありません (ID=" + favoriteId + ")");
        }
    }

    /**
     * 【4】favoriteId でお気に入りを1件取得
     *      （編集・コメント更新用）
     */
    @Transactional(readOnly = true)
    public Favorite findFavoriteById(User user, Integer favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElse(null);
        if (favorite == null || !favorite.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("お気に入りが存在しないか他人のものです (ID=" + favoriteId + ")");
        }
        return favorite;
    }

    /**
     * 【5】お気に入りコメント更新
     * 
     *  - findFavoriteById(...) を再利用し
     *  - コメントを上書きして保存
     */
    @Transactional
    public void updateFavoriteComment(User user, Integer favoriteId, String newComment) {
        Favorite favorite = this.findFavoriteById(user, favoriteId);
        // 更新
        favorite.setFavoriteComment(newComment);
        favoriteRepository.save(favorite);
    }

    /**
     * 【6】(RestaurantController用) ユーザと店舗からお気に入りを1件検索
     * 
     *  - 「登録済みかどうか」をチェックするときに使う
     *  - 所有権チェックはしない。返ってきたらユーザ自身の所有物かどうか
     *    コントローラや呼び出し元で判定するか、ここで判定してもOK
     */
    @Transactional(readOnly = true)
    public Favorite findByUserAndRestaurant(User user, Restaurant restaurant) {
        return favoriteRepository.findByUserAndRestaurant(user, restaurant);
    }
}
