package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// .csrf().disable() // 必要であればCSRF保護を無効化
				.authorizeHttpRequests(authorize -> authorize
						// 静的リソースとログイン、メインメニュー、登録ページは許可
						.requestMatchers("/css/**", "/images/**", "/uploads/**", "/js/**", "/login", "/register")
						.permitAll()
						// それ以外のページは認証が必要
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/loginpage") // カスタムログインページ
						.loginProcessingUrl("/login") // ログイン処理のURL
						.defaultSuccessUrl("/mainmenu", true) // ログイン成功時のリダイレクト先
						.failureUrl("/loginpage?error=true") // ログイン失敗時のリダイレクト先
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout") // ログアウトのURL
						.logoutSuccessUrl("/loginpage?logout=true") // ログアウト成功後のリダイレクト先
						.permitAll());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
