package com.example.login;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterUserRepository extends JpaRepository<Login, Long> {
	Login findByUsername(String username);
}
