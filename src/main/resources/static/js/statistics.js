// ページのDOMが完全に読み込まれた後に実行されるイベントリスナー
document.addEventListener('DOMContentLoaded', function () {
    // 12か月分の収支データを初期化（各月の収支を0に設定）
    const monthlyData = Array.from({length: 12}, () => 0);

    // treeMapMonthCountオブジェクトのエントリをループ処理
    // 各月のキー（'YYYY-MM'形式）から月番号を取得し、対応するインデックスに収支データをセット
    Object.entries(treeMapMonthCount).forEach(([monthKey, value]) => {
        // 月番号を取得し、1を引いてインデックスに変換（0から始まるため）
        const monthIndex = parseInt(monthKey.split("-")[1], 10) - 1;
        // インデックスが0から11の範囲内であることを確認（12か月以内であるかチェック）
		if (0 <= monthIndex && monthIndex < 12) {
		    monthlyData[monthIndex] = value;  // 該当月の収支データをセット
		}
    });

    // 平均収支のデータを12か月分作成（全て同じ平均値）
    const averageIncomeData = Array(12).fill(averageIncome);

    // グラフを描画するためのコンテキストを取得（'lineChart'要素を取得し、その2Dコンテキストを使用）
    const ctx = document.getElementById('lineChart').getContext('2d');
    
    // チャートの作成
    const lineChart = new Chart(ctx, {
        type: 'line',  // 線グラフを指定
        data: {
            // 各月のラベルを設定
            labels: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
            datasets: [
                {
                    label: '月ごとの収支',  // データセットのラベル
                    data: monthlyData,  // 12か月の収支データ
                    borderColor: 'rgba(226, 0, 113, 1)',  // 線の色
                    backgroundColor: 'rgba(255, 183, 229, 0.2)',  // 塗りつぶしの色
                    borderWidth: 1  // 線の太さ
                },
                {
                    label: '月ごとの収支の平均値',  // 平均収支のラベル
                    data: averageIncomeData,  // 平均収支のデータ（12か月同じ値）
                    borderColor: 'rgba(75, 75, 255, 1)',  // 線の色
                    backgroundColor: 'rgba(200, 200, 255, 0.2)',  // 塗りつぶしの色
                    borderWidth: 1  // 線の太さ
                }
            ]
        },
        options: {
            responsive: true,  // レスポンシブ対応（画面サイズに合わせてグラフを調整）
            scales: {
                x: {
                    beginAtZero: true  // X軸の値を0から始める設定
                },
                y: {
                    beginAtZero: true  // Y軸の値を0から始める設定
                }
            }
        }
    });
});
