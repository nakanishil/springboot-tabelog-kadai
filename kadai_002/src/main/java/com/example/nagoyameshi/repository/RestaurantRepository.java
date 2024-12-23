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
}