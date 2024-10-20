package com.example.recipeApp.controller;

import java.io.IOException;
import java.sql.SQLException;
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

import com.example.recipeApp.entity.RecipeMain;
import com.example.recipeApp.entity.RecipeMain.Image;
import com.example.recipeApp.entity.RecipeMain.RecipeSubHowToMake;
import com.example.recipeApp.entity.RecipeMain.RecipeSubMaterial;
import com.example.recipeApp.service.RecipeLogic;

@Controller("recipeMainController")
public class MainController {

	@Autowired
	private RecipeLogic recipeLogic;
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	// 新しいレシピの追加 (POST)
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
		    logger.debug("TestTakeda6");

			// ファイルアップロード
			Image fileName = uploadFile(file);
			if (fileName == null) {
				logger.warn("メインレシピ画像がアップロードされていません");
			}

			// 材料リスト作成
			List<RecipeSubMaterial> recipeSubMaterials = createRecipeSubMaterials(materials, quantities);

			// 作り方リスト作成
			List<RecipeSubHowToMake> recipeSubHowToMakes = createRecipeSubHowToMakes(howToMakeFiles, howToMakes);

			// RecipeMainオブジェクトを作成
			RecipeMain recipeMain = new RecipeMain(recipeName, fileName, comment, number, recipeSubMaterials,
					recipeSubHowToMakes);
		    logger.debug("TestTakeda7");
			
			// レシピ保存
		    RecipeMain  recipeMainTmp = recipeLogic.addRecipe(recipeMain); // 単一のオブジェクトとして保存

//		    Recipe recipe = recipeService.findById(id);
//		    
//		    if (recipe != null && recipe.getImagefile() != null && recipe.getImagefile().getFileData() != null) {
//		        String base64EncodedImage = Base64.getEncoder().encodeToString(recipe.getImagefile().getFileData());
//		        model.addAttribute("base64Image", recipeMainTmp.getImagefile().getFileDataView());
//		    }
			
//			model.addAttribute("recipe", recipeMain);
			model.addAttribute("recipe", recipeMainTmp); // 最初のレシピを表示

			logger.debug("新しいレシピが追加されました: {}", recipeName);

		} catch (IOException | SQLException e) {
			logger.error("レシピの追加中にエラーが発生しました", e);
			return "error"; // エラーページにリダイレクト
		}
		return "recipe/recipeImageView"; // 新しいレシピ表示ページ
	}
	
	// レシピリストの取得 (GET)
	@GetMapping("/recipes")
	public String getRecipes(Model model) {
	    try {
	        logger.debug("レシピの取得処理を開始");
	        List<RecipeMain> recipeList = recipeLogic.getAllRecipes();
	        
	        logger.debug("取得したレシピ数: {}", recipeList.size());
	        model.addAttribute("recipeList", recipeList);
	    } catch (SQLException e) {
	        logger.error("SQLエラーが発生しました: {}", e.getMessage(), e);
	        return "error";
	    } catch (Exception e) {
	        logger.error("予期せぬエラーが発生しました: {}", e.getMessage(), e);
	        return "error";
	    }
	    return "recipe/recipeAll";
	}

	// 特定のレシピの取得 (GET)
	@GetMapping("/recipe")
	public String getRecipe(@RequestParam("button") String recipeName, Model model) {
		try {
			RecipeMain recipeMain = recipeLogic.getRecipeByName(recipeName);
//			model.addAttribute("base64Image", recipeMain.getImagefile().getFileData());
//	        model.addAttribute("base64Image", recipeMain.getImagefile().getFileDataView());
			model.addAttribute("recipe", recipeMain); // 最初のレシピを表示
			
		} catch (SQLException e) {
			logger.error("レシピの取得中にSQLエラーが発生しました", e);
			return "error"; // エラーページにリダイレクト
		}
		return "recipe/recipeImageView"; // 特定のレシピ表示ページ
	}
	
	// レシピの削除 (POST)
	@PostMapping("/deleteRecipe")
	public String deleteRecipe(@RequestParam("delete") String recipeName, Model model) {
		try {
			logger.debug("レシピを削除しました: {}", recipeName);
			List<RecipeMain> recipeList = recipeLogic.deleteRecipe(recipeName); // レシピ削除
			model.addAttribute("recipeList", recipeList);
		} catch (SQLException e) {
			logger.error("レシピの削除中にエラーが発生しました", e);
			return "error"; // エラーページにリダイレクト
		}
		return "recipe/recipeAll"; // 更新されたレシピ一覧を表示
	}

	// ファイルアップロード処理
	private Image uploadFile(MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			logger.warn("アップロードされたファイルが空です");
			return null;
		}
		
		Image image = new RecipeMain().new Image();
		image.setFileName(file.getOriginalFilename());
		image.setFileData(file.getBytes());
//		image.setFileData(Base64.getEncoder().encodeToString(convertToJpeg(file.getBytes())));
		return image;
	}

	// 材料リストの作成
	private List<RecipeSubMaterial> createRecipeSubMaterials(String[] materials, String[] quantities) {
		List<RecipeSubMaterial> recipeSubMaterials = new ArrayList<>();
		for (int i = 0; i < materials.length; i++) {
			RecipeSubMaterial sub = new RecipeMain().new RecipeSubMaterial();
			sub.setMaterial(materials[i]);
			sub.setQuantity(quantities[i]);
			recipeSubMaterials.add(sub);
		}
		return recipeSubMaterials;
	}

	// 作り方リストの作成
	private List<RecipeSubHowToMake> createRecipeSubHowToMakes(List<MultipartFile> files, String[] howToMakes)
			throws IOException {
		List<RecipeSubHowToMake> recipeSubHowToMakes = new ArrayList<>();
		for (int i = 0; i < files.size(); i++) {
			Image image = uploadFile(files.get(i));
			RecipeSubHowToMake sub = new RecipeMain().new RecipeSubHowToMake();
			sub.setImagefile2(image);
			sub.setHowToMake(howToMakes[i]);
			recipeSubHowToMakes.add(sub);
		}
		return recipeSubHowToMakes;
	}
	

}
