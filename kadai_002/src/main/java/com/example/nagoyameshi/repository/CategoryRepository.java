package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
	
	// ▼ IDの昇順ですべて取得するメソッドを追加
    List<Category> findAllByOrderByIdAsc();
    
    // ▼ カテゴリ名の部分一致(大文字小文字区別なし)
    List<Category> findByCategoryNameContainingIgnoreCaseOrderByIdAsc(String keyword);

}
