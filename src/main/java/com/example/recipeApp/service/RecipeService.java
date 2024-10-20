package com.example.recipeApp.service;

import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.recipeApp.entity.RecipeMain;
import com.example.recipeApp.entity.RecipeMain.Image;
import com.example.recipeApp.entity.RecipeMain.RecipeSubHowToMake;
import com.example.recipeApp.entity.RecipeMain.RecipeSubMaterial;

@Repository
public class RecipeService {

	// SQLクエリ
	private static final String SELECT_RECIPEMAIN = "SELECT * FROM RECIPEMAIN";
	private static final String SELECT_RECIPEMAIN_BY_NAME = "SELECT * FROM RECIPEMAIN WHERE RECIPENAME = ?";
	private static final String SELECT_MATERIAL_BY_NAME = "SELECT * FROM MATERIAL WHERE RECIPENAME = ?";
	private static final String SELECT_HOWTOMAKE_BY_NAME = "SELECT * FROM HOWTOMAKE WHERE RECIPENAME = ?";
	private static final String INSERT_RECIPEMAIN = "INSERT INTO RECIPEMAIN (RECIPENAME, FILENAME, FILEDATA, COMMENT, NUMBER) VALUES (?, ?, ?, ?, ?)";
	private static final String INSERT_MATERIAL = "INSERT INTO MATERIAL (RECIPENAME, MATERIAL, QUANTITY) VALUES (?, ?, ?)";
	private static final String INSERT_HOWTOMAKE = "INSERT INTO HOWTOMAKE (RECIPENAME, FILENAME2, FILEDATA2, HOWTOMAKE) VALUES (?, ?, ?, ?)";
	private static final String DELETE_RECIPEMAIN_BY_NAME = "DELETE FROM RECIPEMAIN WHERE RECIPENAME = ?";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);

	// 新しいレシピを挿入するメソッド
	@Transactional
	public String addOne(RecipeMain recipeMain) {

		logger.debug("TestTakeda");

		// レシピメイン情報を挿入
		jdbcTemplate.update(INSERT_RECIPEMAIN, recipeMain.getRecipeName(), recipeMain.getImagefile().getFileName(),
				recipeMain.getImagefile().getFileData(), recipeMain.getComment(), recipeMain.getNumber());

		logger.debug("TestTakeda2");

		// 材料と作り方を挿入
		insertMaterials(recipeMain);
		insertHowToMakes(recipeMain);

		logger.debug("TestTakeda3");

		return recipeMain.getRecipeName();
	}

	// レシピを名前で取得するメソッド
	public RecipeMain getOne(String name) {

		RecipeMain recipeMain = jdbcTemplate.queryForObject(SELECT_RECIPEMAIN_BY_NAME, new Object[] { name },
				recipeMainRowMapper());

		if (recipeMain != null) {
			recipeMain.setRecipeSubMaterials(fetchMaterials(name));
			recipeMain.setRecipeSubHowToMakes(fetchHowToMakes(name));
		}
		return recipeMain;
	}

	// 全てのレシピを取得するメソッド
	public List<RecipeMain> getAll() {
		return jdbcTemplate.query(SELECT_RECIPEMAIN, recipeMainRowMapper());
	}

	// レシピを削除するメソッド
	public void deleteOne(String name) {
		jdbcTemplate.update(DELETE_RECIPEMAIN_BY_NAME, name);
	}

	// 材料リストを取得するメソッド
	private List<RecipeSubMaterial> fetchMaterials(String name) {
		return jdbcTemplate.query(SELECT_MATERIAL_BY_NAME, new Object[] { name }, recipeSubMaterialRowMapper());
	}

	// 作り方リストを取得するメソッド
	private List<RecipeSubHowToMake> fetchHowToMakes(String name) {
		return jdbcTemplate.query(SELECT_HOWTOMAKE_BY_NAME, new Object[] { name }, recipeSubHowToMakeRowMapper());
	}

	// 材料を挿入するメソッド
	private void insertMaterials(RecipeMain recipeMain) {
		for (RecipeSubMaterial sub : recipeMain.getRecipeSubMaterials()) {
			jdbcTemplate.update(INSERT_MATERIAL, recipeMain.getRecipeName(), sub.getMaterial(), sub.getQuantity());
		}
	}

	// 作り方を挿入するメソッド
	private void insertHowToMakes(RecipeMain recipeMain) {
		for (RecipeSubHowToMake sub : recipeMain.getRecipeSubHowToMakes()) {
			jdbcTemplate.update(INSERT_HOWTOMAKE, recipeMain.getRecipeName(), sub.getImagefile2().getFileName(),
					sub.getImagefile2().getFileData(), sub.getHowToMake());
		}
	}

	// RecipeMainのRowMapper
	private RowMapper<RecipeMain> recipeMainRowMapper() {
		return (rs, rowNum) -> {
			String recipeName = rs.getString("recipeName");

			Image image = new RecipeMain().new Image();
			image.setFileName(rs.getString("fileName"));
			image.setFileDataView(Base64.getEncoder().encodeToString(rs.getBytes("fileData")));

			String comment = rs.getString("comment");
			String number = rs.getString("number");
			return new RecipeMain(recipeName, image, comment, number);
		};
	}

	// RecipeSubMaterialのRowMapper
	private RowMapper<RecipeSubMaterial> recipeSubMaterialRowMapper() {
		return (rs, rowNum) -> {
			RecipeSubMaterial sub = new RecipeMain().new RecipeSubMaterial();
			sub.setMaterial(rs.getString("material"));
			sub.setQuantity(rs.getString("quantity"));
			return sub;
		};
	}

	// RecipeSubHowToMakeのRowMapper
	private RowMapper<RecipeSubHowToMake> recipeSubHowToMakeRowMapper() {
		return (rs, rowNum) -> {
			RecipeSubHowToMake sub = new RecipeMain().new RecipeSubHowToMake();
			Image image = new RecipeMain().new Image();
			image.setFileName(rs.getString("fileName2"));
			image.setFileDataView(Base64.getEncoder().encodeToString(rs.getBytes("fileData2")));
			sub.setImagefile2(image);
			sub.setHowToMake(rs.getString("howToMake"));
			return sub;
		};
	}

//	private static byte[] convertToJpeg(byte[] originalImageData) throws IOException {
//		// 画像データをByteArrayInputStreamに変換
//		ByteArrayInputStream bais = new ByteArrayInputStream(originalImageData);
//
//		// 画像を読み込む (元の形式がJPEG以外の場合)
//		BufferedImage originalImage = ImageIO.read(bais);
//		if (originalImage == null) {
//			throw new IOException("無効な画像フォーマットです。");
//		}
//
//		// JPEG形式に変換するためにByteArrayOutputStreamを使用
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(originalImage, "jpeg", baos);
//
//		// JPEG形式のバイトデータを返す
//		return baos.toByteArray();
//	}
}
