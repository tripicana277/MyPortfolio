// すべての「openModalBtn」クラスを持つボタンを取得して、クリックイベントを設定
document.querySelectorAll(".openModalBtn").forEach(button => {
    button.onclick = function () {
        // ボタンの「data-modal-type」属性からモーダルの種類を取得（date, name, count）
        const modalType = this.getAttribute("data-modal-type");
        // ボタンの「data-income-name」属性から収入名を取得
        let modalData = this.getAttribute("data-income-name");
        let actionPath;  // モーダルフォームの送信先URLを格納する変数
        let modal;  // モーダルのクローンを格納する変数

        // モーダルの種類に応じて処理を分岐
        switch (modalType) {
            case "date":
                actionPath = `/set/2/1/null/${encodeURIComponent(modalData)}/0`;
                modal = document.getElementById("modalTemplate1").cloneNode(true);
                break;
            case "name":
                actionPath = `/set/2/2/null/${encodeURIComponent(modalData)}/0`;
                modal = document.getElementById("modalTemplate2").cloneNode(true);
                break;
            case "count":
                actionPath = `/set/2/3/null/${encodeURIComponent(modalData)}/0`;
                modal = document.getElementById("modalTemplate3").cloneNode(true);
        }

        modal.id = "modal-" + new Date().getTime();
        document.body.appendChild(modal);

        modal.style.display = "block";
        modal.querySelector(".hiddenModalData").value = modalData;
        modal.querySelector(".modalForm").action = actionPath;

        // Validation: Ensure fields are filled before enabling submission
        const submitButton = modal.querySelector(".modalForm input[type='submit']");
        submitButton.disabled = true;

        // Function to validate the required input fields
        function validateForm() {
            let isValid = true;
            const requiredInputs = modal.querySelectorAll(".modalForm input[type='text'], .modalForm input[type='date'], .modalForm input[type='number']");
            requiredInputs.forEach(input => {
                if (input.value.trim() === "") {
                    isValid = false;
                }
            });
            return isValid;
        }

        // Attach event listeners to inputs to enable/disable the submit button based on validation
        const inputs = modal.querySelectorAll(".modalForm input");
        inputs.forEach(input => {
            input.addEventListener('input', () => {
                if (validateForm()) {
                    submitButton.disabled = false;
                } else {
                    submitButton.disabled = true;
                }
            });
        });

        modal.querySelector(".close").onclick = function () {
            document.body.removeChild(modal);
        };

        modal.onclick = function (event) {
            if (event.target === modal) {
                document.body.removeChild(modal);
            }
        };
    }
});
