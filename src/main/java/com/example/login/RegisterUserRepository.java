package com.example.login;

import jakarta.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;

@Table
public interface RegisterUserRepository extends JpaRepository<Login, Long> {
	Login findByUsername(String username);
}
