package com.citywithincity.ecard.models.vos;

import android.content.Context;
import android.os.Bundle;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.react.SysModule;
import com.citywithincity.ecard.utils.NfcUtil;
import com.citywithincity.utils.MemoryUtil;
import com.damai.core.DMAccount;
import com.damai.core.DMServers;
import com.damai.react.ReactUtils;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class ECardUserInfo extends DMAccount {

	private String phone;
	private String head;// head:头像地址
	private String bg;				 //壁纸地址
	private String nick;// nick:昵称
	private String bindPhone;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	private boolean valid;
	private String userHash;


	@Override
	protected void reset() {
		phone = null;
		head = null;
		bg = null;
		nick = null;
		bindPhone = null;
		userHash = null;
	}


	public WritableMap toWriteMap(){
		WritableMap bundle = Arguments.createMap();
		try {
			Map<String,Object> data = getData();
			Object value = data.get("bg");
			if(value instanceof  Map){
				data.put("bg",null);
			}

			value = data.get("head");
			if(value instanceof  Map){
				data.put("head",null);
			}

			JSONObject js = new JSONObject(data);
			js.put("account",getAccount());
			bundle.putString("json",  js.toString() );
		} catch (JSONException e) {
			bundle.putString("json",null);
		}
		return bundle;

	}

    /**
     * 初始化
     * @param context
     * @return
     */
	public Bundle toBundle(Context context) {
		Bundle bundle = new Bundle();

		try {
			Map<String,Object> data = getData();
			if(data!=null){
                Object value = data.get("bg");
                if(value instanceof  Map){
                    data.put("bg",null);
                }

                value = data.get("head");
                if(value instanceof  Map){
                    data.put("head",null);
                }

                JSONObject js = new JSONObject(data);
				js.put("account",getAccount());
				bundle.putString("json",js.toString());
			}else{
				bundle.putString("json",null);
			}
		} catch (JSONException e) {
			bundle.putString("json",null);
		}

        if(NfcUtil.isAvailable(context)){
            bundle.putBoolean("nfc",true);
        }
		return bundle;
	}
	public void fromJson(JSONObject data,String account,String pwd) {

		try {
			setData(MemoryUtil.jsonToMap(data));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		setAccount(account);
		setPassword(pwd);
		try {
			setUserHash(data.getString("hash"));
			setId(String.valueOf(data.get("userid")));
			if(!data.isNull("phone")){
				setPhone(data.getString("phone"));
			}
			if(!data.isNull("bg")){
				String bg = data.getString("bg");
				if(bg.startsWith("/")){
					bg = DMServers.getImageUrl(0, bg);
				}
				setBg(bg);
			}

			if(!data.isNull("head")){
				String head = data.getString("head");
				if(head.startsWith("/")){
					head = DMServers.getImageUrl(0, head);
				}
				setHead(head);
			}
			valid = data.optBoolean("valid");

		} catch (JSONException e) {
			throw new RuntimeException(e);
		}



		save();
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getBg() {
		return bg;
	}

	public void setBg(String bg) {
		this.bg = bg;
	}


	public String getBindPhone() {
		return bindPhone;
	}


	public void setBindPhone(String bindPhone) {
		this.bindPhone = bindPhone;
	}




	public Map<String,Object> getData() {
		return data;
	}


	public void setData(Map<String,Object> data) {
		this.data = data;
	}

	private Map<String,Object> data;




	public String getUserHash() {
		return userHash;
	}

	public void setUserHash(String userHash) {
		this.userHash = userHash;
	}
}
