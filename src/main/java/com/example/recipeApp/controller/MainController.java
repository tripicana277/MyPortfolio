package com.example.recipeApp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.recipeApp.model.RecipeMain;
import com.example.recipeApp.model.RecipeMain.Image;
import com.example.recipeApp.model.RecipeMain.RecipeSubHowToMake;
import com.example.recipeApp.model.RecipeMain.RecipeSubMaterial;
import com.example.recipeApp.service.RecipeLogic;

@Controller("recipeMainController") // このコントローラーを"recipeMainController"としてSpringコンテナに登録
public class MainController {

	// RecipeLogicクラスのインスタンスを自動的に注入
	@Autowired
	private RecipeLogic recipeLogic;

	// ログ用にLoggerを設定
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	/**
	 * レシピ追加ページの表示
	 */
	@GetMapping("/addRecipeForm")
	public String addRecipePage() {
		// レシピ追加フォームページ（recipe.html）を返す
		return "recipe/recipe";
	}

	/**
	 * レシピアプリの全体表示ページに遷移
	 */
	@GetMapping("/moveRecipeAll")
	public String moveRecipeAll() {
		// 全てのレシピを表示するページ（recipeAll.html）を返す
		return "recipe/recipeAll";
	}

	/**
	 * レシピの追加処理
	 */
	@PostMapping("/addRecipe")
	public String addRecipe(@RequestParam("fileName") MultipartFile file,
			@RequestParam("recipeName") String recipeName,
			@RequestParam("comment") String comment,
			@RequestParam("number") String number,
			@RequestParam("material[]") String[] materials,
			@RequestParam("quantity[]") String[] quantities,
			@RequestParam("howToMake[]") String[] howToMakes,
			@RequestParam("fileName2[]") List<MultipartFile> howToMakeFiles,
			Model model) {
		try {
			// ファイルのアップロード処理
			Image fileName = uploadFile(file);
			if (fileName == null) {
				logger.warn("メインレシピ画像がアップロードされていません");
			}

			// 材料リストの作成
			List<RecipeSubMaterial> recipeSubMaterials = createRecipeSubMaterials(materials, quantities);

			// 作り方リストの作成
			List<RecipeSubHowToMake> recipeSubHowToMakes = createRecipeSubHowToMakes(howToMakeFiles, howToMakes);

			// RecipeMainオブジェクトを作成
			RecipeMain recipeMain = new RecipeMain(recipeName, fileName, comment, number, recipeSubMaterials,
					recipeSubHowToMakes);

			// レシピの保存
			RecipeMain recipeMainTmp = recipeLogic.addRecipe(recipeMain); // レシピをデータベースに保存
			model.addAttribute("recipe", recipeMainTmp); // 保存されたレシピをビューに渡す

			logger.debug("新しいレシピが追加されました: {}", recipeName);
		} catch (Exception e) {
			// 例外が発生した場合はエラーログを出力し、エラーページにリダイレクト
			logger.error("レシピの追加中にエラーが発生しました", e);
			return "error"; // エラーページに遷移
		}
		return "recipe/recipeImageView"; // レシピ表示ページに遷移
	}

	/**
	 * 全てのレシピを取得して表示
	 */
	@GetMapping("/recipes")
	public String getRecipes(Model model) {
		try {
			logger.debug("レシピの取得処理を開始");
			List<RecipeMain> recipeList = recipeLogic.getAllRecipes(); // 全レシピを取得
			logger.debug("取得したレシピ数: {}", recipeList.size());
			model.addAttribute("recipeList", recipeList); // レシピリストをモデルに追加
		} catch (Exception e) {
			// その他の例外が発生した場合もエラーログを出力し、エラーページに遷移
			logger.error("予期せぬエラーが発生しました: {}", e.getMessage(), e);
			return "error";
		}
		return "recipe/recipeAll"; // レシピ一覧ページに遷移
	}

	/**
	 * 特定のレシピを取得して表示
	 */
	@GetMapping("/recipe")
	public String getRecipe(@RequestParam("button") String recipeName, Model model) {
		try {
			RecipeMain recipeMain = recipeLogic.getRecipeByName(recipeName); // 指定されたレシピを取得
			model.addAttribute("recipe", recipeMain); // 取得したレシピをモデルに追加
		} catch (Exception e) {
			// SQL例外が発生した場合はエラーログを出力し、エラーページに遷移
			logger.error("レシピの取得中にエラーが発生しました", e);
			return "error";
		}
		return "recipe/recipeImageView"; // レシピ詳細ページに遷移
	}

	/**
	 * レシピの削除処理
	 */
	@PostMapping("/deleteRecipe")
	public String deleteRecipe(@RequestParam("delete") String recipeName, Model model) {
		try {
			logger.debug("レシピを削除しました: {}", recipeName);
			List<RecipeMain> recipeList = recipeLogic.deleteRecipe(recipeName); // 指定されたレシピを削除
			model.addAttribute("recipeList", recipeList); // 更新されたレシピリストをモデルに追加
		} catch (Exception e) {
			// SQL例外が発生した場合はエラーログを出力し、エラーページに遷移
			logger.error("レシピの削除中にエラーが発生しました", e);
			return "error";
		}
		return "recipe/recipeAll"; // レシピ一覧ページに遷移
	}

	/**
	 * ファイルのアップロード処理
	 */
	private Image uploadFile(MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			logger.warn("アップロードされたファイルが空です");
			return null;
		}
		// Imageオブジェクトを作成し、アップロードされたファイルを設定
		Image image = new RecipeMain().new Image();
		image.setFileName(file.getOriginalFilename());
		image.setFileData(file.getBytes());
		return image;
	}

	/**
	 * 材料リストの作成処理
	 */
	private List<RecipeSubMaterial> createRecipeSubMaterials(String[] materials, String[] quantities) {
		List<RecipeSubMaterial> recipeSubMaterials = new ArrayList<>();
		for (int i = 0; i < materials.length; i++) {
			RecipeSubMaterial sub = new RecipeMain().new RecipeSubMaterial(); // 新しい材料サブオブジェクトを作成
			sub.setMaterial(materials[i]); // 材料名を設定
			sub.setQuantity(quantities[i]); // 数量を設定
			recipeSubMaterials.add(sub); // リストに追加
		}
		return recipeSubMaterials;
	}

	/**
	 * 作り方リストの作成処理
	 */
	private List<RecipeSubHowToMake> createRecipeSubHowToMakes(List<MultipartFile> files, String[] howToMakes)
			throws IOException {
		List<RecipeSubHowToMake> recipeSubHowToMakes = new ArrayList<>();
		for (int i = 0; i < files.size(); i++) {
			// アップロードされたファイルを処理
			Image fileName = uploadFile(files.get(i));
			if (fileName == null) {
				logger.warn("作り方レシピ画像がアップロードされていません");
			}
			RecipeSubHowToMake sub = new RecipeMain().new RecipeSubHowToMake(); // 新しい作り方サブオブジェクトを作成
			sub.setImagefile2(fileName); // 作り方画像を設定
			sub.setHowToMake(howToMakes[i]); // 作り方の説明を設定
			recipeSubHowToMakes.add(sub); // リストに追加
		}
		return recipeSubHowToMakes;
	}
}
