package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ResetPasswordForm {

    // トークンを受け渡しするためのフィールド
    @NotBlank
    private String token;

    // 新しいパスワード
    @NotBlank(message = "新しいパスワードを入力してください。")
    private String newPassword;

    // 確認用
    @NotBlank(message = "新しいパスワード(確認用)を入力してください。")
    private String newPasswordConfirmation;
}
