package com.example.nagoyameshi.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.UserRepository;


// ユーザ情報をデータベースから取得し、認証用のデータに変換する
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	
	// コンストラクタでuserRepositoryを受け取り、データベース操作を行う。
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	//Spring Securityがユーザー名（ここではメールアドレス）を使ってユーザ情報を取得する際に呼び出される
	public UserDetails loadUserByUsername(String email )throws UsernameNotFoundException {
		
		System.out.println("検索対象メールアドレス: " + email);
		System.out.println("取得したユーザ:" + userRepository.findByMailAddress(email));
		
		try {
			// findMyMailAddressを使いメールアドレスでユーザ情報を検索
			User user = userRepository.findByMailAddress(email);
			// 権限を設定
			String userRoleName = user.getRole().getName();
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(userRoleName));
			return new UserDetailsImpl(user, authorities);
		} catch (Exception e) {
			throw new UsernameNotFoundException("ユーザが見つかりませんでした。", e);
		}
	}
}
