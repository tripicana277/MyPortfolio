package com.example.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.login.model.Login;
import com.example.login.repository.RegisterRepository;

@Service // このクラスをサービスとしてSpringコンテナに登録する
public class RegistrationService {

	// RegisterRepositoryのインスタンスを自動的に注入
	@Autowired
	private RegisterRepository registerRepository;

	// PasswordEncoderのインスタンスを自動的に注入
	@Autowired
	private PasswordEncoder passwordEncoder;

	// 新しいユーザーを登録するメソッド
	public void registerUser(Login user) {
		// ユーザーのパスワードをエンコード（暗号化）
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		// エンコードされたパスワードを持つユーザー情報をデータベースに保存
		registerRepository.save(user);
	}
}
