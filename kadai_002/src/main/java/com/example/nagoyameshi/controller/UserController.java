package com.example.nagoyameshi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	private final UserRepository userRepository;
	private final UserService userService;
	
	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}
	/*主な役割
	認証済みのユーザー情報を取得

	@AuthenticationPrincipal を使って現在ログインしているユーザー（UserDetailsImpl）の情報を取得します。
	ユーザー詳細情報の取得

	ログインしているユーザーのIDを使用して、データベースから対応するユーザー（Userエンティティ）を取得します。
	テンプレートにユーザー情報を渡す

	取得したユーザー情報をビュー（user/index.html）に渡します。
	ビューのレンダリング

	user/index.html がレンダリングされ、ユーザー情報が表示されます。 */
	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
	    System.out.println("Index method invoked for /user");

	    if (userDetailsImpl == null) {
	        System.err.println("AuthenticationPrincipal is null");
	        return "redirect:/login"; // ログイン画面にリダイレクト
	    }

	    if (userDetailsImpl.getUser() == null) {
	        System.err.println("UserDetailsImpl.getUser() returned null");
	        return "redirect:/login";
	    }

	    User user = userRepository.findById(userDetailsImpl.getUser().getId())
	            .orElse(null);
	    if (user == null) {
	        System.err.println("User not found in database for ID: " + userDetailsImpl.getUser().getId());
	        return "redirect:/login";
	    }

	    System.out.println("User found: " + user.getId());
	    model.addAttribute("user", user);
	    return "user/index";
	}

	
	// 会員情報編集
	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		
		// パスワードは空欄にして表示
		
		UserEditForm userEditForm = new UserEditForm(user.getId(), user.getMailAddress(), "", "", user.getName(), user.getPhoneNumber(), user.getPostalCode(), user.getAddress());
		
		model.addAttribute("userEditForm", userEditForm);
		
		return "user/edit";
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		// メールアドレスが変更されており、かつ登録済みであればBindingResultオブジェクトエラー内容を追加する
		if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getMailAddress())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "mailAddress", "既に登録済みのメールアドレスです");
			bindingResult.addError(fieldError);
		}
		
		if (bindingResult.hasErrors()) {
			return "user/edit";
		}
		
		userService.update(userEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
		return "redirect:/user";
	}

}


