package com.example.incomeApp.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.incomeApp.model.Income;
import com.example.incomeApp.model.Statistics;

import lombok.RequiredArgsConstructor;

// 収支に関するビジネスロジックを管理するクラス
@Service
@RequiredArgsConstructor
public class IncomeLogic {

    // IncomeServiceを利用してデータベース操作を行う
    private final IncomeService incomeService;

    /**
     * 収支を追加するロジック
     * 
     * @param formattedDate フォーマットされた日付
     * @param income 追加する収支データ
     * @return 指定された月の収支リスト
     * @throws SQLException データベース操作でエラーが発生した場合
     */
    public List<Income> addIncomeLogic(String formattedDate, Income income) throws Exception {
        // 指定された日付と収支データを追加
        incomeService.addOne(formattedDate, income);
        // 追加後、全ての収支データを取得して返す
        return incomeService.getAll(formattedDate);
    }

    /**
     * 収支データを取得するロジック
     * 
     * @param formattedDate フォーマットされた日付
     * @return 指定された月の収支リスト
     * @throws SQLException データベース操作でエラーが発生した場合
     */
    public List<Income> getIncomeListLogic(String formattedDate) throws Exception {
        // 指定された日付の収支データを取得して返す
        return incomeService.getAll(formattedDate);
    }

    /**
     * 収支データを設定するロジック
     * 
     * @param count 設定する収支の数
     * @param formattedDate フォーマットされた日付
     * @param modalDate モーダルから入力された日付
     * @param modalName モーダルから入力された名前
     * @param modalCount モーダルから入力された収支の数
     * @param income 収支データ
     * @return 設定後の収支リスト
     * @throws SQLException データベース操作でエラーが発生した場合
     */
    public List<Income> setIncomeLogic(int count, String formattedDate, String modalDate,
            String modalName, int modalCount, Income income) throws Exception {
        // 指定された収支データを設定
        incomeService.setOne(count, formattedDate, modalDate, modalName, modalCount, income);
        // 設定後、全ての収支データを取得して返す
        return incomeService.getAll(formattedDate);
    }

    /**
     * 収支データを削除するロジック
     * 
     * @param formattedDate フォーマットされた日付
     * @param income 削除する収支データ
     * @return 削除後の収支リスト
     * @throws SQLException データベース操作でエラーが発生した場合
     */
    public List<Income> deleteIncomeLogic(String formattedDate, Income income) throws Exception {
        // 指定された収支データを削除
        incomeService.deleteOne(formattedDate, income);
        // 削除後、全ての収支データを取得して返す
        return incomeService.getAll(formattedDate);
    }

    /**
     * 統計データを取得するロジック
     * 
     * @param income 統計に基づく収支データ
     * @return 統計情報
     * @throws SQLException データベース操作でエラーが発生した場合
     */
    public Statistics getStatisticsLogic(Income income) throws Exception {
        // 指定された収支データに基づいて統計データを取得
        return incomeService.getStatisticsAll(income);
    }
}
