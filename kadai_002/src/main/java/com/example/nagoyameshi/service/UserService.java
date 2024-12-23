package com.example.nagoyameshi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional
	public User create(SignupForm signupForm) {
		User user = new User();
		Role role = roleRepository.findByName("ROLE_FREE_MEMBER");
		
		user.setMailAddress(signupForm.getMailAddress());
		// パスワードをハッシュ化 encode()
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		user.setName(signupForm.getName());
		user.setPhoneNumber(signupForm.getPhoneNumber());
		user.setPostalCode(signupForm.getPostalCode());
		user.setAddress(signupForm.getAddress());
		user.setRole(role);
		user.setEnabled(false);
		
		return userRepository.save(user);
	}
	
	@Transactional
	public void update(UserEditForm userEditForm) {
		User user = userRepository.getReferenceById(userEditForm.getId());
		
		user.setMailAddress(userEditForm.getMailAddress());
        user.setName(userEditForm.getName());
     // パスワードをハッシュ化 encode()
        user.setPassword(passwordEncoder.encode(userEditForm.getPassword()));
        user.setPostalCode(userEditForm.getPostalCode());
        user.setAddress(userEditForm.getAddress());
        user.setPhoneNumber(userEditForm.getPhoneNumber());
           
        userRepository.save(user);
	}
	
	// メールアドレスが登録済みかどうかをチェックする
	public boolean isEmailRegistered(String mailAddress) {
		User user = userRepository.findByMailAddress(mailAddress);
		// メールアドレスが登録済みであればtrueを返す
		return user != null;
	}
	
	// パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }     
    
    // ユーザを有効にする
    @Transactional
    public void enableUser(User user) {
    	user.setEnabled(true);
    	userRepository.save(user);
    }
    
    // メールアドレスが変更されたかどうかをチェックする
    public boolean isEmailChanged(UserEditForm userEditForm) {
    	User currentUser = userRepository.getReferenceById(userEditForm.getId());
    	return !userEditForm.getMailAddress().equals(currentUser.getMailAddress());
    }
    
    // ユーザーが無料会員かどうかを判定するメソッド
    public boolean isFreeMember(User user) {
        return "ROLE_FREE_MEMBER".equals(user.getRole().getName());
    }

    // ユーザーが有料会員かどうかを判定するメソッド
    public boolean isPayingMember(User user) {
        System.out.println("Checking if user is paying member: " + user); // デバッグ用ログ
        return user.getRole().getName().equals("ROLE_PAYING_MEMBER");
    }
    
 // メールアドレスでユーザーを検索
    public User findByEmail(String email) {
        return userRepository.findByMailAddress(email); // UserRepository のメソッドを呼び出す
    }
    
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }



}
