package com.citywithincity.ecard.utils;

import android.app.Activity;

//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShareUtil {
	public static final String SHARE_URL = "http://218.5.80.17:8092/weixin/share";
	
	
	public static void share(Activity context,String title) {
		// File file = loadImage2File(sCardListVo.thumb);
	/*	File file = ImageUtil.takeSnapshot(context);
		ShareSDK.initSDK(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
//		oks.setNotification(R.drawable.ic_launcher,context.getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(context.getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(SHARE_URL);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(title+ SHARE_URL);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(file.getAbsolutePath());
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(SHARE_URL);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment(sCardListVo.title);
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(context.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(SHARE_URL);

		// 启动分享GUI
		oks.show(context);
		*/
	}
	
}
