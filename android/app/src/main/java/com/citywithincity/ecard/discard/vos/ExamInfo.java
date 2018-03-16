package com.citywithincity.ecard.discard.vos;

import android.view.View;

import com.damai.helper.a.DataType;

public class ExamInfo {
	private String idCard;
	/**
	 * 用户信息
	 */
	private String name;
	private String phone;
	private String custNo;

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

	// 0:身份证 1：户口
	private int idCardType;

	private boolean local;

	private int type;

	private int status;

	/**
	 * 优惠卡提交证件类型
	 * 
	 * @return
	 */
	@DataType(DataType.HINT)
	public String getNavCode_Hint() {
		return "请输入" + getDiscardType();
	}

	/**
	 * 优惠卡提交证件类型
	 * 
	 * @return
	 */
	public String getDiscardType() {
		switch (type) {
		case BookInfo.TYPE_OLD:
			return "老人证号";
		case BookInfo.TYPE_WORK:
			return "劳模证号";
		case BookInfo.TYPE_HERO:
			return "烈属证号";
		default:
			return "学生证号";
		}
	}

	public String getSecPicNameText() {
		switch (type) {
		case BookInfo.TYPE_OLD:
			return "户口本";
		case BookInfo.TYPE_WORK:
			return "劳模证";
		case BookInfo.TYPE_HERO:
			return "烈属证";
		default:
			return "学生证";
		}
	}

	@DataType(DataType.VISIBLE)
	public int getDiscardTypeContainer() {
		return type == BookInfo.TYPE_OLD ? View.GONE : View.VISIBLE;
	}

	private boolean isStudentCard(){
		if(type == BookInfo.TYPE_OLD )return false;
		if(type == BookInfo.TYPE_HERO )return false;
		return type != BookInfo.TYPE_WORK;
	}
	@DataType(DataType.VISIBLE)
	public int getSchoolName_Visible(){
		return isStudentCard() ? View.VISIBLE : View.GONE;
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

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
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

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
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

}
