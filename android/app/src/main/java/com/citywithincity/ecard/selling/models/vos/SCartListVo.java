package com.citywithincity.ecard.selling.models.vos;

import android.annotation.SuppressLint;
import android.content.Context;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.selling.utils.DiyUtils;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.Alert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("DefaultLocale")
@Databind
public class SCartListVo implements IJsonValueObject,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 编号
	 */
	public int id;
	/**
	 * 标题
	 */
	@ViewId(id=R.id.id_title)
	public String title;
	/**
	 * 价格
	 */
	public float price;
	/**
	 * 缩略图
	 */
	@ViewId(id=R.id.s_card_img)
	public String thumb;
	/**
	 * 数量
	 */
	@ViewId(id=R.id.num)
	public int count;
	
	/**
	 * 预充值
	 */
	@ViewId(id=R.id.pre_charge)
	public int recharge = 0;
	
	
	/**
	 * 是否选中
	 */
	@ViewId(id=R.id.s_card_select)
	public boolean isSeleted = false;
	
	/**
	 * 购物车Id
	 */
	public int cartId;	
	/**
	 * diy卡片图片id
	 */
	public int imgId;
	
	
	@ViewId(id=R.id.id_stock)
	public int stock;
	
	/**
	 * 表示diy, 0普通卡，1diy
	 */
	public int cardType = -1;
	
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		id = json.getInt("ID");
		title = json.getString("TITLE");
		price = (float)json.getInt("PRICE")/100;
		thumb = json.getString("THUMB");
		count = json.getInt("COUNT");
		cartId = json.getInt("CART_ID");
		try{
			imgId = json.getInt("IMG_ID");
		}catch(JSONException ex){
			imgId = 0;
		}
		
		recharge = json.getInt("RECHARGE")/100;
		stock = json.getInt("STOCK");
	}
	
	
	public float getTotalPrice(){
		return (price + recharge) * count;
	}
	@ViewId(id=R.id.id_single_total_price)
	public String getTotalPriceString(){
		return String.format("%.2f", (price + recharge) * count);
	}
	
	@ViewId(id=R.id.unit_price)
	public String getPriceString(){
		return String.format("￥%.2f", price);
	}

	@ViewId(id=R.id.count)
	public String getCount(){
		return String.format("X%d", count);
	}

	public Map<String,Object> toJson(Context context) {
		Map<String,Object> jsonObject = new HashMap<String, Object>();
		jsonObject.put("id", id);
		if(cartId>0){
			jsonObject.put("cart_id", cartId);
		}
		jsonObject.put("count", count);
		jsonObject.put("recharge", recharge);
		if(imgId>0){
			jsonObject.put("image_id", imgId);
		}
		if (cardType == 1) {


			try {
				jsonObject.put("file", DiyUtils.getDiy(context,thumb));
			} catch (IOException e) {
				Alert.showShortToast("文件写入失败,请检查sk卡的权限或sd卡是否已满");
			}

			/*
            File tempFile = new File(SDCardUtil.getSDCardDir(LifeManager.getActivity(),"png"), System.currentTimeMillis() + ".jpg");

			Bitmap bm = BitmapFactory.decodeFile(thumb);
			try {
				bm.compress(CompressFormat.JPEG, 95, new FileOutputStream(tempFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                Alert.showShortToast("文件写入失败,请检查sk卡的权限或sd卡是否已满");
			}

			try {
				jsonObject.put("file", IoUtil.readBytes(tempFile));
				tempFile.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		
		return jsonObject;
	}

}
