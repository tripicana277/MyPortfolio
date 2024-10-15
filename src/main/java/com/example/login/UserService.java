package com.example.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Service
@Table(name = "login") // データベースのテーブル名を指定
public class UserService {

    @Autowired
    private RegisterUserRepository registerUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(Login user) {
        // パスワードをエンコード
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // ユーザー情報をデータベースに保存
        registerUserRepository.save(user);
    }
}