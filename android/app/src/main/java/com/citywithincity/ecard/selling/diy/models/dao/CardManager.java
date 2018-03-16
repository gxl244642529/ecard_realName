package com.citywithincity.ecard.selling.diy.models.dao;

import com.citywithincity.ecard.selling.diy.models.DiyCard;
import com.citywithincity.models.FileDao;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.MemoryUtil;

import java.io.IOException;
import java.util.List;

public class CardManager {
	//当前数据
	private DiyCard currentData;
	private DiyCard cloneData;
	
	private FileDao<DiyCard> model;
	
	
	/**
	 * 创建新的
	 */
	public void createNew(){
		currentData = new DiyCard();
	}
	
	public DiyCard getCurrent(){
		if(currentData==null){
			currentData = new DiyCard();
		}
		try {
			cloneData = MemoryUtil.clone(currentData);
			return cloneData;
		} catch (Exception e) {
			return null;
		}
		
	}
	

	public CardManager() throws IOException {
		model = new FileDao<DiyCard>(JsonTaskManager.getApplicationContext(),
				DiyCard.class);
	}
	
	
	public List<DiyCard> getList(){
		return model.getAll();
	}
	
	/**
	 * 保存
	 */
	public void save(){
		model.save(cloneData);
		currentData = cloneData;
	}

	public void setCurrentData(DiyCard data) {
		currentData = data;
	}

	public void remove(DiyCard cardVo) {
		model.delete(cardVo);
	}

}
