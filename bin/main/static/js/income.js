document.querySelectorAll(".openModalBtn").forEach(button => {
	button.onclick = function() {
		const modalType = this.getAttribute("data-modal-type");
		let modalData = this.getAttribute("data-income-name");
		let actionPath;
		let modal;

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
				break;  // ここに break を追加
			case "add":
				actionPath = `/add/2?modalDate=${encodeURIComponent(modalDate)}&modalName=${encodeURIComponent(modalData)}`; // countは0または任意の値をURLに追加
				modal = document.getElementById("modalTemplate4").cloneNode(true);
				break;
		}

		modal.id = "modal-" + new Date().getTime();
		document.body.appendChild(modal);

		modal.style.display = "block";
		modal.querySelector(".hiddenModalData").value = modalData;
		modal.querySelector(".modalForm").action = actionPath;

		const submitButton = modal.querySelector(".modalForm input[type='submit']");
		submitButton.disabled = true;

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

		modal.querySelector(".close").onclick = function() {
			document.body.removeChild(modal);
		};

		modal.onclick = function(event) {
			if (event.target === modal) {
				document.body.removeChild(modal);
			}
		};
	}
});
