package com.damai.widget.vos;

import java.io.Serializable;

import android.view.View;

import com.damai.helper.a.DataType;


public class AddressVo implements Serializable {

	private int id;
	private int tyId;
	private long cmId;
	private int shengId;
	private int shiId;
	private int quId;
	private String sheng;
	private String shi;
	private String qu;
	private String jie;
	private boolean def;
	private String name;
	private String phone;
	private String postcode;
	private long lastModified;

	public AddressVo getAddressVo(){
		return this;
	}
	
	
	public static String formatAddress(AddressVo vo){
		StringBuilder sb = new StringBuilder().append(vo.getSheng()).append(vo.getShi());
		if(vo.getQu()!=null){
			sb.append(vo.getQu());
		}
		sb.append(vo.getJie());
		return sb.toString();
	}
	
	
	
	public String getAddress(){
		StringBuilder sb = new StringBuilder().append(sheng).append(shi);
		if(getQu()!=null){
			sb.append(qu);
		}
		sb.append(jie);
		return sb.toString();
	}
	@DataType(DataType.VISIBLE)
	public int getDefShow(){
		return def ? View.VISIBLE : View.GONE;
	}
	public String getSheng() {
		return sheng;
	}
	public void setSheng(String sheng) {
		this.sheng = sheng;
	}
	public String getShi() {
		return shi;
	}
	public void setShi(String shi) {
		this.shi = shi;
	}
	public String getQu() {
		return qu;
	}
	public void setQu(String qu) {
		this.qu = qu;
	}
	public String getJie() {
		return jie;
	}
	public void setJie(String jie) {
		this.jie = jie;
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
	
	
	
	public boolean isDef() {
		return def;
	}
	public void setDef(boolean def) {
		this.def = def;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public int getTyId() {
		return tyId;
	}
	public void setTyId(int tyId) {
		this.tyId = tyId;
	}
	public long getCmId() {
		return cmId;
	}
	public void setCmId(long cmId) {
		this.cmId = cmId;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public int getShiId() {
		return shiId;
	}
	public void setShiId(int shiId) {
		this.shiId = shiId;
	}
	public int getShengId() {
		return shengId;
	}
	public void setShengId(int shengId) {
		this.shengId = shengId;
	}
	public int getQuId() {
		return quId;
	}
	public void setQuId(int quId) {
		this.quId = quId;
	}


}
