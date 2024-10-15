package com.example.recipeApp.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.recipeApp.entity.RecipeMain;

import lombok.RequiredArgsConstructor;

//ロジックを管理するクラス
@Service
@RequiredArgsConstructor
public class RecipeLogic {

    private final RecipeService recipeService;

    // 新しいレシピを保存するメソッド
    public List<RecipeMain> addRecipe(RecipeMain recipeMain) throws SQLException {
    	String recipeName = recipeService.addOne(recipeMain);
        return recipeService.getOne(recipeName); // DAOを使用してデータベースにレシピを保存
    }
    
    // 指定されたIDや文字列に基づいてRecipeオブジェクトを取得するメソッド
    public List<RecipeMain> getRecipeByName(String recipeName) throws SQLException {
        return recipeService.getOne(recipeName); // DAOを使用してデータベースから指定された項目を取得
    }

    // 全てのRecipeオブジェクトを取得するメソッド
    public List<RecipeMain> getAllRecipes() throws SQLException {
        return recipeService.getAll(); // DAOを使用してデータベースから全てのRecipeを取得
    }

    // 指定されたレシピを削除するメソッド
    public List<RecipeMain> deleteRecipe(String recipeName) throws SQLException {
        recipeService.deleteOne(recipeName); // DAOを使用してデータベースから指定された項目を削除
        return recipeService.getAll(); // DAOを使用してデータベースから全てのRecipeを取得    
    }
}
