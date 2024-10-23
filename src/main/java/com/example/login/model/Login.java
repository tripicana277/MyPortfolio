package com.example.login.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Login {

	@Id // このフィールドがテーブルの主キーであることを示す
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 主キーの値を自動生成する（自動インクリメント）
	private Long id; // 主キー

	private String username;// ユーザー名を格納するフィールド
	private String password;// パスワードを格納するフィールド
}
