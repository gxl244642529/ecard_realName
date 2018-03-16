package com.citywithincity.ecard.insurance.models;

import android.app.Activity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.widgets.InsuranceClickableSpan;
import com.citywithincity.ecard.insurance.widgets.InsuranceClickableSpan.IOnTextClickListener;

public class InsuranceUtil {
	
	/**
	 * 　　* 重新计算listView高度 　　* @param listView 　　
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
		
	}

	// 　　在我们setAdapter()的之前，我们调用上面的方法，如setListViewHeightBasedOnChildren(accomplishmentStateListView);
	// 　　因为我是在ListView中嵌套GridView，所以重新计算GridView的总高度的时候，要放在setAdapter(...GridViewAdapter)这个BaseAdapter的衍生类里。代码如下：

	/**
	 * 　　* 计算gridview高度 　　* @param gridView 　　
	 */
	public static void setGridViewHeightBasedOnChildren(GridView gridView) {
		// 获取GridView对应的Adapter
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int rows;
		int columns = 0;
		int horizontalBorderHeight = 0;
		// Class<?> clazz = gridView.getClass();
		try {

			columns = gridView.getNumColumns();

			// 利用反射，取得每行显示的个数
			// Field column = clazz.getDeclaredField("mRequestedNumColumns");
			// column.setAccessible(true);
			// columns = (Integer) column.get(gridView);
			// 利用反射，取得横向分割线高度
			// Field horizontalSpacing =
			// clazz.getDeclaredField("mRequestedHorizontalSpacing");
			// horizontalSpacing.setAccessible(true);
			// horizontalBorderHeight = (Integer)
			// horizontalSpacing.get(gridView);

			horizontalBorderHeight = 1;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
		if (listAdapter.getCount() % columns > 0) {
			rows = listAdapter.getCount() / columns + 1;
		} else {
			rows = listAdapter.getCount() / columns;
		}
		int totalHeight = 0;
		for (int i = 0; i < rows; i++) { // 只计算每项高度*行数
			View listItem = listAdapter.getView(i, null, gridView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight + horizontalBorderHeight * (rows - 1);// 最后加上分割线总高度
		gridView.setLayoutParams(params);

	}
	
	public static String setUrl(String url) {
		return ECardConfig.PICC + url;
	}
	
	/**
	 * 设置HTML标签的文字
	 * @param context
	 * @param viewId
	 * @param text
	 */
	public static void setTextFromHtml(Activity context,int viewId, String text) {
		TextView textView = (TextView) context.findViewById(R.id.intro);
		textView.setText(Html.fromHtml(text));
	}
							
	public static void setTextClick(Activity context, String string, int colorId,IOnTextClickListener listener){
		
		TextView insuranceClause;
		
		if (TextUtils.isEmpty(string)) {
			string = "《保险条款》";
		}
		
		//TextView添加点击事件
		insuranceClause = (TextView) context.findViewById(R.id.insurance_clause);
		insuranceClause.setVisibility(View.VISIBLE);
//        String clause = "《保险条款》，";
        SpannableString clauseSpannableString = new SpannableString(string);
        InsuranceClickableSpan clickClause = new InsuranceClickableSpan(context,colorId);
        clauseSpannableString.setSpan(clickClause, 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        
        SpannableString noticeSpannableString = new SpannableString("《重要告知》");
        InsuranceClickableSpan noticeSpan = new InsuranceClickableSpan(context,colorId);
        noticeSpannableString.setSpan(noticeSpan, 0, "《重要告知》".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        
        insuranceClause.setText("本人已认真阅读了本保单所适用的所有");
        insuranceClause.append(clauseSpannableString);
        insuranceClause.append("和");
        insuranceClause.append(noticeSpannableString);
        insuranceClause.append("，了解并接受其中保险责任、责任免除、投保人、被保险人义务、赔偿处理、其他事项等相关约定，同意以此作为订立保险合同的依据。");
        clickClause.setTag(string);
        clickClause.setListener(listener);
        
        noticeSpan.setListener(listener);
        
        insuranceClause.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	public static void setTextClick2(Activity context, String string, int colorId,IOnTextClickListener listener){
		
		TextView insuranceClause;
		
		if (TextUtils.isEmpty(string)) {
			string = "《保险条款》";
		}
		
		//TextView添加点击事件
		insuranceClause = (TextView) context.findViewById(R.id.insurance_clause);
		insuranceClause.setVisibility(View.VISIBLE);
//        String clause = "《保险条款》，";
        SpannableString clauseSpannableString = new SpannableString(string);
        InsuranceClickableSpan clickClause = new InsuranceClickableSpan(context,colorId);
        clauseSpannableString.setSpan(clickClause, 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        
        SpannableString noticeSpannableString = new SpannableString("《重要告知》");
        InsuranceClickableSpan noticeSpan = new InsuranceClickableSpan(context,colorId);
        noticeSpannableString.setSpan(noticeSpan, 0, "《重要告知》".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        
        insuranceClause.setText("点击查看");
        insuranceClause.append(clauseSpannableString);
        insuranceClause.append("和");
        insuranceClause.append(noticeSpannableString);
        clickClause.setTag(string);
        clickClause.setListener(listener);
        
        noticeSpan.setListener(listener);
        
        insuranceClause.setMovementMethod(LinkMovementMethod.getInstance());
	}

}
