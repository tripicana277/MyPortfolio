package com.example.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.login.model.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
    
    // ユーザー名に基づいてユーザーを検索するメソッドの定義
    Optional<Login> findByUsername(String username);
}
