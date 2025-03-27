package com.freeks.training.stockSystem.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freeks.training.stockSystem.service.ItemService;

@RestController
@RequestMapping("/api/csv")
public class CsvDownloadAPI {

    @Autowired
    private ItemService itemService;

    @GetMapping("/export")
    public void exportCsv(HttpServletResponse response) {
    	
    	// CSV出力処理後、マップのmessageキーに成否に応じたメッセージを紐づけ
        try {
        	
    		// CSVファイル名を設定
    		String filename = "itemList.csv";
    		response.setContentType("text/csv; charset=Windows-31J");
    		response.setCharacterEncoding("Windows-31J");
    		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        	
        	// CSV生成
        	boolean isSuccess = itemService.generateCsv(response);
        	
        	if(isSuccess) {
        		// ダウンロード成功
                response.setStatus(HttpServletResponse.SC_OK);
                
        	}else {
            	// 商品リストがない時
        		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            
            // レスポンスを即座に送信
            response.flushBuffer();
            
        } catch(IOException e){
        	// "システムエラー：CSV出力中にエラーが発生しました"
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
             
        }
    }
}