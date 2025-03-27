package com.freeks.training.stockSystem.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.freeks.training.stockSystem.entity.ItemInfoEntity;
import com.freeks.training.stockSystem.entity.ItemStockEntity;

@Repository
@Mapper
public interface ItemInfoMapper {
	
	/**
	 *  商品情報リスト取得
	 *  引数があるものは受け取った値をItemInfoMapper.xmlに渡す
	 * @return
	 */

	public List<ItemInfoEntity> getFindAll();
	
	/**
	 *  商品リストのうち、CSV出力する項目を取得
	 * @return
	 */
	public List<ItemInfoEntity> getExportsItem();
	
	/**
	 *  商品IDから情報取得
	 * @param itemId
	 * @return 
	 */
	public ItemInfoEntity findByItemId(int itemId);
	
	/**
	 *  商品名を検索
	 * @param itemName
	 * @return
	 */
	public ItemInfoEntity findByItemName(String itemName);
	
	/** 最大IDを取得
	 * 
	 * @return
	 */
	public Integer getMaxItemId();
	
	/**
	 *  商品IDから在庫情報取得
	 * @param itemId
	 * @return
	 */
	public ItemStockEntity findStockByItemId(int itemId);  // ここstorageStockIdじゃなくてOK
	
	/**
	 *  商品情報登録
	 * @param entity
	 * @return
	 */
	public int insert(ItemInfoEntity entity);
	
	/**
	 *  在庫情報登録
	 * @param entity
	 * @return
	 */
	public int stockInsert(ItemStockEntity entity);
	
	/**
	 *  商品情報更新
	 * @param entity
	 * @return
	 */
	public int updateItemInfo(ItemStockEntity entity);
	
	/**
	 *  在庫情報更新
	 * @param entity
	 * @return
	 */
	public int updateStock(ItemStockEntity entity);
	
	/**
	 * 商品情報削除
	 * @param itemId
	 * @return
	 */
	public int delete(int itemId);
	
	/**
	 *  在庫情報削除
	 * @param itemId
	 * @return
	 */
	public int deleteStock(int itemId);
	
}


