package com.example.nagoyameshi.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEditForm {
    @NotNull
    private Integer id;
    
    @NotBlank(message = "メールアドレスを入力してください。")
    @Email(message = "メールアドレスは正しい形式で入力してください。")
    private String mailAddress;
    
	@NotBlank(message = "パスワードを入力してください。")
	@Length(min = 8, max = 60, message = "パスワードは8文字以上で入力してください。")
	private String password;
	
	@NotBlank(message = "パスワード(確認用)を入力してください。")
	private String passwordConfirmation;
    
    @NotBlank(message = "氏名を入力してください。")
    private String name;
    
    @NotBlank(message = "電話番号を入力してください。")
    private String phoneNumber;
    
    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;
    
    @NotBlank(message = "住所を入力してください。")
    private String address;
    
}
