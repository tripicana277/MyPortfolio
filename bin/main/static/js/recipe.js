// フォームのバリデーションを行う関数
function validateForm() {
	const requiredFields = ["recipeName", "fileName", "comment", "number"];  // 必須フィールドの名前を配列で保持
	for (let i = 0; i < requiredFields.length; i++) {
		// 各必須フィールドの最初の要素を取得
		const field = document.getElementsByName(requiredFields[i])[0];
		// フィールドの値が空でないことを確認（空白も含めて確認）
		if (field.value.trim() === "") {
			// 空の場合はエラーメッセージを表示し、バリデーション失敗
			alert(field.name + "を入力してください。");
			return false;
		}
	}

	// 材料フィールドのバリデーション
	const materials = document.getElementsByName("material[]");
	for (let i = 0; i < materials.length; i++) {
		// 材料が空でないことを確認
		if (materials[i].value.trim() === "") {
			alert("材料を入力してください。");
			return false;
		}
	}

	// 数量フィールドのバリデーション
	const quantities = document.getElementsByName("quantity[]");
	for (let i = 0; i < quantities.length; i++) {
		// 数量が空でないことを確認
		if (quantities[i].value.trim() === "") {
			alert("数量を入力してください。");
			return false;
		}
	}

	// 作り方の画像フィールドのバリデーション
	const fileNames = document.getElementsByName("fileName2[]");
	for (let i = 0; i < fileNames.length; i++) {
		// 画像フィールドが空でないことを確認
		if (fileNames[i].value.trim() === "") {
			alert("作り方の画像を入力してください。");
			return false;
		}
	}

	// 作り方フィールドのバリデーション
	const howToMakes = document.getElementsByName("howToMake[]");
	for (let i = 0; i < howToMakes.length; i++) {
		// 作り方が空でないことを確認
		if (howToMakes[i].value.trim() === "") {
			alert("作り方を入力してください。");
			return false;
		}
	}

	return true; // 全てのチェックが成功した場合、trueを返しフォームを送信
}

// テーブルに新しい行を追加するための汎用関数
function addRow(tableId, rowHtml) {
	const table = document.getElementById(tableId).getElementsByTagName('tbody')[0];  // テーブルの<tbody>要素を取得
	const newRow = table.insertRow();  // 新しい行（<tr>）を挿入
	newRow.innerHTML = rowHtml;  // 行にHTMLを挿入
}

// 材料の新しい行を追加する関数
function addMaterialRow() {
	const rowHtml = `
	<td class="no-wrap">・<input type="text" name="material[]" /></td>
	<td class="no-wrap"><input type="text" name="quantity[]" /></td>
	<td><input type="button" value="削除" onclick="deleteRow(this, 'materialTable')"></td>
	`;  // 材料と数量の入力フィールド、および削除ボタンを含むHTML
	addRow('materialTable', rowHtml);  // 'materialTable'に新しい行を追加
}

// 作り方の新しい行を追加する関数
function addHowToMakeRow() {
	const rowHtml = `
	<td><input type="file" name="fileName2[]" /></td>
	<td class="no-wrap">・<input type="text" name="howToMake[]" /></td>
	<td><input type="button" value="削除" onclick="deleteRow(this, 'howToMakeTable')"></td>
	`;  // 作り方の画像、作り方のテキスト入力フィールド、および削除ボタンを含むHTML
	addRow('howToMakeTable', rowHtml);  // 'howToMakeTable'に新しい行を追加
}

// 指定した行を削除する関数
function deleteRow(button, tableId) {
	const table = document.getElementById(tableId).getElementsByTagName('tbody')[0];  // 指定したテーブルの<tbody>を取得
	const rows = table.getElementsByTagName('tr');  // テーブルのすべての行を取得
	if (rows.length > 1) {
		// テーブルに2行以上ある場合は行を削除
		const row = button.closest('tr');  // ボタンが属する<tr>要素を取得
		row.parentNode.removeChild(row);  // 行を削除
	} else {
		// 行が1つしかない場合、削除できない警告を表示
		alert("これ以上削除できません。");
	}
}
