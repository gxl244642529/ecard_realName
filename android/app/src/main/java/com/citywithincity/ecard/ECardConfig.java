package com.citywithincity.ecard;


import com.citywithincity.paylib.PayType;

public class ECardConfig {
	public static final int[] RECHARGE_PAY =new int[]{
			PayType.PAY_WEIXIN.value(),
			PayType.PAY_ETONGKA.value(),
			PayType.PAY_CMB.value(),
			//	PayType.PAY_UMA.value()
	} ;
	public static String PICC = "http://www.xm95518.com:8989";
 //   public static final String PICC = "http://120.55.115.222:8989";
	public static final boolean PINGAN = true;

//    public static final boolean PINGAN_RELEASE = true;

	public static final boolean PINGAN_RELEASE = PinganConfig.RELEASE;

	public static final boolean YUANXIN = false;

	//内网
//	public static final String JAVA_SERVER = "http://192.168.1.244:8080/api/";
//	public static final String BASE_URL="http://192.168.1.248:8887";
	
//	public static final String JAVA_SERVER = "http://citywithincity.duapp.com/";
//	public static final String JAVA_SERVER = "http://192.168.1.246:8082/api/";
//	public static final String JAVA_SERVER = "http://192.168.3.112:8080/api/";
//	public static final String BASE_URL="http://192.168.1.248:8887";
	
	
//	public static final String JAVA_SERVER = "http://192.168.3.130:8080/api/";

//	
	
//	public static final String JAVA_SERVER = "http://192.168.1.5:8080/api/";
	
	//eCard-wifi
//	public static final String BASE_URL = "http://172.16.35.56:8887";
//	public static final String JAVA_SERVER = "http://172.16.35.56:8080/api/";

	//正式
//	public static final String BASE_URL = "http://218.5.80.17:8092";
	
//	public static final String JAVA_SERVER = "http://218.5.80.17:28095/api/";
	
	//public static final String PICC = "http://120.55.115.222:8989";

//	public static final String JAVA_SERVER = "http://218.5.80.17:28095/api/";
//	public static final String PICC = "http://www.xm95518.com:8989";

	//public static final String JAVA_SERVER = "http://192.168.1.246:7070";

//	public static final String BASE_URL="http://192.168.1.101:8887";
//	public static final String BASE_URL = "http://192.168.1.253:8887";
//	public static final String BASE_URL = "http://192.168.1.248:8887";
	// public static final String SITE_URL = "http://192.168.0.249:8887/api/";
	//public static final String BASE_URL="http://27.154.63.206:7891";
	//	http://110.80.22.108:8887/index.php/admin
	//public static final String BASE_URL="http://192.168.1.113:8887";
	
	
	/**
	 * 测试
	 */
	
	//eCard-3g
	public static String JAVA_SERVER = "http://110.80.22.108:28080/api/";
	public static String BASE_URL="http://110.80.22.108:8887";
	
	/**
	 * 正式
	 */
	/*
	public static String JAVA_SERVER = "http://218.5.80.17:28095/api/";
	public static String BASE_URL = "http://www.xmecard.com:8092";
	*/
	
	
	public static String API_URL = BASE_URL + "/api2/";

	
	
	// 百度社会化组件key
	public static final String BAIDU_APIKEY = "ySWZIINjxpsgvqsgaCrQffAI";

	public static final String PACKAGE_NAME = "com.citywithincity.ecard";
	
	public static final String WEIXIN_APPID = "wx43fbb2036fc139c5";

	public static final String XM_LAT = "24.496503909308";
	public static final String XM_LNG = "118.15870558854";
	
	
	public static final int MIN_CARD_WIDTH = 1016;
	public static final int MIN_CARD_HEIGHT = 640;

}
