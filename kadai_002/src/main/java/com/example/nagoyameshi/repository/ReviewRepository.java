package com.example.nagoyameshi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    // レストランIDでレビューを取得（更新日時順）
    List<Review> findByRestaurant_IdOrderByUpdatedAtDesc(Integer restaurantId);
    
    // 新着レビューを取得 (指定した店舗IDのレビューを投稿日で降順に並べて最大4件)
    @Query("SELECT r FROM Review r WHERE r.restaurant.id = :restaurantId ORDER BY r.createdAt DESC")
    List<Review> findLatestReviewsByRestaurantId(@Param("restaurantId") Integer restaurantId);
    
    List<Review> findByRestaurantId(Integer restaurantId);
    Optional<Review> findByUserAndRestaurant(User user, Restaurant restaurant);
    List<Review> findByUser(User user);
    Page<Review> findByRestaurantId(Integer restaurantId, Pageable pageable);
    Page<Review> findByUser(User user, Pageable pageable);

}