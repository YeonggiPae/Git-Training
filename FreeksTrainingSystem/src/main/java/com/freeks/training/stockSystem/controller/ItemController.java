package com.freeks.training.stockSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.freeks.training.stockSystem.form.ItemInfoForm;
import com.freeks.training.stockSystem.form.ItemListForm;
import com.freeks.training.stockSystem.form.ItemStockForm;
import com.freeks.training.stockSystem.service.ItemService;
import com.freeks.training.stockSystem.session.ItemSession;


@Controller
public class ItemController {
	
	@Autowired
	private MessageSource msg;
	
	@Autowired
	private ItemService itemService;	
	
	@Autowired
	private ItemSession itemSession;
	
	// ログインユーザー情報(今回は固定)
	public static final String LOGINUSER = "freeks";
	
	
	// 環境依存しない改行コード
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	/**
	 *  商品情報一覧取得メソッド
	 * @param model 商品リスト
	 * @return 商品一覧画面のエンドポイントを返す
	 */
	@GetMapping("/itemList")
	public String itemList(Model model) {
		// 商品情報一覧を取得
			
		ItemListForm itemList = itemService.getItemListAll();
		
		// itemListのHTMLに「itemListForm」として変数itemListを渡す
		model.addAttribute("itemListForm", itemList);
		
		// 商品一覧画面へ遷移
		return "stockSystem/itemList";
		
	}

	
	/**
	 *  商品情報登録画面への遷移処理
	 * @param model
	 * @return 商品登録画面へのエンドポイントを返す
	 */
	@GetMapping("/itemInsert")
	public String itemInsert(Model model) {
		
		//// リダイレクトチェック ////
		if (model.containsAttribute("itemInfoForm")) {

			return "stockSystem/itemInsert";

		}

		ItemInfoForm itemInfoForm = new ItemInfoForm();

		// セッションにItemInfoFormが存在チェック
		if (itemSession.getItemInfoForm() != null) {

			//商品セッションから商品情報formを取得
			itemInfoForm = itemSession.getItemInfoForm();

		}

		model.addAttribute("itemInfoForm", itemInfoForm);
		return "stockSystem/itemInsert";
		
	}
	
	/**
	 * 商品情報登録処理
	 * @param itemInfoForm 登録する商品名を受け取る(重複チェック用)
	 * @param model 登録する商品情報を渡す
	 * @return 商品一覧画面へのエンドポイントを返す
	 */	
	@PostMapping("/itemCreate")
	public String itemCreate(@ModelAttribute @Validated ItemInfoForm itemInfoForm, BindingResult result, RedirectAttributes re, Model model){
		
		//// バリデーションチェック ////
		// チェックに引っかかった場合、登録画面へリダイレクトしてからメッセージを表示
		// BindingResult.MODEL_KEY_PREFIXを使うと、受け取ったフォーム情報を保持したままビューに再度表示できる
		if(result.hasErrors()) {
			itemInfoForm.setErrMsg(msg.getMessage("Valid.FailInputValue", null, null));
			re.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "itemInfoForm", result);
			re.addFlashAttribute("itemInfoForm", itemInfoForm);
			return "stockSystem/itemInsert";
		}
		
		//// 重複チェック ////
		// バリデーションチェック通ってから重複チェック回したほうが軽そうなのでif文分けた
		// 登録する商品名を受け取ってString型変数に代入
		String itemName = itemInfoForm.getItemName();
		
		// 重複チェックの結果を受け取る
		String duplicateCheckResult = itemService.checkDuplicateItemName(itemName);
		
		// チェック結果受け取り変数がnull以外なら登録失敗。商品登録画面へリダイレクトしてからメッセージ表示
		if(duplicateCheckResult != null) {
			// 発生したエラーメッセージをセット
			itemInfoForm.setErrMsg(duplicateCheckResult);
			re.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "itemInfoForm", result);
			re.addFlashAttribute("itemInfoForm", itemInfoForm);
			return "redirect:/itemInsert";
		}
		
		// 商品登録後、在庫登録で例外発生時にロールバックさせるため、例外をキャッチできるようにする
		try {
		//// 各チェック完了後 ////
		// 商品情報および在庫情報登録
		ItemListForm itemList = itemService.createItem(itemInfoForm, LOGINUSER);
		
		// セッションの商品情報をクリア
		itemSession.clearItemInfoForm();
		
		model.addAttribute("itemListForm", itemList);
		
		// 商品一覧画面へリダイレクト
		return "stockSystem/itemList";
		
		}catch(RuntimeException e) {
			// ロールバック後、エラーメッセージを表示
			ItemListForm itemList = new ItemListForm();
			itemList.setErrMsg(e.getMessage());
			model.addAttribute("itemListForm", itemList);
			
			// 商品一覧画面へリダイレクト
			return "stockSystem/itemList";
		}
	}
	
	
	/**
	 * 商品情報詳細への遷移処理
	 * @param itemId
	 * @param model
	 * @return 取得した商品詳細情報と、商品詳細画面へのエンドポイントを返す
	 */
	
	@GetMapping("/itemDetail/{itemId}")
	public String itemDetail(@PathVariable("itemId") String itemId, Model model) {
	    
	    //// リダイレクトチェック ////
	    if (model.containsAttribute("itemInfoForm")) {
	    	// modelにitemInfoFormが設定されてる場合
	    	return "stockSystem/itemDetail";
	    }
	    
	    ItemInfoForm itemInfoForm = new ItemInfoForm();
	    
	    //// 商品情報取得 ////
	    // セッションに商品情報が存在するならセッションから取得
	    if (itemSession.getItemInfoForm() != null) {
	    	
            itemInfoForm = itemSession.getItemInfoForm();
            
        } else {
        	
            // そうでなければ商品IDを使ってDBから取得
            int itemIdInt = Integer.parseInt(itemId); // itemIdをintに変換
            itemInfoForm = itemService.getItemDetail(itemIdInt); // 商品情報取得
            
        }
	    
	    // セッションに更新前の商品情報がない場合はセット
	    if (itemSession.getBeforeUpdItemInfoForm() == null) {
	    	
	        itemSession.setBeforeUpdItemInfoForm(itemInfoForm); 
	        
	    }
	    
	    model.addAttribute("itemInfoForm", itemInfoForm);
	    
	    // 商品詳細画面へ遷移
	    return "stockSystem/itemDetail";
	}
	
	
	/**
	 *  在庫情報詳細への遷移処理
	 * @param itemId 受け取った商品ID
	 * @param model
	 * @return  取得した在庫情報と、在庫情報画面へのエンドポイントを返す
	 */
	@GetMapping("/itemStock/{itemId}")
	public String itemStock(@PathVariable("itemId") String itemId, Model model) {
		
	    //// リダイレクトチェック ////		
		if (model.containsAttribute("itemStockForm")) {
			return "stockSystem/itemStock";
		}
		
	    ItemStockForm itemStockForm = new ItemStockForm();
	    
	    //// 在庫情報取得 ////
	    // セッションに在庫情報が存在するならセッションから取得
	    if (itemSession.getItemStockForm() != null) {
            itemStockForm = itemSession.getItemStockForm();
            
        } else {
        	
        	// セッションに在庫情報が存在しない場合、商品IDを使ってDBから取得
            int itemIdInt = Integer.parseInt(itemId); // itemIdをintに変換
            itemStockForm = itemService.getItemStock(itemIdInt); //在庫情報取得
        }

	    
	    // セッションに更新前の在庫情報がない場合はセット
	    if (itemSession.getBeforeUpdItemStockForm() == null) {
	    	
	        itemSession.setBeforeUpdItemStockForm(itemStockForm);
	        
	    }
	    
	    model.addAttribute("itemStockForm", itemStockForm);
	    
	    // 在庫詳細画面へ遷移
	    return "stockSystem/itemStock";
	}
	
	
	/**
	 *  在庫情報更新処理
	 * @param itemStockForm 受け取った更新フォーム情報
	 * @param result バリデーションチェック結果
	 * @param re リダイレクト
	 * @param model
	 * @return 更新に成功したら最新の商品リストとitemListへのエンドポイントを返し、失敗したらitemStockへリダイレクト
	 */
	@PostMapping("/stockUpdate")
	public String stockUpdate(@ModelAttribute @Validated ItemStockForm itemStockForm, BindingResult result, RedirectAttributes re, Model model){ 
			
		//// バリデーションチェック ////
		// チェックに引っかかった場合、登録画面へリダイレクトしてからメッセージを表示
		// BindingResult.MODEL_KEY_PREFIXを使うと、受け取ったフォーム情報を保持したままビューに再度表示できる
		if(result.hasErrors()) {
			itemStockForm.setErrMsg(msg.getMessage("Valid.FailInputValue", null, null));
			re.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "itemStockForm", result);
			re.addFlashAttribute("itemStockForm", itemStockForm);
			
			// 商品IDと紐づけて在庫詳細画面へリダイレクト
			return "redirect:/itemStock/" + itemStockForm.getItemId();
		}
		
		
		//// 更新情報チェック ////
		// セッションの更新前在庫情報を代入
		ItemStockForm beforeForm = itemSession.getBeforeUpdItemStockForm();
		
		// 更新情報チェックの結果を受け取る(デフォルトの戻り値はnull)
		String stockCheckResult = itemService.updateCheck(beforeForm, itemStockForm);
		
		// 更新情報チェックでエラーメッセージが発生したら更新失敗
		if(stockCheckResult != null) {			
			itemStockForm.setErrMsg(stockCheckResult);
			re.addFlashAttribute("itemStockForm", itemStockForm);
			
			// 商品IDと紐づけて在庫詳細画面へリダイレクト
			return "redirect:/itemStock/" + itemStockForm.getItemId();
		}
		
		// 在庫情報更新後、商品情報更新で例外発生時にロールバックさせるため、例外をキャッチできるようにする
		try {
			//// 更新チェック成功後 ////
			// 在庫更新して商品一覧取得
			ItemListForm itemList = itemService.updateStock(itemStockForm, beforeForm, LOGINUSER);
			
			
			// セッションの在庫情報をクリア
			itemSession.clearItemStockForm();
			
			model.addAttribute("itemListForm", itemList);
			
			// 商品一覧画面へ遷移
			return "stockSystem/itemList";
			
		}catch(Exception e){
			// 商品情報更で例外発生時にリダイレクト
			itemStockForm.setErrMsg(e.getMessage());
			re.addFlashAttribute("itemStock", itemStockForm);
			return "redirect:/itemStock" + itemStockForm.getItemId();
		}
		
		
		
	}
	
	
	/**
	 *  商品情報削除処理
	 * @param itemInfoForm 受け取った商品ID
	 * @param model
	 * @return 最新の商品リストとitemListへのエンドポイントを返す
	 */
	@PostMapping("/itemDelete")
	public String itemDelete(@ModelAttribute ItemInfoForm itemInfoForm, Model model) {
		
		ItemListForm itemList = new ItemListForm();
		
		try {
			// 削除処理実行			
			itemList = itemService.deleteItem(itemInfoForm);
			
			// セッションのForm情報クリア
			itemSession.clearItemInfoForm();
			itemSession.clearItemStockForm();
			
			model.addAttribute("itemListForm", itemList);
			return "stockSystem/itemList";
			
		}catch(Exception e) {
			itemList.setErrMsg(e.getMessage());
			model.addAttribute("itemListForm", itemList);
			return "stockSystem/itemList";
		}
		
	}

}
