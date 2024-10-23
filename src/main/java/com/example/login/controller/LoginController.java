package com.example.login.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // ログインページのGETリクエストを処理するメソッド
    @GetMapping("/loginpage")
    public String showLoginPage() {
        // "login.html"テンプレートを表示
        return "login/login";
    }
    
    // メインメニュー画面を表示するメソッド
    @GetMapping("/mainmenu")
    public String showMainMenu() {
        // 現在の認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 認証されていない場合は、ログインページにリダイレクト
        if (authentication == null || !authentication.isAuthenticated()) {
            // 未認証時には"/loginpage"にリダイレクト
            return "redirect:/loginpage";  
        }
        // 認証されている場合は、"mainmenu.html"を表示
        return "mainmenu";
    }

    // メインメニュー画面に移動するためのメソッド(login画面以外からの遷移)
    @GetMapping("/moveMainMenu")
    public String moveMainMenu() {
        // "mainmenu.html"テンプレートを表示
        return "mainmenu";
    }
}
