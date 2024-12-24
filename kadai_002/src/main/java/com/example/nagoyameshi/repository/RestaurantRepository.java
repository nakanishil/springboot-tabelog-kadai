package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Restaurant;


public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>{
	public Page<Restaurant> findByNameLike(String keyword, Pageable pageable);
	
    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);  
    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByPriceRangeAsc(String nameKeyword, String addressKeyword, Pageable pageable);  
    public Page<Restaurant> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);
    public Page<Restaurant> findByAddressLikeOrderByPriceRangeAsc(String area, Pageable pageable);
    public Page<Restaurant> findByPriceRangeLessThanEqualOrderByCreatedAtDesc(Integer priceRange, Pageable pageable);
    public Page<Restaurant> findByPriceRangeLessThanEqualOrderByPriceRangeAsc(Integer priceRange, Pageable pageable); 
    public Page<Restaurant> findAllByOrderByCreatedAtDesc(Pageable pageable);
    public Page<Restaurant> findAllByOrderByPriceRangeAsc(Pageable pageable);   
    
    // ---------------------------------------------------------------
    // ① 価格が高い順ですべて取得
    // ---------------------------------------------------------------
    public Page<Restaurant> findAllByOrderByPriceRangeDesc(Pageable pageable);

    // ---------------------------------------------------------------
    // ② キーワード検索（店名 or 住所）＋価格が高い順
    // ---------------------------------------------------------------
    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByPriceRangeDesc(String nameKeyword, String addressKeyword, Pageable pageable);

    // ---------------------------------------------------------------
    // ③ エリア検索（住所に含まれる）＋価格が高い順
    // ---------------------------------------------------------------
    public Page<Restaurant> findByAddressLikeOrderByPriceRangeDesc(String areaKeyword, Pageable pageable);

    // ---------------------------------------------------------------
    // ④ 価格範囲以内 + 価格が高い順
    // ---------------------------------------------------------------
    public Page<Restaurant> findByPriceRangeLessThanEqualOrderByPriceRangeDesc(Integer priceRange, Pageable pageable);

    // ===============================================================
    // ここから下は「カテゴリ」検索を今後実装する場合に追加
    // （必要なければ削除してOK）
    // ===============================================================

    // ---------------------------------------------------------------
    // ⑤ カテゴリ検索 + 価格が高い順
    // ---------------------------------------------------------------
    public Page<Restaurant> findByCategoryIdOrderByPriceRangeDesc(Integer categoryId, Pageable pageable);

    // ---------------------------------------------------------------
    // ⑥ カテゴリ検索 + 価格範囲以内 + 価格が高い順
    // ---------------------------------------------------------------
    public Page<Restaurant> findByCategoryIdAndPriceRangeLessThanEqualOrderByPriceRangeDesc(Integer categoryId, Integer priceRange, Pageable pageable);

    // ---------------------------------------------------------------
    // ⑦ カテゴリ検索 + 新着順（または安い順）など
    //    (既にあるかもしれませんが無ければ追加)
    // ---------------------------------------------------------------
    public Page<Restaurant> findByCategoryIdOrderByCreatedAtDesc(Integer categoryId, Pageable pageable);
    public Page<Restaurant> findByCategoryIdOrderByPriceRangeAsc(Integer categoryId, Pageable pageable);
    public Page<Restaurant> findByCategoryIdAndPriceRangeLessThanEqualOrderByCreatedAtDesc(Integer categoryId, Integer priceRange, Pageable pageable);
    public Page<Restaurant> findByCategoryIdAndPriceRangeLessThanEqualOrderByPriceRangeAsc(Integer categoryId, Integer priceRange, Pageable pageable);

}
