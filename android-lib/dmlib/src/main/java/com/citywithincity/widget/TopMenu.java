package com.citywithincity.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.citywithincity.auto.tools.FormError;
import com.citywithincity.interfaces.IDataProvider;
import com.citywithincity.interfaces.IDataProviderListener;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IWidget;
import com.citywithincity.models.AbsListAdapter;
import com.citywithincity.utils.SizeUtil;
import com.citywithincity.widget.data.MenuData;
import com.damai.lib.R;


/**
 * 顶部菜单
 * 数据为：
 * @author Randy
 *
 */
public class TopMenu extends LinearLayout implements IDataProviderListener<List<List<MenuData>>>,IWidget<Map<String, Object>>{
	
	private MenuEventHander[] eventHanders;
	
	private OnMenuSelectListener listener;
	
	public interface OnMenuSelectListener {
		
		/**
		 * 
		 * @param index
		 * @param data
		 * @param fromUser	是否用户点击事件产生
		 */
		void onMenuSelected(int index,MenuData data,boolean fromUser);
	}
	
	public void setOnMenuSelectListener(OnMenuSelectListener listener){
		this.listener = listener;
		
	}
	
	
	public abstract class MenuEventHander implements OnClickListener{

		protected final View triggerView;

		public MenuEventHander(View triggerView){
			triggerView.setOnClickListener(this);
			this.triggerView = triggerView;
		}

		public abstract void setData(List<MenuData> list);
	}
	
	/**
	 * 下拉
	 * @author Randy
	 *
	 */
	class SingleMenu extends MenuEventHander implements OnDismissListener, OnItemClickListener{

		protected final TextView label;
		private final View contentView;
		
		//列表
		protected ListView list;
		//数据源
		protected AbsListAdapter dataProvider;
		
		private MenuData selectedData;
		
		protected int index;


		public SingleMenu(View triggerView,View contentView,int index) {
			super(triggerView);
			label = (TextView)triggerView.findViewById(R.id._text_view);
			this.contentView = contentView;
			this.contentView.findViewById(R.id._popup_bg).setOnClickListener(this);

			this.index = index;
			this.list = (ListView)contentView.findViewById(R.id._list_view);
			list.setOnItemClickListener(this);
			dataProvider = new AbsListAdapter(contentView.getContext(),
					R.layout._tree_sub_item){
				@Override
				protected void initView(View view, Object data) {
					((TreeSubItem)view).setData((MenuData)data);
				}
			};
			list.setAdapter(dataProvider);
		}


		/**
		 * 设置菜单的数据源
		 */
		public void setData(List<MenuData> menuData) {
			dataProvider.setData(menuData);
			for(int i=0,count = menuData.size(); i < count; ++i){
				MenuData data = menuData.get(i);
				if(data.selected){
					selectedData = data;
					setSelectedData(data,false);
					break;
				}
				
			}
		}
		
		private void setSelectedData(MenuData data,boolean fromUser){
			if(listener!=null){
				listener.onMenuSelected(index,data,fromUser);
			}
			label.setText(data.topLabel==null ? data.label : data.topLabel);
		}
		

		@Override
		public void onClick(View v) {
			if(v==contentView){
				Popups.hide();
			}else{
				//show
				label.setSelected(true);
				Popups.addPopup(contentView, triggerView,false,this);
			}
		}

		@Override
		public void onDismiss() {
			label.setSelected(false);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//反选上一个
			if(selectedData!=null){
				selectedData.selected = false;
			}
			MenuData data = (MenuData)dataProvider.getItem(position);
			selectedData = data;
			data.selected = true;
			dataProvider.notifyDataSetChanged();
			setSelectedData(data, true);
			Popups.hide();
		}
		
	}
	

	private static final List<MenuData> NULL_ARRAY = new ArrayList<MenuData>();
	
	class TreeTopMenu extends SingleMenu{
		//mainlist
		private ListView mainListView;
		
		private MenuData selectedData;
		//数据源
		private AbsListAdapter mainDataPrivider;
		
		public TreeTopMenu(View triggerView,View contentView,int index) {
			super(triggerView, contentView, index);
			mainListView = (ListView)contentView.findViewById(R.id._list_view_left);
			mainListView.setOnItemClickListener(this);
			mainDataPrivider = new AbsListAdapter(getContext(),R.layout._tree_main_item) {
				
				@Override
				protected void initView(View view, Object data) {
					((TreeMainItem)view).setData((MenuData)data);
				}
			};
			mainListView.setAdapter(mainDataPrivider);
		}
		
		@Override
		public void setData(List<MenuData> menuData){
			mainDataPrivider.setData(menuData);
			Point pt = SizeUtil.measureView(mainListView);
			LayoutParams lp = (LayoutParams)list.getLayoutParams();
			lp.height = pt.y;
			list.setLayoutParams(lp);
			for(int i=0,count = menuData.size(); i < count; ++i){
				MenuData data = menuData.get(i);
				if(data.selected){
					selectedData = data;
					setSelectedData(data,false);
					break;
				}
				
			}
		}
		

		private void setSelectedData(MenuData data,boolean fromUser){
			
			if(listener!=null){
				listener.onMenuSelected(index,data,fromUser);
			}
			label.setText(data.topLabel==null ? data.label : data.topLabel);
			
			selectedData = data;
			selectedData.selected = true;
			if(data.children!=null){
				super.setData(data.children);
			}else{
				super.setData(NULL_ARRAY);
			}
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(parent.getAdapter()==mainDataPrivider){
				//反选上一个
				if(selectedData!=null){
					selectedData.selected = false;
				}
				MenuData data = (MenuData)mainDataPrivider.getItem(position);
				if(data.children==null){
					//把右边的清空掉
					dataProvider.setData(new ArrayList<MenuData>());
					setSelectedData(data, true);
					Popups.hide();
				}else{
					dataProvider.setData(data.children);
				}
				
				selectedData = data;
				data.selected = true;
				
				mainDataPrivider.notifyDataSetChanged();
			}else{
				super.onItemClick(parent, view, position, id);
			}
			
		}
		
		
	}

	private int menuTypsResID;

	public TopMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable._top_menu);
	
		menuTypsResID = a.getResourceId(R.styleable._top_menu_menu_types, 0);
		
		
		a.recycle();
	}
	
	

	public TopMenu(Context context) {
		super(context);
	}

	@Override
	protected void onFinishInflate() {
		//查找对应的view
		for(int i=0, count = getChildCount(); i<count; ++i){
			getChildAt(i).setEnabled(false);
		}
		if(isInEditMode())return;
		if(menuTypsResID>0){
			
			LayoutInflater layoutInflater = LayoutInflater.from(getContext());
			TypedArray types = getContext().getResources().obtainTypedArray(menuTypsResID); 
			
			int count = types.length();
			eventHanders = new MenuEventHander[count];
			for(int i=0 ; i < count ; ++i){
				int type = types.getInteger(i, R.integer._menu_single);
				if(type==1){
					View contentView = layoutInflater.inflate(R.layout._popup_menu_view, null);
					SingleMenu singleMenu = new SingleMenu(getChildAt(i), contentView, i);
					eventHanders[i] = singleMenu;
				}else if(type==2){
					View contentView = layoutInflater.inflate(R.layout._popup_tree_menu_view, null);
					TreeTopMenu treeTopMenu = new TreeTopMenu(getChildAt(i), contentView, i);
					eventHanders[i] = treeTopMenu;
				}
				
			}
			types.recycle();
		}
	}

	public MenuEventHander getHander(int index){
		return eventHanders[index];
	}

	private IDataProvider<List<List<MenuData>>> dataProvider;
	
	public void setDataProvider(IDataProvider<List<List<MenuData>>> dataProvider){
		dataProvider.load();
		this.dataProvider = dataProvider;
		dataProvider.setListener(this);
	}
	
	/**
	 * 选中
	 */
	@Override
	public Map<String, Object> getValue() throws FormError {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * 设置默认值
	 */
	@Override
	public void setValue(Map<String, Object> value) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * 属性列表
	 */
	@Override
	public String[] getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void onReceive(List<List<MenuData>> data) {
		for(int i=0, count = getChildCount(); i<count; ++i){
			getChildAt(i).setEnabled(true);
			eventHanders[i].setData(data.get(i));
		}
		
		
	}



	@Override
	public void onError(String error, boolean isNetworkError) {
		for(int i=0, count = getChildCount(); i<count; ++i){
			getChildAt(i).setEnabled(false);
		}
	}
	
	@Override
	protected void onDetachedFromWindow() {
		
		if(dataProvider!=null){
			if(dataProvider instanceof IDestroyable)
			{
				((IDestroyable)dataProvider).destroy();
			}
			dataProvider = null;
		}
		super.onDetachedFromWindow();
	}



	@Override
	public void setOnWidgetValueChangeListener(
			com.citywithincity.interfaces.IWidget.OnWidgetValueChangeListener<Map<String, Object>> listener) {
		
	}
	
	
}
