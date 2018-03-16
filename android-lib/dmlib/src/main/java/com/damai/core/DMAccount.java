package com.damai.core;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import android.app.Activity;
import android.util.Base64;

import com.citywithincity.crypt.Aes128DataCrypt;
import com.citywithincity.models.LocalData;
import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.MemoryUtil;
import com.citywithincity.utils.SDCardUtil;
import com.damai.auto.LifeManager;
import com.damai.interfaces.DMLoginCaller;
import com.damai.push.Push;

public abstract class DMAccount implements Serializable {

	private static final String SUB_USER_INFO = "userInfo" ;

	public static interface LogoutListener{
		void onLogout(DMAccount account);
	}

	private static final long serialVersionUID = -890169255794879160L;
	private String id;
	private String account;
	private String password;
	//平台
	private String channel;
	
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private static LogoutListener _logoutListener;

	public static void setLogoutListener(LogoutListener logoutListener) {
		_logoutListener = logoutListener;
	}
	private static Class<? extends DMAccount> accountClazz;
	private static DMAccount instance;
	private static final String KEY = "account";

	public static void setClass(Class<? extends DMAccount> clazz){
		accountClazz = clazz;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends DMAccount> T get(){
		if(instance==null){
			instance =   read();  //  LocalData.read().getObject(KEY, accountClazz);
		}
		return (T) instance;
	}
	
	private static DMAccount read(){
		DMAccount account = readAndGet();
		if(account==null){
			try {
				account = accountClazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return account;
	}

	private static DMAccount readAndGet(){
		try{
			return readFromFile();
		}catch (Exception e)
		{
			return readFromConfig();
		}
	}

	private static DMAccount readFromConfig() {
		return LocalData.createDefault(LocalData.LocalDataMode.MODE_READ).getObject(
				SUB_USER_INFO, accountClazz);
	}

	private static DMAccount readFromFile() throws Exception {
		File dir = SDCardUtil.getSDCardDir(LifeManager.getApplicationContext(), "data");
		//加载用户信息
		File user = new File(dir,"user.data");
		//读取,加密信息
		byte[] bytes = IoUtil.readBytes(user);
		Aes128DataCrypt dataCrypt = new Aes128DataCrypt();
		byte[] content = dataCrypt.decript( Base64.decode(bytes, 0),
                Push.getUdid().substring(0,16).getBytes() );

		return MemoryUtil.getObject(content);

	}

	public static void logout(){
		DMAccount account = get();
		if(_logoutListener!=null){
			_logoutListener.onLogout(account);
		}
		account.setAccount(null);
		account.setId(null);
		account.setPassword(null);
		account.setChannel(null);
		account.reset();
		account.save();
		if(DMLib.getJobManager()!=null)DMLib.getJobManager().clearSession();

		Push.onLogout();


	}
	
	protected abstract void reset();

	private void saveToFile() throws Exception {
		//转化为json
		byte[] content = MemoryUtil.getBytes(this);
		Aes128DataCrypt dataCrypt = new Aes128DataCrypt();
		byte[] src = dataCrypt.encript(content, Push.getUdid().substring(0,16).getBytes() );

		File dir = SDCardUtil.getSDCardDir(LifeManager.getApplicationContext(), "data");
		//加载用户信息
		File user = new File(dir,"user.data");
		IoUtil.writeBytes(user,Base64.encode(src, 0));
	}

	public void save(){
		try{
			saveToFile();
		}catch (Exception e){
			saveToConfig();
		}
		
		
	}

	private void saveToConfig() {
		LocalData.createDefault(LocalData.LocalDataMode.MODE_WRITE)
				.putObject(SUB_USER_INFO, this).destroy();
	}


	public static boolean isLogin(){
		DMAccount account = get();
		return account!=null && (account.id!=null || account.channel!=null);
	}

	private static DMLoginCaller _caller;

	public static  void setLoginCaller(DMLoginCaller caller){
		_caller = caller;
	}


	public static void callLoginActivity(LoginListener listener) {

	/*	Activity currentActivity=LifeManager.getActivity();
		LoginListenerWrap.getInstance().setListener(listener);
		currentActivity.startActivity(new Intent(currentActivity.getPackageName() + ".action.LOGIN"));
		*/
        Activity currentActivity=LifeManager.getActivity();
        callLoginActivity(currentActivity,listener);
	}



	public static void callLoginActivity(Activity activity,LoginListener listener) {

		/*	LoginListenerWrap.getInstance().setListener(listener);
		activity.startActivity(new Intent(activity.getPackageName() + ".action.LOGIN"));*/

        _caller.callLogin(activity,listener);
	}



	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	
	

	

}
