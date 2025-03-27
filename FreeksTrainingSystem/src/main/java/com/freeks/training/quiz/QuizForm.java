package com.freeks.training.quiz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class QuizForm {
			
		// 設問1
		private String title1;
		// 問題文1
		private String body1;
		// 選択肢1
		private List<String> choiceList1 = new ArrayList<String>();
		// 選択した回答1
		private int selected1;
		
		
		// 設問1のゲッターとセッター
		public String getTitle1() {
			return this.title1;
		}
		
		public void setTitle1(String title1) {
			this.title1 = title1;
		}
		
		
		// 問題文1のゲッターとセッター
		public String getBody1() {
			return this.body1;
		}
		
		public void setBody1(String body1) {
			this.body1 = body1;
		}
		
		
		// 選択肢1のゲッターとセッター
		public List<String> getChoiceList1() {
			return this.choiceList1;
			
		}
		public void setChoiceList1(List<String> choiceList1) {
			this.choiceList1 = choiceList1;
		}
		
		
		// 回答1のゲッターとセッター
		public int getSelected1() {
			return this.selected1;
		
		}
		public void setSelected1(int selected1) {
			this.selected1 = selected1;
		}
		
		/*---------------------*/
		
		// 設問2
		private String title2;
		// 問題文2
		private String body2;
		// 選択肢2
		private List<String> choiceList2 = new ArrayList<String>();
		// 選択した回答2
		private int selected2;
		
		
		// 設問2のゲッターとセッター
		public String getTitle2() {
			return this.title2;
		}
		
		public void setTitle2(String title2) {
			this.title2 = title2;
		}
		
		
		// 問題文2のゲッターとセッター
		public String getBody2() {
			return this.body2;
		}
		
		public void setBody2(String body2) {
			this.body2 = body2;
		}
		
		
		// 選択肢2のゲッターとセッター
		public List<String> getChoiceList2() {
			return this.choiceList2;
			
		}
		public void setChoiceList2(List<String> choiceList2) {
			this.choiceList2 = choiceList2;
		}
		
		
		// 回答2のゲッターとセッター
		public int getSelected2() {
			return this.selected2;
		
		}
		public void setSelected2(int selected2) {
			this.selected2 = selected2;
		}
		
		/*---------------------*/
		
		// 設問3
		private String title3;
		// 問題文3
		private String body3;
		// 選択肢3
		private List<String> choiceList3 = new ArrayList<String>();
		// 選択した回答3
		private int selected3;
		

		// 設問3のゲッターとセッター
		public String getTitle3() {
			return this.title3;
		}
		
		public void setTitle3(String title3) {
			this.title3 = title3;
		}
		
		
		// 問題文3のゲッターとセッター
		public String getBody3() {
			return this.body3;
		}
		
		public void setBody3(String body3) {
			this.body3 = body3;
		}
		
		
		// 選択肢3のゲッターとセッター
		public List<String> getChoiceList3() {
			return this.choiceList3;
			
		}
		public void setChoiceList3(List<String> choiceList3) {
			this.choiceList3 = choiceList3;
		}
		
		
		// 回答3のゲッターとセッター
		public int getSelected3() {
			return this.selected3;
		
		}
		public void setSelected3(int selected3) {
			this.selected3 = selected3;
		}

		/*---------------------*/
		
		// 結果メッセージ
		private String resultMessage;
		
		// 結果メッセージのゲッターとセッター
		public String getResultMessage() {
			return this.resultMessage;
		}
		
		public void setResultMessage(String resultMessage) {
			this.resultMessage = resultMessage;
		}

}
