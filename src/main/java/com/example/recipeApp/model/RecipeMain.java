package com.example.recipeApp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* レシピデータ
*/
@Data // Lombokを使用して、getter/setterなどを自動生成
@AllArgsConstructor // 全フィールドを初期化するコンストラクタを自動生成
@NoArgsConstructor // 引数なしのデフォルトコンストラクタを自動生成
public class RecipeMain {

	// レシピ名を保持するフィールド
	private String recipeName; 
	
	// 画像データを保持するフィールド（Imageクラスのインスタンス）
	private Image imagefile; 
	
	// レシピに対するコメントを保持するフィールド
	private String comment; 
	
	// 何人分のレシピかを保持するフィールド
	private String number; 
	
	// 材料リスト（RecipeSubMaterialクラスのリスト）を保持
	private List<RecipeSubMaterial> recipeSubMaterials; 
	
	// 作り方リスト（RecipeSubHowToMakeクラスのリスト）を保持
	private List<RecipeSubHowToMake> recipeSubHowToMakes; 

	// レシピ画像のデータを保持する内部クラス
	@Data
	public class Image {
		// 画像ファイル名
		private String fileName; 
		
		// 画像データ（バイト配列で保持）
		private byte[] fileData;
		
		// 表示用にエンコードされたファイルデータ
		private String fileDataView;
	}
	
	// レシピ材料のデータを保持する内部クラス
	@Data
	public class RecipeSubMaterial {
		// 材料名を保持
		private String material; 
		
		// 材料の数量を保持
		private String quantity;
	}
	
	@Data
	// 作り方とその画像データを保持する内部クラス
	public class RecipeSubHowToMake {
		// 作り方に関連する画像データを保持
		private Image imagefile2; 
		
		// 作り方の詳細を保持
		private String howToMake; 
	}

	// レシピ名とファイル名を引数にとるコンストラクタ
	public RecipeMain(String recipeName, String fileName) {
		// レシピ名を設定
		this.setRecipeName(recipeName); 
		
		// ファイル名を設定
		this.setRecipeName(fileName); 
	}

	// レシピ名、画像データ、コメント、人数を引数にとるコンストラクタ
	public RecipeMain(String recipeName, Image fileName, String comment, String number) {
		// レシピ名を設定
		this.setRecipeName(recipeName); 
		
		// 画像データを設定
		this.setImagefile(fileName); 
		
		// コメントを設定
		this.setComment(comment); 
		
		// 人数を設定
		this.setNumber(number); 
	}
}
