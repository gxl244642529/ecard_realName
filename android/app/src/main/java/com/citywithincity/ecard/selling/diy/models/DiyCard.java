package com.citywithincity.ecard.selling.diy.models;

import com.citywithincity.ecard.selling.models.vos.SCartVo;

import java.io.Serializable;
/**
 * 
 * @author Administrator
 * 
 */

public class DiyCard extends SCartVo implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long id;
	
	//反面
	public String fImage;
	
	public float price;
	
	public int stock;
	
/*	(generatedId = true)
	public int id;
	
	(canBeNull = true)
	public int bgId;
	
	(canBeNull = false) 
	public  String cardBackground;//用户自定义的图片（正面）
	
	(canBeNull = false) 
	public String front;//系统指定的几种图片（反面）
	
	(canBeNull = false) 
	public String time;
	
	(canBeNull = false) 
	public String title;
	
	(canBeNull = false) 
	public String thumb;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCardBackground() {
		return cardBackground;
	}
		

	public void setCardBackground(String cardBackground) {
		this.cardBackground = cardBackground;
	}

	public String getFront() {
		return front;
	}
	public void setFront(String front) {
		this.front = front;
	}

	public DiyCard() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public int getBgId() {
		return bgId;
	}

	public void setBgId(int bgId) {
		this.bgId = bgId;
	}*/
}
