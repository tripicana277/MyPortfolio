package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoveController {

	private static final Logger logger = LoggerFactory.getLogger(MoveController.class);

    // ログインページのGETリクエストを処理するメソッド
    @GetMapping("/addRecipeForm")
    public String addRecipePage() {
        // recipe.htmlを返す
	    logger.debug("TestTakeda8");
        return "recipe/recipe";
    }

    @GetMapping("/moveMainMenu")
    public String moveMainMenu() {
	    logger.debug("TestTakeda9");
    	
        // mainManu.htmlを返す
        return "mainmenu";
    }
    
    @GetMapping("/moveRecipeAll")
    public String moveRecipeAll() {
    	
	    logger.debug("TestTakeda10");

        // recipeAll.htmlを返す
        return "recipe/recipeAll";
    }
}