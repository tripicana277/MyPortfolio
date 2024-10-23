// .openModalBtnクラスを持つ全てのボタンにクリックイベントを設定
document.querySelectorAll(".openModalBtn").forEach(button => {
    button.onclick = function() {
        // クリックされたボタンのデータ属性からモーダルタイプとデータを取得
        const modalType = this.getAttribute("data-modal-type");
        let modalData = this.getAttribute("data-income-name");
        let actionPath;  // フォームのアクションパスを保持する変数
        let modal;  // モーダルテンプレートを保持する変数

        // モーダルの種類に応じてアクションパスとモーダルテンプレートを切り替える
        switch (modalType) {
            case "date":
                actionPath = `/set/2/1/null/${encodeURIComponent(modalData)}/0`;
                modal = document.getElementById("modalTemplate1").cloneNode(true);  // 日付入力用のモーダル
                break;
            case "name":
                actionPath = `/set/2/2/null/${encodeURIComponent(modalData)}/0`;
                modal = document.getElementById("modalTemplate2").cloneNode(true);  // 名前入力用のモーダル
                break;
            case "count":
                actionPath = `/set/2/3/null/${encodeURIComponent(modalData)}/0`;
                modal = document.getElementById("modalTemplate3").cloneNode(true);  // 数量入力用のモーダル
                break;
            case "add":
                actionPath = `/add/2?modalDate=${encodeURIComponent(modalDate)}&modalName=${encodeURIComponent(modalData)}`;  // 追加用のモーダル
                modal = document.getElementById("modalTemplate4").cloneNode(true);
                break;
        }

        // モーダルの一意なIDを設定してページに追加
        modal.id = "modal-" + new Date().getTime();
        document.body.appendChild(modal);

        // モーダルを表示
        modal.style.display = "block";
        // 隠しフィールドにモーダルデータをセット
        modal.querySelector(".hiddenModalData").value = modalData;
        // フォームのアクションパスを設定
        modal.querySelector(".modalForm").action = actionPath;

        // フォームの送信ボタンを無効化しておく
        const submitButton = modal.querySelector(".modalForm input[type='submit']");
        submitButton.disabled = true;

        // フォームの入力内容を検証する関数
        function validateForm() {
            let isValid = true;
            const requiredInputs = modal.querySelectorAll(".modalForm input[type='text'], .modalForm input[type='date'], .modalForm input[type='number']");
            // 全ての必須入力フィールドが埋められているかチェック
            requiredInputs.forEach(input => {
                if (input.value.trim() === "") {
                    isValid = false;
                }
            });
            return isValid;
        }

        // 入力フィールドのイベントリスナーを設定
        const inputs = modal.querySelectorAll(".modalForm input");
        inputs.forEach(input => {
            input.addEventListener('input', () => {
                // フォームが有効な場合、送信ボタンを有効化
                if (validateForm()) {
                    submitButton.disabled = false;
                } else {
                    submitButton.disabled = true;
                }
            });
        });

        // モーダルを閉じるボタンのイベントリスナー
        modal.querySelector(".close").onclick = function() {
            document.body.removeChild(modal);  // モーダルをDOMから削除
        };

        // モーダルの外側をクリックすると閉じる機能
        modal.onclick = function(event) {
            if (event.target === modal) {
                document.body.removeChild(modal);  // モーダルを閉じる
            }
        };
    }
});
