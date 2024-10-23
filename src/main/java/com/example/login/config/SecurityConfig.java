package com.example.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Securityを有効化するアノテーション
public class SecurityConfig {

	// SecurityFilterChain Beanの定義。HttpSecurityを使用してセキュリティ設定を行う
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// リクエストに対する認可設定
				.authorizeHttpRequests(authorize -> authorize
						// 静的リソース（CSS、画像、JSファイルなど）とログイン、登録ページは誰でもアクセス可能にする
						.requestMatchers("/css/**", "/images/**", "/uploads/**", "/js/**", "/login", "/register")
						.permitAll()
						// その他のリクエストは認証が必要
						.anyRequest().authenticated())
				// フォームログインの設定
				.formLogin(form -> form
						// カスタムログインページの指定
						.loginPage("/loginpage") 
						// 認証処理を行うURLの指定
						.loginProcessingUrl("/login") 
						// ログイン成功時のリダイレクト先（メインメニュー）
						.defaultSuccessUrl("/mainmenu", true) 
						// ログイン失敗時のリダイレクト先（エラーメッセージ付き）
						.failureUrl("/loginpage?error=true") 
						// ログインページ自体へのアクセスを許可
						.permitAll())
				// ログアウトの設定
				.logout(logout -> logout
						// ログアウト処理のURLを指定
						.logoutUrl("/logout")
						// ログアウト成功後のリダイレクト先を指定
						.logoutSuccessUrl("/loginpage?logout=true")
						// ログアウトページ自体へのアクセスを許可
						.permitAll());

		// SecurityFilterChainの設定を反映して返す
		return http.build();
	}

	// パスワードエンコーダーとしてBCryptを使用するためのBean定義
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // BCryptPasswordEncoderは、パスワードをハッシュ化するために使用される
	}
}
