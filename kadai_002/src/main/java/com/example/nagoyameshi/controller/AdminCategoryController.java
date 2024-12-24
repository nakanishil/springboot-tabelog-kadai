package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.repository.CategoryRepository;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;

    public AdminCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String index(
        @RequestParam(name = "keyword", required = false) String keyword,
        Model model
    ) {
        List<Category> categoryList;

        // 1. キーワードが入力されていれば部分一致検索、なければ全件取得（ID昇順）
        if (keyword != null && !keyword.isBlank()) {
            categoryList = categoryRepository.findByCategoryNameContainingIgnoreCaseOrderByIdAsc(keyword);
        } else {
            categoryList = categoryRepository.findAllByOrderByIdAsc();
        }

        // 2. 検索結果と検索キーワードをモデルに詰める
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("keyword", keyword);

        return "admin/categories/index";
    }

    
 // 新規作成用エンドポイント
    @PostMapping("/create")
    public String createCategory(@RequestParam("categoryName") String categoryName) {
        // 1. バリデーション（簡易的に必須チェックだけ例示）
        if (categoryName == null || categoryName.isBlank()) {
            // 本来は BindingResultなどを使い、画面にエラーメッセージを戻す処理が望ましい
            return "redirect:/admin/categories?error=blankCategoryName";
        }

        // 2. 同名カテゴリの重複チェックはDBユニーク制約でエラーになる場合など
        //    ここでは簡易的に repository.save(...) だけ行う
        Category newCategory = new Category();
        newCategory.setCategoryName(categoryName);
        categoryRepository.save(newCategory);

        // 3. 一覧にリダイレクト
        return "redirect:/admin/categories?success=created";
    }
    
    @PostMapping("/update")
    public String updateCategory(
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("categoryName") String categoryName
    ) {
        // 1. カテゴリ取得
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("カテゴリが存在しません: " + categoryId));

        // 2. 更新
        if (categoryName == null || categoryName.isBlank()) {
            return "redirect:/admin/categories?error=blankCategoryName";
        }
        category.setCategoryName(categoryName);
        categoryRepository.save(category);

        // 3. リダイレクト
        return "redirect:/admin/categories?success=updated";
    }
    
    @PostMapping("/delete")
    public String deleteCategory(@RequestParam("categoryId") Integer categoryId) {
        // 1. カテゴリが存在するかチェック
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("カテゴリが存在しません: " + categoryId));

        // 2. 削除実行
        categoryRepository.deleteById(categoryId);

        // 3. 一覧にリダイレクト
        return "redirect:/admin/categories?success=deleted";
    }


}