package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FavoriteForm {
    
    @NotNull
    private Integer restaurantId;

    // コメントは任意入力にしたい場合、必須アノテーションなし
    private String favoriteComment;
}
