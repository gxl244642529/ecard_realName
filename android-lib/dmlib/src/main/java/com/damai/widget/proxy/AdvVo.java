package com.damai.widget.proxy;

public class AdvVo {
	
	public static final int PLAT_ALL = 0;
	public static final int PLAT_ANDROID = 1;
	public static final int PLAT_IOS = 2;
	
	
	
	private int id;
	private String title;
	private String url;
	private String img;
	private String imgBig;
	/**
	 * 平台
	 */
	private int plat;
	//是否允许进入
	private int enable;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getImgBig() {
		return imgBig;
	}
	public void setImgBig(String imgBig) {
		this.imgBig = imgBig;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getPlat() {
		return plat;
	}
	public void setPlat(int plat) {
		this.plat = plat;
	}
	
}
