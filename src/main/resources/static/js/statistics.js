document.addEventListener('DOMContentLoaded', function () {
    // 12か月分の収支データの初期化
    const monthlyData = Array.from({length: 12}, () => 0);

    // treeMapMonthCount からデータを月ごとにセット
    Object.entries(treeMapMonthCount).forEach(([monthKey, value]) => {
        const monthIndex = parseInt(monthKey.split("-")[1], 10) - 1;
        if (monthIndex >= 0 && monthIndex < 12) {
            monthlyData[monthIndex] = value;
        }
    });

    // 平均収支のデータを12か月分設定
    const averageIncomeData = Array(12).fill(averageIncome);

    // グラフの設定
    const ctx = document.getElementById('lineChart').getContext('2d');
    const lineChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
            datasets: [
                {
                    label: '月ごとの収支',
                    data: monthlyData,
                    borderColor: 'rgba(226, 0, 113, 1)',
                    backgroundColor: 'rgba(255, 183, 229, 0.2)',
                    borderWidth: 1
                },
                {
                    label: '月ごとの収支の平均値',
                    data: averageIncomeData,
                    borderColor: 'rgba(75, 75, 255, 1)',
                    backgroundColor: 'rgba(200, 200, 255, 0.2)',
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    beginAtZero: true
                },
                y: {
                    beginAtZero: true
                }
            }
        }
    });
});