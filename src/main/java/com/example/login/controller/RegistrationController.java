package com.example.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.login.model.Login;
import com.example.login.service.RegistrationService;

@Controller
public class RegistrationController {

    // RegistrationServiceのインスタンスを自動注入
    @Autowired
    private RegistrationService registrationService;

    // GETリクエストでユーザー登録フォームを表示するメソッド
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // 空のLoginオブジェクトを作成し、フォームにバインド
        model.addAttribute("user", new Login());
        // "register.html"を表示（ユーザー登録画面）
        return "login/register";
    }

    // POSTリクエストでユーザーを登録するメソッド
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Login user) {
        // RegistrationServiceを使用して新しいユーザーを登録
        registrationService.registerUser(user);
        // 登録完了後、ログインページにリダイレクト
        return "redirect:/login";
    }
}
