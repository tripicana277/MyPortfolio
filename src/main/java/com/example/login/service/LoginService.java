package com.example.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.login.model.Login;
import com.example.login.repository.LoginRepository;

@Service // サービス層としてこのクラスをSpringのコンポーネントに登録する
public class LoginService implements UserDetailsService {

	// LoginRepositoryのインスタンスを自動的に注入
	@Autowired
	private LoginRepository loginRepository;

	// ユーザー名に基づいてユーザー情報をロードするメソッド（Spring Securityの認証で使用）
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// ユーザー名に基づいてLoginエンティティを検索し、見つからない場合は例外をスロー
		Login login = loginRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));

		// UserDetailsを構築し、認証に必要な情報を返す
		return User.withUsername(login.getUsername()) // ユーザー名を設定
				.password(login.getPassword()) // パスワード（データベースから取得したもの）を設定
				.roles("USER") // 必要に応じてロールを設定（ここでは"USER"ロールを指定）
				.build(); // UserDetailsオブジェクトを構築
	}
}
