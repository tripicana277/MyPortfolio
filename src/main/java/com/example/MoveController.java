package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoveController {

    // ログインページのGETリクエストを処理するメソッド
    @GetMapping("/addRecipeForm")
    public String addRecipePage() {
        // recipe.htmlを返す
        return "recipe/recipe";
    }

    @GetMapping("/moveMainMenu")
    public String moveMainMenu() {
    	
        // mainManu.htmlを返す
        return "mainmenu";
    }
    
    @GetMapping("/moveRecipeAll")
    public String moveRecipeAll() {
        // recipeAll.htmlを返す
        return "recipe/recipeAll";
    }
}