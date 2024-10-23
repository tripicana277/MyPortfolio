package com.example.recipeApp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.recipeApp.model.RecipeMain;

import lombok.RequiredArgsConstructor;

// ロジックを管理するクラス
@Service // Springのサービスコンポーネントとして登録
@RequiredArgsConstructor // finalフィールドに対してコンストラクタを自動生成するLombokアノテーション
public class RecipeLogic {

    // RecipeServiceのインスタンスを自動的に注入
    private final RecipeService recipeService;

    /**
     * レシピの追加処理
     * @param recipeMain - 追加するレシピのデータ
     * @return RecipeMain - データベースに保存されたレシピ
     * @throws Exception - SQL実行時に発生する例外
     */
    public RecipeMain addRecipe(RecipeMain recipeMain) throws Exception {
    	
    	// レシピをデータベースに追加し、追加したレシピの名前を取得
    	String recipeName = recipeService.addOne(recipeMain);
    	
    	// 追加したレシピの詳細を取得して返す
        return recipeService.getOne(recipeName); 
    }
    
    /**
     * 個別レシピの取得処理
     * @param recipeName - 取得したいレシピの名前
     * @return RecipeMain - 取得されたレシピ
     * @throws Exception - SQL実行時に発生する例外
     */
    public RecipeMain getRecipeByName(String recipeName) throws Exception {
        // DAOを使用して、指定されたレシピをデータベースから取得
        return recipeService.getOne(recipeName); 
    }

    /**
     * 全レシピの取得処理
     * @return List<RecipeMain> - 全てのレシピのリスト
     * @throws Exception - SQL実行時に発生する例外
     */
    public List<RecipeMain> getAllRecipes() throws Exception {
        // DAOを使用して、データベースから全てのレシピを取得
        return recipeService.getAll(); 
    }

    /**
     * レシピの削除処理
     * @param recipeName - 削除するレシピの名前
     * @return List<RecipeMain> - 更新された全レシピのリスト
     * @throws Exception - SQL実行時に発生する例外
     */
    public List<RecipeMain> deleteRecipe(String recipeName) throws Exception {
        // DAOを使用して、指定されたレシピをデータベースから削除
        recipeService.deleteOne(recipeName); 
        
        // レシピ削除後、全てのレシピを再取得して返す
        return recipeService.getAll(); 
    }
}
