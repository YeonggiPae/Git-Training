package com.freeks.training.quiz;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class QuizController {
	// 設問数に使用するループカウント定数
	static final int LOOP_COUNT = 3;
	// デフォルトの回答番号に使用する定数
	static final int DEFAULT_CHECKED = 1;
	
	// MEssageSourceライブラリを変数msgとして定義
	@Autowired
	private MessageSource msg;
	// QuizFormのインスタンス「form」生成
	@Autowired
	private QuizForm form;

	// get通信で「/quizQuestion」のリクエストが呼ばれると画面遷移して実行
	@GetMapping("/quizQuestion")   // ここの引数に「value＝」なくてもいいらしい
	public String question(Model model) {
		
		// message.propertiesから設問数分の「タイトル」「問題文」「選択肢」を取得して各変数に格納
		for(int i = 1; i <= LOOP_COUNT; i++) {
			String title = msg.getMessage("quiz.question.title"+i, null, null);
			String body = msg.getMessage("quiz.question.body"+i, null, null);
			String choice = msg.getMessage("quiz.question.choice"+i, null, null);
			
			// 選択肢choiceを改行記号「\n」ごとで区切ってリスト化
			List<String> choiceList = Arrays.asList(choice.split("\n"));
			

			// 設問番号に応じてそれぞれの値をインスタンス変数に格納
			switch(i){
				case 1:
					form.setTitle1(title);
					form.setBody1(body);
					form.setChoiceList1(choiceList);
					form.setSelected1(DEFAULT_CHECKED);   // ラジオボタンの初期値を渡す
					break;
					
				case 2:
					form.setTitle2(title);
					form.setBody2(body);
					form.setChoiceList2(choiceList);
					form.setSelected2(DEFAULT_CHECKED);
					break;
				
				case 3:
					form.setTitle3(title);
					form.setBody3(body);
					form.setChoiceList3(choiceList);
					form.setSelected3(DEFAULT_CHECKED);
					break;
			}
		}
		
		// JavaからThymeleaf(HTML)に値を渡す
		model.addAttribute("quizForm", form);
		// 戻り値はtemplate/quizフォルダ内のHTMLファイルの名前
		return "quiz/quizQuestion";   // 階層違うから「"quizQuestion"」だけじゃダメ
		
	}
	
	
	// post通信で「/quizResult」のリクエストが呼ばれると画面遷移して実行
	@PostMapping("/quizResult")
	public String result(QuizForm form, Model model) {	
		int correctCount = 0;
		
		// 各設問の正解を変数に格納
		for(int i = 1; i <= LOOP_COUNT; i++) {
			String correctStr = msg.getMessage("quiz.question.correct"+i, null, null);
			
			// 比較のためストリング型から整数へ変換
			int correct = Integer.parseInt(correctStr);
			
			// 各設問の正解と選択された回答が一致すれば正解数を増やす
			switch(i) {
				case 1:
					if(correct == form.getSelected1()) {
						correctCount++;
					}
					break;
				
				case 2:
					if(correct == form.getSelected2()) {
						correctCount++;
					}
					break;
				
				case 3:
					if(correct == form.getSelected3()) {
						correctCount++;
					}
					break;
				
			}

			// 正解数に応じたメッセージを格納
			switch(correctCount) {
				case 0:
					form.setResultMessage(msg.getMessage("quiz.msg.correct0", null, null));
					break;
					
				case 1:
					form.setResultMessage(msg.getMessage("quiz.msg.correct1", null, null));
					break;
				
				case 2:
					form.setResultMessage(msg.getMessage("quiz.msg.correct2", null, null));
					break;
					
				case 3:
					form.setResultMessage(msg.getMessage("quiz.msg.correct3", null, null));
					break;
			}
		}
			// JavaからThymeleaf(HTML)に値を渡す
			model.addAttribute("quizForm", form);
			return "quiz/quizResult";
	
	}
}