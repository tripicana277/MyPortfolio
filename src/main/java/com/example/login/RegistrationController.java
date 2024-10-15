package com.example.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // 空のユーザーオブジェクトを作成してフォームにバインド
        model.addAttribute("user", new Login());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Login user) {
        // ユーザーを登録
        userService.registerUser(user);
        return "redirect:/login";
    }
}
