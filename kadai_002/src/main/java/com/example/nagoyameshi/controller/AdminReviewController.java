package com.example.nagoyameshi.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional; // まとめて更新なのでトランザクション推奨
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.repository.ReviewRepository;

@Controller
public class AdminReviewController {

    private final ReviewRepository reviewRepository;

    public AdminReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // --- 前ステップのGETメソッド ---
    @GetMapping("/admin/reviews")
    public String listReviews(
            @RequestParam(name="page", defaultValue="0") int page,
            @RequestParam(name="keyword", required=false) String keyword,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, 20);

        // 検索キーワードで振り分け
        Page<Review> reviewsPage;
        if (keyword != null && !keyword.isEmpty()) {
            reviewsPage = reviewRepository.findByUser_NameContainingIgnoreCaseOrderByIdDesc(keyword, pageable);
        } else {
            reviewsPage = reviewRepository.findAllByOrderByIdDesc(pageable);
        }

        model.addAttribute("reviewsPage", reviewsPage);
        model.addAttribute("keyword", keyword);
        return "admin/reviews/list";
    }

    // --- まとめて更新用のPOSTメソッド ---
    @Transactional
    @PostMapping("/admin/reviews/bulkUpdate")
    public String bulkUpdate(
            @RequestParam Map<String, String> enabledMap,
            @RequestParam(name="keyword", required=false) String keyword
    ) {
        // enabledMap のキー例： "enabledMap[3]", "enabledMap[7]" ...
        // 値： "true" or "false"

        // 1. enabledMap から、レビューIDと公開フラグを取り出す
        //    「enabledMap[3]」のようなキーから数値部分を抜き出すイメージ
        for (Map.Entry<String, String> entry : enabledMap.entrySet()) {
            String formFieldName = entry.getKey(); // 例： "enabledMap[10]"
            String valueStr = entry.getValue();     // "true" or "false"

            // formFieldNameから reviewIdを抜き出す
            //   "enabledMap[" と "]" を取り除く → 数値の部分だけをIntegerに変換
            if (formFieldName.startsWith("enabledMap[")) {
                String idPart = formFieldName.substring("enabledMap[".length(),
                                                        formFieldName.length() - 1);
                Integer reviewId = Integer.valueOf(idPart);

                // 2. DBからレビューを取得
                Review review = reviewRepository.findById(reviewId)
                        .orElse(null);
                if (review != null) {
                    // 3. enabledを更新
                    boolean newEnabled = Boolean.parseBoolean(valueStr);
                    review.setEnabled(newEnabled);
                    // 4. save (Transaction中なので .save()省略可だが念のため)
                    reviewRepository.save(review);
                }
            }
        }

        // 更新が終わったら、再度一覧画面にリダイレクト
        // 検索キーワードが指定されていた場合はパラメータ付きで戻す
        if (keyword != null && !keyword.isEmpty()) {
            return "redirect:/admin/reviews?keyword=" + keyword;
        } else {
            return "redirect:/admin/reviews";
        }
    }
}
