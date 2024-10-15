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
				.authorizeHttpRequests(authorize -> authorize
						// 静的リソースとログインページは許可
//						.requestMatchers("/css/**", "/images/**", "/uploads/**", "/js/**"
//								/*"/mainmenu",*/ 
//								, "/login", "/register")
						.requestMatchers("/css/**", "/images/**", "/uploads/**", "/js/**"
						, "/resipe"
						, "/recipeAll"
						, "/recipeImageView"
						, "/income"
						, "/statistics"
						, "/mainmenu"
						, "/register"
						, "/login", "/register")
						.permitAll()
						.anyRequest().authenticated() // その他は認証が必要
				)
				.formLogin(form -> form
						.loginPage("/loginpage")
				        .loginProcessingUrl("/login")  // POSTリクエストで送信されるURL
						.defaultSuccessUrl("/mainmenu", true)
		                .failureUrl("/loginpage?error=true") // ログイン失敗時のURL
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")  // ログアウトURL
						.logoutSuccessUrl("/loginpage?logout=true")  // ログアウト成功時にリダイレクト
						.permitAll());
		return http.build();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
