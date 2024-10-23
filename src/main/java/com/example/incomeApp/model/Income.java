package com.example.incomeApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 収支データ
*/
@Entity // データベースのテーブルに対応するJavaクラス
@Data // Lombokを使用して、getter/setterなどを自動生成
@AllArgsConstructor // 全フィールドを初期化するコンストラクタを自動生成
@NoArgsConstructor // 引数なしのデフォルトコンストラクタを自動生成
public class Income {
	private String incomeDate; // 日付
	@Id
	private String incomeName; // 名称
	private int incomeCount; // 金額
}
