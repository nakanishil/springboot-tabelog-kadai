package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    // ユーザと店舗の組み合わせによる検索
    // → すでにお気に入り登録されているかどうかチェックするため
    Favorite findByUserAndRestaurant(User user, Restaurant restaurant);
    
    // 指定のユーザのお気に入りを createdAt 降順でページング取得するメソッド
    Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    // ユーザに紐づくお気に入りを作成日降順で返す（ページングなし）
    List<Favorite> findByUserOrderByCreatedAtDesc(User user);

    
}
