package com.citywithincity.ecard.interfaces;

import com.citywithincity.ecard.models.vos.LostCardDetailInfo;
import com.citywithincity.interfaces.IDestroyable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




public interface IECardPlatform extends IDestroyable {

	class ConfigInfo{
		public String page_l;
		public List<HeadInfo> heads = new ArrayList<HeadInfo>();
	}
	
	class HeadInfo{
		public String url;
		public String data;
	}
	
	/**
	 * 数据
	 * @author Randy
	 *
	 */
	class AdInfo{
		public int type;
		public Object data;
	}
	
	
	
	
	
	
	/**
	 * 中间结果
	 * @author randy
	 *
	 */
	class LotteryResult{
		public int result;//结果
		public int leftCount;//剩余抽奖次数
	}
	
	/**
	 * 我的优惠券信息
	 * @author randy
	 *
	 */
	class MyCouponInfo implements Serializable
	{
		
		//这里
		public static final int CC_NULL=0;
		public static final int CC_USED=1;
		public static final int CC_OUT=2;
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public int id;//id   ccouponid
		
		public String title;//标题
		
		public String img;//优惠券图片
		
		public String bsname;//商家名称
		
		public String etime;
		
		public int state;
	}
	
	
	/**
	 * 详细
	 * @author randy
	 *
	 */
	class MyCouponDetail implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5059775744642294279L;
		public int id;//id
		public int couponID; //对应的优惠券id
		//public int bsid;//商家id
		public String tip;//优惠券使用提示
		//public String stime;//开始时间
		public String etime;//结束时间
		//public String left;//剩余时间
		public String bsname;//商家名称
		public int state;//使用情况 0:未使用，1：已经使用 2：已经过期
		//二维码
		public String code;
		
		//public ShopGruop shops;//支持分店
	}
	
	
	
	
	
	
	
	


	
	
	/**
	 * 我的积分列表信息
	 */
	class MyBonusInfo
	{
		public String title;
		public String date;
		public String address;
		public String cost;
		public String bonus;
	}
	
	
	/**
	 * 信息
	 */
	class MyMessageInfo
	{
		public String title;
		public String content;
		public int createTime;
	}
	
	
	
	
	
	/**
	 * 路线搜索信息,包含起点和终点
	 * @author randy
	 *
	 */
	class RouteSearchInfo implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public double destlat;
		public double destlng;
		
		//终点名字
		public String destName;
		
	}
	
	
	
	
	class YoungInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String img;
		public String title;
		public String html;
	}
	
	class GlobalInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String s1;
		public String s2;
		public String s3;
		public String s4;
		public String img;
	}
	
	class PickCardQueryResult{
		public int result;
		public LostCardDetailInfo info;
	}
	
	class MessageInfo{
		public String title;
		public String content;
	}
	void clearCache();
	//清除缓存
	void onExit();
	
	
	
}
