package com.example.incomeApp.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.incomeApp.model.Components;
import com.example.incomeApp.model.Income;
import com.example.incomeApp.model.Statistics;
import com.example.incomeApp.service.IncomeLogic;
import com.example.incomeApp.service.IncomeService;

@Controller("incomeMainController")
public class MainController {

	// 収支に関するロジックを処理するためのクラス
	private final IncomeLogic incomeLogic;

	// 年月をフォーマットするためのDateTimeFormatter
	private final DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

	// 年のみをフォーマットするためのDateTimeFormatter
	private final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");

	// ユーザーセッション情報を保持するクラス
	@Autowired
	private Components userSession;

	// コンストラクタでIncomeServiceを受け取り、IncomeLogicを初期化
	public MainController(IncomeService incomeService) {
		this.incomeLogic = new IncomeLogic(incomeService);
	}

	/**
	 * 収支を追加する処理
	 * @param id ユーザーID
	 * @param count 収支のカウント
	 * @param modalDate モーダルで入力された日付
	 * @param modalName モーダルで入力された名前
	 * @param modalCount モーダルで入力されたカウント
	 * @param model 画面に渡すデータを保持するためのモデル
	 * @return 収支ページのテンプレート
	 */
	@GetMapping("/add/{id}")
	public String addIncome(@PathVariable("id") int id,
			@RequestParam(value = "count", required = false, defaultValue = "0") Integer count,
			@RequestParam(value = "modalDate", required = false, defaultValue = "") String modalDate,
			@RequestParam(value = "modalName", required = false, defaultValue = "") String modalName,
			@RequestParam(value = "modalCount", required = false, defaultValue = "0") int modalCount,
			Model model) throws Exception {

		try {
			// セッションの日付を取得し、フォーマットを適用
			LocalDate localDate = getSessionAdjustedDate(id, 2, false);
			String formattedDate = localDate.format(yearMonthFormatter);

			// 収支を追加し、そのリストを取得
			List<Income> incomes = incomeLogic.addIncomeLogic(formattedDate,
					new Income(modalDate, modalName, modalCount));

			// モデルに追加した収支リストと日付を設定
			model.addAttribute("incomes", incomes);
			model.addAttribute("localDate", localDate);

		} catch (Exception e) {
			model.addAttribute("errorMessage", "収支の追加に失敗しました: " + e.getMessage());
			return "error/errorPage"; // エラーページに遷移
		}
		return "income/income";
	}

	/**
	 * 収支を取得する処理
	 * @param id ユーザーID
	 * @param model 画面に渡すデータを保持するためのモデル
	 * @return 収支ページのテンプレート
	 */
	@GetMapping("/getIncome")
	public String getIncome(@RequestParam("id") int id, Model model) throws Exception {

		try {
			// セッションの日付を取得し、フォーマットを適用
			LocalDate localDate = getSessionAdjustedDate(id, 2, false);
			String formattedDate = localDate.format(yearMonthFormatter);

			// 収支のリストを取得
			List<Income> incomes = incomeLogic.getIncomeListLogic(formattedDate);

			// モデルに収支リストと日付を追加
			model.addAttribute("incomes", incomes);
			model.addAttribute("localDate", localDate);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "収支の取得に失敗しました: " + e.getMessage());
			return "error/errorPage"; // エラーページに遷移
		}
		return "income/income";
	}

	/**
	 * 収支を設定する処理
	 * @param id ユーザーID
	 * @param count 収支カウント
	 * @param modalDate モーダルで入力された日付
	 * @param modalName モーダルで入力された名前
	 * @param modalCount モーダルで入力されたカウント
	 * @param incomeDate 収支の日付
	 * @param incomeName 収支の名前
	 * @param incomeCount 収支のカウント
	 * @param model 画面に渡すデータを保持するためのモデル
	 * @return 収支ページのテンプレート
	 */
	@GetMapping("/set/{id}/{count}/{incomeDate}/{incomeName}/{incomeCount}")
	public String setIncome(@PathVariable("id") int id,
			@PathVariable("count") int count,
			@RequestParam(value = "modalDate", required = false, defaultValue = "") String modalDate,
			@RequestParam(value = "modalName", required = false, defaultValue = "") String modalName,
			@RequestParam(value = "modalCount", required = false, defaultValue = "0") int modalCount,
			@PathVariable("incomeDate") String incomeDate,
			@PathVariable("incomeName") String incomeName,
			@PathVariable("incomeCount") int incomeCount,
			Model model) throws Exception {
		try {
			// セッションの日付を取得し、フォーマットを適用
			LocalDate localDate = getSessionAdjustedDate(id, 2, false);
			String formattedDate = localDate.format(yearMonthFormatter);

			// 収支のリストを設定
			List<Income> incomes = incomeLogic.setIncomeLogic(count, formattedDate, modalDate, modalName, modalCount,
					new Income(incomeDate, incomeName, incomeCount));

			// モデルに収支リストと日付を追加
			model.addAttribute("incomes", incomes);
			model.addAttribute("localDate", localDate);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "収支の設定に失敗しました: " + e.getMessage());
			return "error/errorPage"; // エラーページに遷移
		}
		return "income/income";
	}

	/**
	 * 収支を削除する処理
	 * @param id ユーザーID
	 * @param name 削除する収支の名前
	 * @param model 画面に渡すデータを保持するためのモデル
	 * @return 収支ページのテンプレート
	 */
	@PostMapping("/delete")
	public String deleteIncome(@RequestParam("id") int id,
			@RequestParam("incomeName") String name,
			Model model) throws Exception {
		try {
			// セッションの日付を取得し、フォーマットを適用
			LocalDate localDate = getSessionAdjustedDate(id, 2, false);
			String formattedDate = localDate.format(yearMonthFormatter);

			// 収支を削除し、そのリストを取得
			List<Income> incomes = incomeLogic.deleteIncomeLogic(formattedDate, new Income(formattedDate, name, 0));

			// モデルに収支リストと日付を追加
			model.addAttribute("incomes", incomes);
			model.addAttribute("localDate", localDate);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "収支の削除に失敗しました: " + e.getMessage());
			return "error/errorPage"; // エラーページに遷移
		}
		return "income/income";
	}

	/**
	 * 統計データを取得する処理
	 * @param id ユーザーID
	 * @param model 画面に渡すデータを保持するためのモデル
	 * @return 統計ページのテンプレート
	 */
	@GetMapping("/getStatistics")
	public String getStatistics(@RequestParam("id") int id, Model model) throws Exception {
		// セッションの日付を取得し、フォーマットを適用
		try {
			LocalDate localDate = getSessionAdjustedDate(id, 2, true);
			String formattedDate = localDate.format(yearFormatter);

			// 統計データを取得
			Statistics statistics = incomeLogic.getStatisticsLogic(new Income(formattedDate, null, 0));

			// モデルに統計データを追加
			model.addAttribute("allAsset", statistics.getAllAsset() + userSession.getStandardAsset());
			model.addAttribute("averageIncome", statistics.getAverageIncome());
			model.addAttribute("treeMapMonthCount", statistics.getTreeMapMonthCount());
			model.addAttribute("localDate", localDate);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "統計データの取得に失敗しました: " + e.getMessage());
			return "error/errorPage"; // エラーページに遷移
		}
		return "income/statistics";
	}

	/**
	 * セッションの日時を調整する処理
	 * @param id ユーザーID
	 * @param baseId 基準となるID
	 * @param adjustByYear 年単位で調整するかどうか
	 * @return 調整後の日付
	 */
	private LocalDate getSessionAdjustedDate(int id, int baseId, boolean adjustByYear) {
		LocalDate localDate = LocalDate.now();
		if (id != baseId) {
			// 年単位か月単位かに応じて日付を調整
			localDate = adjustByYear ? userSession.getDate().plusYears(id - baseId)
					: userSession.getDate().plusMonths(id - baseId);
		}
		// セッションの日付を更新
		userSession.setDate(localDate);
		return localDate;
	}
}
