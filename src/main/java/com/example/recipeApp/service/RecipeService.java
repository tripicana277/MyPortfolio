package com.example.recipeApp.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.recipeApp.model.RecipeMain;
import com.example.recipeApp.model.RecipeMain.Image;
import com.example.recipeApp.model.RecipeMain.RecipeSubHowToMake;
import com.example.recipeApp.model.RecipeMain.RecipeSubMaterial;

@Service
@Transactional // トランザクションを管理して、データ整合性を確保
public class RecipeService {

	// SQLクエリ定数
	private static final String SELECT_RECIPEMAIN = "SELECT * FROM RECIPEMAIN";
	private static final String SELECT_RECIPEMAIN_BY_NAME = "SELECT * FROM RECIPEMAIN WHERE RECIPENAME = ?";
	private static final String SELECT_MATERIAL_BY_NAME = "SELECT * FROM MATERIAL WHERE RECIPENAME = ?";
	private static final String SELECT_HOWTOMAKE_BY_NAME = "SELECT * FROM HOWTOMAKE WHERE RECIPENAME = ?";
	private static final String INSERT_RECIPEMAIN = "INSERT INTO RECIPEMAIN (RECIPENAME, FILENAME, FILEDATA, COMMENT, NUMBER) VALUES (?, ?, ?, ?, ?)";
	private static final String INSERT_MATERIAL = "INSERT INTO MATERIAL (RECIPENAME, MATERIAL, QUANTITY) VALUES (?, ?, ?)";
	private static final String INSERT_HOWTOMAKE = "INSERT INTO HOWTOMAKE (RECIPENAME, FILENAME2, FILEDATA2, HOWTOMAKE) VALUES (?, ?, ?, ?)";
	private static final String DELETE_RECIPEMAIN_BY_NAME = "DELETE FROM RECIPEMAIN WHERE RECIPENAME = ?";

	@Autowired
	private JdbcTemplate jdbcTemplate; // データベース操作用のJdbcTemplateを注入

	// ログの出力に使うLoggerの宣言
	// private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);

	/**
	 * レシピの追加処理
	 * @param recipeMain - 追加するレシピデータ
	 * @return String - 追加されたレシピの名前
	 */
	public String addOne(RecipeMain recipeMain) throws Exception {

		// レシピメイン情報をデータベースに挿入
		jdbcTemplate.update(INSERT_RECIPEMAIN, recipeMain.getRecipeName(),
				recipeMain.getImagefile().getFileName(),
				recipeMain.getImagefile().getFileData(),
				recipeMain.getComment(),
				recipeMain.getNumber());

		// 材料リストと作り方リストをそれぞれ挿入
		insertMaterials(recipeMain);
		insertHowToMakes(recipeMain);

		return recipeMain.getRecipeName(); // 挿入されたレシピの名前を返す
	}

	/**
	 * 個別レシピの取得処理
	 * @param name - 取得するレシピの名前
	 * @return RecipeMain - 取得されたレシピ
	 */
	public RecipeMain getOne(String name) throws Exception {
		// レシピのメイン情報をデータベースから取得
		List<RecipeMain> recipeMainList = jdbcTemplate.query(
//				List<RecipeMain> recipeMainList = jdbcTemplate.query(
				SELECT_RECIPEMAIN_BY_NAME,
				(PreparedStatement ps) -> {
					ps.setString(1, name);
				},
				recipeMainRowMapper() // ジェネリクスを確認
		);

		// 結果が空でない場合は、最初のレコードを取得
		if (!recipeMainList.isEmpty()) {
			RecipeMain recipeMain = recipeMainList.get(0);

			// 材料と作り方を追加で取得
			recipeMain.setRecipeSubMaterials(fetchMaterials(name));
			recipeMain.setRecipeSubHowToMakes(fetchHowToMakes(name));

			return recipeMain;
		}

		// 該当するレコードがない場合はnullを返す
		return null;
	}

	/**
	 * 全レシピの取得処理
	 * @return List<RecipeMain> - すべてのレシピのリスト
	 */
	public List<RecipeMain> getAll() throws Exception {
		// データベースから全てのレシピを取得
		return jdbcTemplate.query(SELECT_RECIPEMAIN, recipeMainRowMapper());
	}

	/**
	 * レシピの削除処理
	 * @param name - 削除するレシピの名前
	 */
	public void deleteOne(String name) {
		// 指定されたレシピをデータベースから削除
		jdbcTemplate.update(DELETE_RECIPEMAIN_BY_NAME, name);
	}

	/**
	 * 材料リストの取得処理
	 * @param name - レシピの名前
	 * @return List<RecipeSubMaterial> - 取得された材料リスト
	 */
	private List<RecipeSubMaterial> fetchMaterials(String name) throws Exception {
		// 材料リストをデータベースから取得
		return jdbcTemplate.query(SELECT_MATERIAL_BY_NAME,
				(PreparedStatement ps) -> {
					ps.setString(1, name);
				},
				recipeSubMaterialRowMapper() // ジェネリクスを確認
		);
	}

	/**
	 * 作り方リストの取得処理
	 * @param name - レシピの名前
	 * @return List<RecipeSubHowToMake> - 取得された作り方リスト
	 */
	private List<RecipeSubHowToMake> fetchHowToMakes(String name) throws Exception {
		// 材料リストをデータベースから取得
		return jdbcTemplate.query(SELECT_HOWTOMAKE_BY_NAME,
				(PreparedStatement ps) -> {
					ps.setString(1, name);
				},
				recipeSubHowToMakeRowMapper() // ジェネリクスを確認
		);
	}

	/**
	 * 材料の挿入処理
	 * @param recipeMain - 挿入するレシピのメイン情報
	 */
	private void insertMaterials(RecipeMain recipeMain) throws Exception {
		// 各材料をデータベースに挿入
		for (RecipeSubMaterial sub : recipeMain.getRecipeSubMaterials()) {
			jdbcTemplate.update(INSERT_MATERIAL, recipeMain.getRecipeName(), sub.getMaterial(), sub.getQuantity());
		}
	}

	/**
	 * 作り方の挿入処理
	 * @param recipeMain - 挿入するレシピのメイン情報
	 */
	private void insertHowToMakes(RecipeMain recipeMain) {
		// 各作り方をデータベースに挿入
		for (RecipeSubHowToMake sub : recipeMain.getRecipeSubHowToMakes()) {
			jdbcTemplate.update(INSERT_HOWTOMAKE, recipeMain.getRecipeName(), sub.getImagefile2().getFileName(),
					sub.getImagefile2().getFileData(), sub.getHowToMake());
		}
	}

	/**
	 * RecipeMainのRowMapper
	 * データベースの結果セットをRecipeMainオブジェクトにマッピング
	 * @return recipeMainList レシピリスト
	 */
	private RowMapper<RecipeMain> recipeMainRowMapper() throws Exception {
		return (rs, rowNum) -> {
			// データベースの列から値を取得
			String recipeName = rs.getString("recipeName");

			// 画像データをエンコードしてImageオブジェクトに設定
			Image image = createImageFromResultSet(rs, "fileName", "fileData");

			// レシピのコメントと人数を設定
			String comment = rs.getString("comment");
			String number = rs.getString("number");

			// RecipeMainオブジェクトを返す
			return new RecipeMain(recipeName, image, comment, number);
		};
	}

	/**
	 * RecipeSubMaterialのRowMapper
	 * データベースの結果セットをRecipeSubMaterialオブジェクトにマッピング
	 * @return recipeSubMaterialList 材料データリスト
	 */
	private RowMapper<RecipeSubMaterial> recipeSubMaterialRowMapper() throws Exception {
		return (rs, rowNum) -> {
			// 材料データをRecipeSubMaterialオブジェクトに設定して返す
			RecipeSubMaterial sub = new RecipeMain().new RecipeSubMaterial();
			sub.setMaterial(rs.getString("material"));
			sub.setQuantity(rs.getString("quantity"));
			return sub;
		};
	}

	/**
	 * RecipeSubHowToMakeのRowMapper
	 * データベースの結果セットをRecipeSubHowToMakeオブジェクトにマッピング
	 * @return recipeSubHowToMakeList 作り方データリスト
	 */
	private RowMapper<RecipeSubHowToMake> recipeSubHowToMakeRowMapper() throws Exception {
		return (rs, rowNum) -> {
			// 作り方データをRecipeSubHowToMakeオブジェクトに設定して返す
			RecipeSubHowToMake sub = new RecipeMain().new RecipeSubHowToMake();
			Image image = createImageFromResultSet(rs, "fileName2", "fileData2");
			sub.setImagefile2(image);
			sub.setHowToMake(rs.getString("howToMake"));
			return sub;
		};
	}

	/**
	* 画像データをエンコードしてImageオブジェクトに設定
	* @param re 現在行
	* @param fileName ファイル名称 
	* @param fileData ファイルデータ
	* @return image - Imageオブジェクト
	*/
	public Image createImageFromResultSet(ResultSet rs, String fileName, String fileData) throws SQLException {
		// RecipeMainクラスのインナークラスImageのインスタンスを作成
		RecipeMain.Image image = new RecipeMain().new Image();

		// ResultSetからfileName2の値を取得してセット
		image.setFileName(rs.getString(fileName));

		// ResultSetからfileData2の値を取得し、Base64エンコードしてセット
		image.setFileDataView(Base64.getEncoder().encodeToString(rs.getBytes(fileData)));

		return image;
	}

}
