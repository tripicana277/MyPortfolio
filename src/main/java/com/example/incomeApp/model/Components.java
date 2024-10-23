package com.example.incomeApp.model;

import java.time.LocalDate;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
* セッションの日時と基準資産
*/
@Component
@Data
// ユーザーごとのセッションでデータを保持
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Components {
	private LocalDate date;			// セッションの日時
	private int standardAsset = 0;	// 基準資産(現在未使用)
}