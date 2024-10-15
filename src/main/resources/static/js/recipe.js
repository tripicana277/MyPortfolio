function validateForm() {
	const requiredFields = ["recipeName", "fileName", "comment", "number"];
	for (let i = 0; i < requiredFields.length; i++) {
		// 入力データの最初の値を取得
		const field = document.getElementsByName(requiredFields[i])[0];
		// フィールドが空でないことを確認(文字列のスペースを取り除き比較)
		if (field.value.trim() === "") {
			// フィールドが空の場合エラーメッセージを表示(型変換無しで比較)
			alert(field.name + "を入力してください。");
			return false;
		}
	}

	const materials = document.getElementsByName("material[]");
	for (let i = 0; i < materials.length; i++) {
		// 材料フィールドが空でないことを確認
		if (materials[i].value.trim() === "") {
			// 材料フィールドが空の場合エラーメッセージを表示(型変換無しで比較)
			alert("材料を入力してください。");
			return false;
		}
	}

	const quantities = document.getElementsByName("quantity[]");
	for (let i = 0; i < quantities.length; i++) {
		// 数量フィールドが空でないことを確認
		if (quantities[i].value.trim() === "") {
			// 数量フィールドが空の場合エラーメッセージを表示(型変換無しで比較)
			alert("数量を入力してください。");
			return false;
		}
	}

	const fileNames = document.getElementsByName("fileName2[]");
	for (let i = 0; i < fileNames.length; i++) {
		// 作り方の画像フィールドが空でないことを確認
		if (fileNames[i].value.trim() === "") {
			// 作り方の画像フィールドが空の場合エラーメッセージを表示(型変換無しで比較)
			alert("作り方の画像を入力してください。");
			return false;
		}
	}

	const howToMakes = document.getElementsByName("howToMake[]");
	for (let i = 0; i < howToMakes.length; i++) {
		// 作り方フィールドが空でないことを確認
		if (howToMakes[i].value.trim() === "") {
			// 作り方フィールドが空の場合エラーメッセージを表示(型変換無しで比較)
			alert("作り方を入力してください。");
			return false;
		}
	}
	return true; // すべてのチェックが成功した場合にフォームを送信
}

// テーブルに新しい行を追加する関数
function addRow(tableId, rowHtml) {
	const table = document.getElementById(tableId).getElementsByTagName('tbody')[0];
	const newRow = table.insertRow();// <tr>を挿入
	newRow.innerHTML = rowHtml;
}

// 材料の新しい行を追加する関数
function addMaterialRow() {
	const rowHtml = `
	<td class="no-wrap">・<input type="text" name="material[]" /></td>
	<td class="no-wrap"><input type="text" name="quantity[]" /></td>
	<td><input type="button" value="削除" onclick="deleteRow(this, 'materialTable')"></td>
`;
	addRow('materialTable', rowHtml);
}

// 作り方の新しい行を追加する関数
function addHowToMakeRow() {
	const rowHtml = `
	<td><input type="file" name="fileName2[]" /></td>
	<td class="no-wrap">・<input type="text" name="howToMake[]" /></td>
	<td><input type="button" value="削除" onclick="deleteRow(this, 'howToMakeTable')"></td>
`;
	addRow('howToMakeTable', rowHtml);
}

// 指定した行を削除する関数
function deleteRow(button, tableId) {
	const table = document.getElementById(tableId).getElementsByTagName('tbody')[0];
	const rows = table.getElementsByTagName('tr');
	if (rows.length > 1) {
		// 行が2行の以上の場合は削除
		const row = button.closest('tr');
		row.parentNode.removeChild(row);
	} else {
		alert("これ以上削除できません。"); // 行が1つしかない場合の警告メッセージ
	}
}