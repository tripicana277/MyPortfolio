package com.example.incomeApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.incomeApp.model.Income;
import com.example.incomeApp.model.Statistics;

@Service
@Transactional // トランザクションを管理して、データ整合性を確保
public class IncomeService {

    // JdbcTemplateを使用してデータベースとのやり取りを行う
	private final JdbcTemplate jdbcTemplate;

    // コンストラクタでJdbcTemplateを受け取る
	public IncomeService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// SQLクエリ定数（各操作に使用されるSQL文）
	private static final String SELECT_INCOME_BY_MONTH = "SELECT * FROM income WHERE DATE_FORMAT(income_date, '%Y-%m') = ? ORDER BY income_date ASC";
	private static final String SELECT_INCOME_BY_YEAR = "SELECT * FROM income WHERE DATE_FORMAT(income_date, '%Y') = ? ORDER BY income_date ASC";
	private static final String SELECT_ALL_INCOME = "SELECT * FROM income ORDER BY income_date ASC";
	private static final String INSERT_INCOME = "INSERT INTO income (income_date, income_name, income_count) VALUES (?, ?, ?)";
	private static final String UPDATE_INCOME_DATE = "UPDATE income SET income_date = ? WHERE income_name = ?";
	private static final String UPDATE_INCOME_NAME = "UPDATE income SET income_name = ? WHERE income_name = ?";
	private static final String UPDATE_INCOME_COUNT = "UPDATE income SET income_count = ? WHERE income_name = ?";
	private static final String DELETE_INCOME_BY_NAME = "DELETE FROM income WHERE income_name = ?";

	/**
	 * 収支を追加するメソッド
	 * 
	 * @param formattedDate フォーマットされた日付
	 * @param income 追加する収支オブジェクト
	 */
	public void addOne(String formattedDate, Income income) {
		// 収支の名前が重複していない場合のみ追加
		if (!isDuplicateIncome(income.getIncomeName())) {
			jdbcTemplate.update(INSERT_INCOME, income.getIncomeDate(), income.getIncomeName(), income.getIncomeCount());
		}
	}
	
	/**
	 * 指定された月の収支リストを取得
	 * 
	 * @param formattedDate フォーマットされた日付
	 * @return 収支リスト
	 */
	public List<Income> getAll(String formattedDate) {
		// 指定された月の収支データを取得
		return queryIncome(SELECT_INCOME_BY_MONTH, formattedDate);
	}

	/**
	 * 収支データを設定（更新）するメソッド
	 * 
	 * @param count 更新対象のフィールド（1:日付、2:名前、その他:金額）
	 * @param formattedDate フォーマットされた日付
	 * @param modalDate モーダルで入力された日付
	 * @param modalName モーダルで入力された名前
	 * @param modalCount モーダルで入力された収支の金額
	 * @param income 更新対象の収支オブジェクト
	 */
	public void setOne(int count, String formattedDate, String modalDate, String modalName, int modalCount,
			Income income) {
		// 収支の名前が重複していない場合のみ更新
		if (!isDuplicateIncome(modalName)) {
			// countに応じて更新するフィールドを切り替え
			switch (count) {
			case 1:
				jdbcTemplate.update(UPDATE_INCOME_DATE, modalDate, income.getIncomeName());
				break;
			case 2:
				jdbcTemplate.update(UPDATE_INCOME_NAME, modalName, income.getIncomeName());
				break;
			default:
				jdbcTemplate.update(UPDATE_INCOME_COUNT, modalCount, income.getIncomeName());
				break;
			}
		}
	}

	/**
	 * 収支データを削除するメソッド
	 * 
	 * @param formattedDate フォーマットされた日付
	 * @param income 削除する収支オブジェクト
	 */
	public void deleteOne(String formattedDate, Income income) {
		// 収支データを名前に基づいて削除
		jdbcTemplate.update(DELETE_INCOME_BY_NAME, income.getIncomeName());
	}

	/**
	 * 指定された年と全ての収支データから統計データを取得
	 * 
	 * @param income 収支オブジェクト（統計データ計算用）
	 * @return 統計データオブジェクト
	 */
	public Statistics getStatisticsAll(Income income) {
		// 指定された年の収支データを取得
		List<Income> incomesByYear = queryIncome(SELECT_INCOME_BY_YEAR, income.getIncomeDate());
		// 全ての収支データを取得
		List<Income> allIncomes = queryIncome(SELECT_ALL_INCOME);

		// 月毎の収入合計を取得
		Map<String, Integer> MonthlyIncomeTotals = getMonthlyIncomeTotals(incomesByYear);

		// 統計データオブジェクトを作成して返す
		return new Statistics(
				getIncomeTotal(allIncomes), // 全収支の合計
				(int) Math.round(getIncomeAverage(MonthlyIncomeTotals)), // 平均収入(それを四捨五入して整数に変換)
				MonthlyIncomeTotals // 月ごとの収支合計
		);
	}

	/**
	 * 月毎の収入合計を計算する
	 * 
	 * @param incomeList 収支データリスト
	 * @return 月ごとの収入合計を示すMap
	 */
	public Map<String, Integer> getMonthlyIncomeTotals(List<Income> incomeList) {
		return incomeList.stream()
				.collect(Collectors.groupingBy(
						income -> income.getIncomeDate().substring(0, 7), // 年月ごとにグループ化
						Collectors.summingInt(Income::getIncomeCount))); // 各月の収入を合計
	}

	/**
	 * 全収支の合計を計算する
	 * 
	 * @param incomeList 収支データリスト
	 * @return 合計値
	 */
	public Integer getIncomeTotal(List<Income> incomeList) {
		// 各収支のカウントを合計
		return incomeList.stream()
				.collect(Collectors.summingInt(Income::getIncomeCount));
	}

	/**
	 * 月毎の収入の平均値を計算
	 * 
	 * @param MonthlyIncomeTotals 月ごとの収支合計
	 * @return 平均値
	 */
	public Double getIncomeAverage(Map<String, Integer> MonthlyIncomeTotals) {
		// 月毎の収支合計の平均を計算
		return MonthlyIncomeTotals.values().stream()
				.mapToInt(Integer::intValue)
				.average()
				.orElse(0.0);// 計算できない場合デフォルト値0を返す
	}

	/**
	 * SQLクエリを実行し、結果を収支リストとして返す
	 * 
	 * @param sql 実行するSQL文
	 * @param params SQLのパラメータ
	 * @return 収支リスト
	 */
	private List<Income> queryIncome(String sql, Object... params) {
	    // SQLを実行して、リストに変換された結果を取得
	    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
	    
	    // リストを収支オブジェクトに変換していく
	    List<Income> incomes = new ArrayList<>();
	    
	    for (Map<String, Object> row : rows) {
	        Income income = new Income();
	        income.setIncomeDate((String) row.get("INCOME_DATE"));
	        income.setIncomeName((String) row.get("INCOME_NAME"));
	        income.setIncomeCount((Integer) row.get("INCOME_COUNT"));
	        // 他のフィールドも必要に応じて設定
	        incomes.add(income);
	    }
	    
	    return incomes;
	}

	/**
	 * 収支が重複しているかどうかをチェック
	 * 
	 * @param incomeName 収支の名前
	 * @return 重複していればtrue、そうでなければfalse
	 */
	private boolean isDuplicateIncome(String incomeName) {
		// 全収支を取得し、重複する名前が存在するかチェック
		List<Income> incomes = queryIncome(SELECT_ALL_INCOME);
		return incomes.stream().anyMatch(income -> income.getIncomeName().equals(incomeName));
	}
}
