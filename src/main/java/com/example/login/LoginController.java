package com.example.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // ログインページのGETリクエストを処理するメソッド
    @GetMapping("/loginpage")
    public String showLoginPage() {
        // login.htmlを返す
        return "login";
    }
    
    @GetMapping("/mainmenu")
    public String showMainMenu() {
    	
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {

        	// 認証されていない場合、ログインページにリダイレクト
        	return "redirect:/loginpage";  
        }

        return "mainmenu";
    }
}