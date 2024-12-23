package com.example.nagoyameshi.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ForgotPasswordForm {

    @NotBlank(message = "メールアドレスを入力してください。")
    @Email(message = "メールアドレスの形式が正しくありません。")
    private String mailAddress;
}
