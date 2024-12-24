package com.example.nagoyameshi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    public ReviewService(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
    }

    // レストランIDに基づいてレビューを取得
    public List<Review> getReviewsByRestaurantId(Integer restaurantId) {
        return reviewRepository.findByRestaurant_IdOrderByUpdatedAtDesc(restaurantId);
    }

    // レビューIDでレビューを取得
    public Optional<Review> getReviewById(Integer reviewId) {
        return reviewRepository.findById(reviewId);
    }

    // 新しいレビューを保存
    @Transactional
    public void saveReview(Integer restaurantId, User user, Review review) {
        // レストラン情報を取得
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("指定されたレストランが見つかりません: ID=" + restaurantId));
        
        // 既存レビューがあるかを確認
        Optional<Review> existingReview = reviewRepository.findByUserAndRestaurant(user, restaurant);
        if (existingReview.isPresent()) {
            // 既存レビューがある場合は、そのレビューを更新する
            Review existing = existingReview.get();
            existing.setPoint(review.getPoint());
            existing.setReviewComment(review.getReviewComment());
            reviewRepository.save(existing);
            return; // 処理終了
        }

        // 新規レビューの場合は、レストランとユーザー情報をレビューに設定して保存
        review.setRestaurant(restaurant);
        review.setUser(user);
        reviewRepository.save(review);
    }

    // レビューを削除
    @Transactional
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // レビューを編集
    @Transactional
    public void updateReview(Review review) {
        reviewRepository.save(review);
    }
    
    // 新着レビューを取得
    public List<Review> getLatestReviews(Integer restaurantId) {
        return reviewRepository.findLatestReviewsByRestaurantId(restaurantId);
    }
    
    public List<Review> findByRestaurantId(Integer restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId);
    }
    
    @Transactional(readOnly = true)
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }
    
    public Page<Review> findReviewsByRestaurantIdWithPaging(Integer restaurantId, Pageable pageable) {
        return reviewRepository.findByRestaurantId(restaurantId, pageable);
    }
    
    // ユーザーのレビュー（ページング対応）
    @Transactional(readOnly = true)
    public Page<Review> getReviewsByUser(User user, Pageable pageable) {
        return reviewRepository.findByUser(user, pageable);
    }
    
 // 追加するメソッド例
    public Page<Review> findPublicReviewsByRestaurantIdWithPaging(Integer restaurantId, Pageable pageable) {
        // 「enabled=true & restaurantId一致 & 更新日時降順」ページング
        return reviewRepository.findByRestaurantIdAndEnabledTrueOrderByUpdatedAtDesc(restaurantId, pageable);
    }

    // 追加するメソッド例
    public List<Review> getLatestPublicReviews(Integer restaurantId) {
        // 「enabled=true & restaurantId一致」新着順
        return reviewRepository.findLatestReviewsByRestaurantIdAndEnabledTrue(restaurantId);
    }
    
    public List<Review> getLatestPublicReviewsTop4(Integer restaurantId) {
        return reviewRepository.findTop4ByRestaurantIdAndEnabledTrueOrderByCreatedAtDesc(restaurantId);
    }



}
