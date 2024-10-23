package com.example.incomeApp.model;

import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
* 統計データ
*/
@Data
@AllArgsConstructor
public class Statistics {

//	@Id
	private int allAsset;		// 総資産
	private int averageIncome;	// 平均収支
	private Map<String, Integer> treeMapMonthCount = new TreeMap<>(); // 月額リスト
}