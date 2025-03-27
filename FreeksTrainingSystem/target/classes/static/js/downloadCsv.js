/**
 * 非同期でCSVをダウンロード
 */
	
document.getElementById("downloadCsvBtn").addEventListener("click", async function(){

	try{
		const response = await fetch("/api/csv/export", { method:"GET" });
		
		if(!response.ok){
			
			// 失敗メッセージ
			alert("CSVファイルのダウンロードに失敗しました")
			return;
		}
		
		const blob = await response.blob();
		const url = window.URL.createObjectURL(blob);
		const a = document.createElement("a");
		a.href = url;
		a.download = "itemList.csv";
		document.body.appendChild(a);
		a.click();
		document.body.removeChild(a);
		window.URL.revokeObjectURL(url);
		
		alert("CSVファイルをダウンロードしました");  // 成功メッセージ
			

	}catch(error){
		alert("CSVファイルのダウンロードに失敗しました");
	}
});

