package com.example.nagoyameshi.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RestaurantRegisterForm {
    @NotBlank(message = "店名を入力してください。")
    private String name;
    
//    これで画像がいけるらしい
    private MultipartFile imageFile;
    
    @NotBlank(message = "営業時間を入力してください。")
    private String businessHours;
    
    @NotBlank(message = "定休日を入力してください。")
    private String regularClosingDay;
    
    @NotNull(message = "価格帯を入力してください。")
    @Min(value = 300, message = "料金はセットメニュー300円以上に設定してください。")
    private Integer priceRange;  
    
//    カテゴリ後でやる
    @NotNull(message = "カテゴリを選択してください。")
    private Integer categoryId;
    
    @NotNull(message = "総席数を入力してください。")
    @Min(value = 1, message = "席数は1人以上に設定してください。")
    private Integer numOfSeats; 
    
    @NotBlank(message = "電話番号を入力してください。")
    private String phoneNumber;
    
    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;
    
    @NotBlank(message = "住所を入力してください。")
    private String address;
    
    @NotBlank(message = "説明を入力してください。")
    private String description;   
    
}
