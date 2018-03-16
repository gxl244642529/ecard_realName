package com.citywithincity.ecard.discard.vos;

import android.annotation.SuppressLint;
import android.view.View;

import com.citywithincity.ecard.R;
import com.damai.helper.a.DataType;
/**
 *  {"result":{"idCard":"320483198110070514","name":"","phone":"","comment":null,
 *  "postCode":null,"schoolName":"","schoolCode":null,"birth":"19811007",
 *  "cityCode":null,"areaCode":"","navCode":null,"sex":0,"savType":0,"cardId":null,
 *  "img1":null,"img2":null,"img3":null,"idCardType":0,"local":true,"custNo":null,
 *  "cardMType":null,"cardSType":null,"type":4,"status":0},"flag":0}
 * @author renxueliang
 *
 */
@SuppressLint("DefaultLocale")
public class BookInfo {
	
	public static final int TYPE_OLD = 125000;

	public static final int TYPE_WORK = 126000;

	public static final int TYPE_HERO = 127000;
	
	private String custNo;
	private String idCard;
	private String name;
	private String phone;
	private String comment;
	private String postCode;
	private String schoolName;
	private String schoolCode;
	private String birth;
	private String cityCode;
	private String areaCode;
	private String navCode;
	private int sex;
	private int savType;
	private String cardId;
	private int idCardType;
	private int status;
	private int type;
	private boolean local;
	
	
	/**
	 * 是否补办
	 * @return
	 */
	public boolean isReissued(){
		
		return savType != 0;
		
	}

	public boolean isStudentCard() {
		return !(type == 125000 || type == 126000 || type == 127000);
	}

	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getSchoolCode() {
		return schoolCode;
	}
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getNavCode() {
		return navCode;
	}
	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getSavType() {
		return savType;
	}
	public void setSavType(int savType) {
		this.savType = savType;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public int getIdCardType() {
		return idCardType;
	}
	public void setIdCardType(int idCardType) {
		this.idCardType = idCardType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isLocal() {
		return local;
	}
	public void setLocal(boolean local) {
		this.local = local;
	}
	
	
	
	@DataType(DataType.VISIBLE)
	public int getSchoolName_Visible(){
		return isStudentCard() ? View.VISIBLE : View.GONE;
	}
	
	public String getImgTip(){
		switch (type) {
		
		case TYPE_WORK:
			return "劳模证原件照片";
		case TYPE_HERO:
			return "烈属证原件照片";
		case TYPE_OLD:
			return "户口本原件照片";
		default:
			return "学生证原件照片";
		}
	}

	/**
	 * 优惠卡提交证件类型
	 * @return
	 */
	@DataType(DataType.HINT)
	public String getNavCode_Hint(){
		return "请输入" + getDiscardType();
	}
	
	/**
	 * 优惠卡提交证件类型
	 * @return
	 */
	public String getDiscardType(){
		switch (type) {
			
		case TYPE_OLD:
			return "老人证号";
		case TYPE_WORK:
			return "劳模证号";
		case TYPE_HERO:
			return "烈属证号";
		default:
			return "学生证号";
		}
	}

	public int getThumb() {
		switch (type) {
		case TYPE_OLD:
			return R.drawable.ic_old_card;
		case TYPE_WORK:
			return R.drawable.ic_hero_card;
		case TYPE_HERO:
			return R.drawable.ic_hero_card;
		default:

			return R.drawable.pic_student;
		}
	}
	
	public String getDiscardCardName(){
		switch (type) {
		case TYPE_OLD:
			return "敬老卡";
		case TYPE_WORK:
			return "劳模卡";
		case TYPE_HERO:
			return "烈属卡";
		default:
			return "学生卡";
		}
	}
	
	public String getDiscardName(){
		switch (type) {
		case TYPE_OLD:
			return "敬老证";
		case TYPE_WORK:
			return "劳模证";
		case TYPE_HERO:
			return "烈属证";
		default:
			return "学生证";
		}
	}
	/**
	 * 
	 * @param checked
	 * @return
	 */
	@SuppressLint("DefaultLocale") public CharSequence getFormatTotalPrice(boolean recharge) {
		return String.format("¥ %.02f", (float)(getCardPrice()  +(recharge ? 3000 :0)+ getTransFee())/100);
	}
	
	public int getTransFee(){
		return 1000;
	}
	
	
	@SuppressLint("DefaultLocale")
	public String formatCardPrice(){
		return String.format("¥ %.02f",(float) getCardPrice()/100);
	}
	
	/**
	 * 卡的价格
	 * @return
	 */
	public int getCardPrice(){
		if(isStudentCard() || isReissued()){
			return 1500;
		}
		return 0;
		
	}
	
	@DataType(DataType.VISIBLE)
	public int getDiscardTypeContainer_Visible(){
		return type == TYPE_OLD ? View.GONE  : View.VISIBLE;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	
	
}
