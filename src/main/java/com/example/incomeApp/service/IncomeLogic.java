package com.example.incomeApp.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.incomeApp.entity.Income;
import com.example.incomeApp.entity.Statistics;

import lombok.RequiredArgsConstructor;

// ロジックを管理するクラス
@Service
@RequiredArgsConstructor
public class IncomeLogic {
	
	private final IncomeService incomeService;

	public List<Income> addIncomeLogic(String formattedDate, Income income) throws SQLException {
		incomeService.addOne(formattedDate, income);
		return incomeService.getAll(formattedDate);
	}

	public List<Income> getIncomeListLogic(String formattedDate) throws SQLException {
		return incomeService.getAll(formattedDate);
	}

	public Statistics getStatisticsLogic(Income income) throws SQLException {
		return incomeService.getStatisticsAll(income);
	}

	public List<Income> setIncomeLogic(int count, String formattedDate, String modalDate,
			String modalName, int modalCount, Income income) throws SQLException {
		incomeService.setOne(count, formattedDate, modalDate, modalName, modalCount, income);
		return incomeService.getAll(formattedDate);
	}

	public List<Income> deleteIncomeLogic(String formattedDate, Income income) throws SQLException {
		incomeService.deleteOne(formattedDate, income);
		return incomeService.getAll(formattedDate);
	}
}