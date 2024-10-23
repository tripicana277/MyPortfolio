package com.example.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.login.model.Login;

public interface RegisterRepository extends JpaRepository<Login, Long> {
    
    // Optionalを使用して、結果が存在しない場合の処理を簡単にする
    Optional<Login> findByUsername(String username);
}
