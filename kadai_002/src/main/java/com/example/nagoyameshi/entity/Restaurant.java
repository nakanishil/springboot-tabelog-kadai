package com.example.nagoyameshi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "restaurants")
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "name", nullable = false, unique = true, length =30)
    private String name;
    
    @Column(name = "business_hours", nullable = false, length = 30)
    private String businessHours;
    
    @Column(name = "regular_closing_day", nullable = false, length = 10)
    private String regularClosingDay;
    
    @Column(name = "price_range", nullable = false)
    private Integer priceRange;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_restaurants_categories"))
    private Category category;
    
    @Column(name = "num_of_seats", nullable = false)
    private Integer numOfSeats;
    
    @Column(name = "phone_number", nullable = false, length = 30)
    private String phoneNumber;
    
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;
    
    @Column(name = "address", nullable = false, length = 255)
    private String address;
    
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true; // デフォルト値をtrueに設定
    
    @Column(name = "image_name", length = 255)
    private String imageName;
	
}
