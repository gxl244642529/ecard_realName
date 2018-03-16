package com.damai.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.json.JSONObject;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.damai.core.DMLib;
import com.damai.core.DMServers;
import com.damai.dmlib.ExceptionHandler;
import com.damai.dmlib.LibBuildConfig;
import com.damai.helper.a.DataType;
import com.damai.util.StrKit;

/**
 * 用来描述一个视图信息，
 * 视图id、视图类型
 * 第一次初始化以后，常驻内存
 * @author Randy
 *
 */
public abstract class ViewInfo implements IViewInfo {
	@SuppressWarnings("rawtypes")
	protected ValueGetter field;
	/**
	 * 视图id
	 */
	public int id;
	
	
	public ViewInfo(Field  field,int id) {
		this.id = id;
		this.field = new FieldValueGetter(field);
	}

	public ViewInfo(Method method, int id) {
		this.id = id;
		this.field = new MethodValueGetter(method);
	}
	public ViewInfo(String name, int id,boolean isImage) {
		this.id = id;
		if(isImage){
			this.field = new JsonValueImageGetter(name);
		}else{
			this.field = new JsonValueGetter(name);
		}
		
	}
	public String getName(){
		return field.getName();
	}
	
	
	
	private static ViewInfoFactory defaultViewInfoFactory;
	
	public static void setFactory(ViewInfoFactory factory){
		defaultViewInfoFactory = factory;
	}
	
	public static interface ViewInfoFactory{
		/**
		 * 使用指定的type，需要在DataType这个标注中指定
		 * 创建ViewInfo
		 * @param type
		 * @param id
		 * @return
		 */
		ViewInfo createViewInfo(int type,Method method,View view);
		ViewInfo createViewInfo(int type,Field method,View view);
		
		/**
		 * 基于json的设置器
		 * @param name
		 * @param view
		 * @return
		 */
		ViewInfo createViewInfo(String name,View view);
	}
	
	
	/**
	 * 从一个实体类或者JsonObject中取出值
	 * @author Randy
	 *
	 */
	private static interface ValueGetter<T>{
		Object getValue(T data) throws Exception;
		String getName();
	}
	
	private static class FieldValueGetter implements ValueGetter<Object>{
		private Field field;
		public FieldValueGetter(Field field){
			this.field = field;
		}
		
		@Override
		public Object getValue(Object data) throws Exception {
			return field.get(data);
		}

		@Override
		public String getName() {
			
			return field.getName();
		}
	}
	private static class MethodValueGetter implements ValueGetter<Object>{
		private Method field;
		public MethodValueGetter(Method field){
			this.field = field;
		}
		
		@Override
		public Object getValue(Object data) throws Exception {
			return field.invoke(data);
		}

		@Override
		public String getName() {
			
			return field.getName();
		}
	}
	
	
	private static class JsonValueImageGetter implements ValueGetter<JSONObject>{
		private String name;
		public JsonValueImageGetter(String name){
			this.name = name;
		}
		@Override
		public Object getValue(JSONObject data) throws Exception {
			return DMServers.getImageUrl(0, (String)data.get(name));
		}
		@Override
		public String getName() {
			return name;
		}
		
	}
	
	
	private static class JsonValueGetter implements ValueGetter<JSONObject>{
		private String name;
		public JsonValueGetter(String name){
			this.name = name;
		}
		@Override
		public Object getValue(JSONObject data) throws Exception {
			if(data.isNull(name)){
				return null;
			}
			return String.valueOf(data.get(name));
		}
		@Override
		public String getName() {
			return name;
		}
		
	}
	
	
	private static class BgDataSetter implements DataSetter{
		@SuppressWarnings("rawtypes")
		private ValueGetter field;
		private View view;
		@SuppressWarnings("rawtypes")
		public BgDataSetter(ValueGetter field,View view){
			this.field = field;
			this.view = view;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object data) {
			try {
				view.setBackgroundColor((Integer)field.getValue(data));
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			} 
		}
		
	}
	private static class BgResDataSetter implements DataSetter{
		@SuppressWarnings("rawtypes")
		private ValueGetter field;
		private View view;
		@SuppressWarnings("rawtypes")
		public BgResDataSetter(ValueGetter field,View view){
			this.field = field;
			this.view = view;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object data) {
			try {
				view.setBackgroundResource((Integer)field.getValue(data));
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			} 
		}
		
	}

	private static class CheckBoxDataSetter implements DataSetter{
		@SuppressWarnings("rawtypes")
		private ValueGetter field;
		private CheckBox checkBox;
		@SuppressWarnings("rawtypes")
		public CheckBoxDataSetter(ValueGetter field,CheckBox checkBox){
			this.field = field;
			this.checkBox = checkBox;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object data) {
			
			try {
				Boolean value = (Boolean) field.getValue(data);
				if(LibBuildConfig.DEBUG){
					System.out.println("Setter Value: " + field.getName() + ":" + value);
				}
				checkBox.setChecked(value);
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			} 
		}
		
	}
	
	
	private static class TextViewDataSetter implements DataSetter{
		@SuppressWarnings("rawtypes")
		private ValueGetter field;
		private TextView textView;
		@SuppressWarnings("rawtypes")
		public TextViewDataSetter(ValueGetter field,TextView textView){
			this.field = field;
			this.textView = textView;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object data) {
			
			try {
				Object srcData = field.getValue(data);
				if(srcData!=null){
					String value = String.valueOf(srcData);
					if(LibBuildConfig.DEBUG){
						System.out.println("Setter Value: " + field.getName() + ":" + value);
					}
					textView.setText(value);
				}else{
					textView.setText("");
				}
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			} 
		}
		
	}
	
	
	private static class ImageDataSetter implements DataSetter{
		@SuppressWarnings("rawtypes")
		private ValueGetter field;
		private ImageView view;
		
		
		public ImageDataSetter(ValueGetter field,ImageView view){
			this.field = field;
			this.view = view;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object data) {
			try {
				String src = (String)field.getValue(data);
				DMLib.getJobManager().loadImage(src, view);
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			} 
		}
		
	}
	
	
	
	private static class MutilValueDataSetter implements DataSetter{
		@SuppressWarnings("rawtypes")
		private ValueGetter field;
		private IValue view;
		@SuppressWarnings("rawtypes")
		public MutilValueDataSetter(ValueGetter field,IValue view){
			this.field = field;
			this.view = view;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object data) {
			try {
				view.setValue(data);
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			} 
		}
		
		
		
	}
	private static class ValueDataSetter implements DataSetter{
		@SuppressWarnings("rawtypes")
		private ValueGetter field;
		private IValue view;
		@SuppressWarnings("rawtypes")
		public ValueDataSetter(ValueGetter field,IValue view){
			this.field = field;
			this.view = view;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object data) {
			try {
				view.setValue(field.getValue(data));
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			} 
		}
		
		
		
	}
	
	
	private static class HintDataSetter implements DataSetter{
		@SuppressWarnings("rawtypes")
		private ValueGetter field;
		private TextView view;
		@SuppressWarnings("rawtypes")
		public HintDataSetter(ValueGetter field,TextView view){
			this.field = field;
			this.view = view;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object data) {
			try {
				view.setHint((String) field.getValue(data));
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			} 
		}
	}
	private static class SrcDataSetter implements DataSetter{
		@SuppressWarnings("rawtypes")
		private ValueGetter field;
		private ImageView view;
		@SuppressWarnings("rawtypes")
		public SrcDataSetter(ValueGetter field,ImageView view){
			this.field = field;
			this.view = view;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object data) {
			try {
				view.setImageResource((Integer) field.getValue(data));
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			} 
		}
	}
	private static class VisibleDataSetter implements DataSetter{
		@SuppressWarnings("rawtypes")
		private ValueGetter field;
		private View view;
		@SuppressWarnings("rawtypes")
		public VisibleDataSetter(ValueGetter field,View view){
			this.field = field;
			this.view = view;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object data) {
			try {
				view.setVisibility((Integer) field.getValue(data));
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			} 
		}
	}
	
	private static class BgResInfo extends ViewInfo{
		
		public BgResInfo(Field field, int id) {
			super(field, id);
		}

		public BgResInfo(Method method, int id) {
			super(method, id);
		}

		@Override
		public DataSetter createSetter(View view) {
			return new BgResDataSetter(field,view.findViewById(id));
		}
	}
	
	
	private static class BgInfo extends ViewInfo{
		
		public BgInfo(Field field, int id) {
			super(field, id);
		}

		public BgInfo(Method method, int id) {
			super(method, id);
		}

		@Override
		public DataSetter createSetter(View view) {
			return new BgDataSetter(field,(TextView)view.findViewById(id));
		}
	}
	
	
	
	private static class HintInfo extends ViewInfo{
		public HintInfo(Field field, int id) {
			super(field, id);
		}

		public HintInfo(Method method, int id) {
			super(method, id);
		}

		@Override
		public DataSetter createSetter(View view) {
			return new HintDataSetter(field,(TextView)view.findViewById(id));
		}
	}
	private static class SrcInfo extends ViewInfo{
		public SrcInfo(Field field, int id) {
			super(field, id);
		}

		public SrcInfo(Method method, int id) {
			super(method, id);
		}

		@Override
		public DataSetter createSetter(View view) {
			return new SrcDataSetter(field,(ImageView)view.findViewById(id));
		}
	}
	
	private static class VisibleInfo extends ViewInfo{
		
		public VisibleInfo(Field field, int id) {
			super(field, id);
		}

		public VisibleInfo(Method method, int id) {
			super(method, id);
		}

		@Override
		public DataSetter createSetter(View view) {
			return new VisibleDataSetter(field,view.findViewById(id));
		}
	}
	
	
	
	private static class CheckBoxInfo extends ViewInfo{
		public CheckBoxInfo(Field field, int id) {
			super(field, id);
		}

		public CheckBoxInfo(Method method, int id) {
			super(method, id);
		}
		
		public CheckBoxInfo(String name, int id) {
			super(name, id,false);
		}

		@Override
		public DataSetter createSetter(View view) {
			return new CheckBoxDataSetter(field,(CheckBox)view.findViewById(id));
		}
	}
	
	
	private static class TextViewInfo extends ViewInfo{
		public TextViewInfo(Field field, int id) {
			super(field, id);
		}

		public TextViewInfo(Method method, int id) {
			super(method, id);
		}
		
		public TextViewInfo(String name, int id) {
			super(name, id,false);
		}

		@Override
		public DataSetter createSetter(View view) {
			return new TextViewDataSetter(field,(TextView)view.findViewById(id));
		}
	}
	
	
	private static class ImageViewInfo extends ViewInfo{
		

		public ImageViewInfo(Field field, int id) {
			super(field, id);
		}

		public ImageViewInfo(Method method, int id) {
			super(method, id);
		}
		
		public ImageViewInfo(String name, int id) {
			super(name, id,true);
		}
		@Override
		public DataSetter createSetter(View view) {
			return new ImageDataSetter(field,(ImageView)view.findViewById(id));
		}
	}
	
	
	private static class MutilValueViewInfo extends ViewInfo{
		
		
		public MutilValueViewInfo(Field field, int id) {
			super(field, id);
		}

		public MutilValueViewInfo(Method method, int id) {
			super(method, id);
		}
		public MutilValueViewInfo(String name, int id) {
			super(name, id,false);
		}
		
		
		@Override
		public DataSetter createSetter(View view) {
			return new MutilValueDataSetter(field,(IValue)view.findViewById(id));
		}
	}
	
	private static class ValueViewInfo extends ViewInfo{
		
		
		public ValueViewInfo(Field field, int id) {
			super(field, id);
		}

		public ValueViewInfo(Method method, int id) {
			super(method, id);
		}
		public ValueViewInfo(String name, int id) {
			super(name, id,false);
		}
		
		
		@Override
		public DataSetter createSetter(View view) {
			return new ValueDataSetter(field,(IValue)view.findViewById(id));
		}
	}
	
	
	/**
	 * 
	 * @param name
	 * @param subView
	 * @return
	 */
	public static ViewInfo create(String name,Class<?> fieldClass,View subView){
		if(subView instanceof IValue){
			return new ValueViewInfo(name, subView.getId());
		}else if(subView instanceof TextView){
			if(subView instanceof CheckBox && (fieldClass==Boolean.class || fieldClass==boolean.class)){
				return new CheckBoxInfo(name,subView.getId());
			}
			return new TextViewInfo(  name,subView.getId() );
		}else if(subView instanceof ImageView){
			return new ImageViewInfo(name, subView.getId());
		}else if(subView instanceof IMutilValue){
			return new MutilValueViewInfo(name, subView.getId());
		}
		return defaultViewInfoFactory.createViewInfo(name, subView);
	}
	
	public static ViewInfo create(View view,Method method,BaseDataSetter setter) {
		String name = method.getName();
		if(name.startsWith("get") && name.length() > 3){
			name = StrKit.firstCharToLowerCase(name.substring(3));
		}else if( (name.startsWith("is") && name.length() > 2)){
			name = StrKit.firstCharToLowerCase(name.substring(2));
		}else{
			return null;
		}
		String replace = null;
		if(method.isAnnotationPresent(DataType.class)){
			DataType type = method.getAnnotation(DataType.class);
			switch (type.value()) {
			case DataType.BG_COLOR:
				replace = "_Bg";
				break;
			case DataType.VISIBLE:
				replace = "_Visible";
				break;
			case DataType.SRC:
				replace = "_Src";
				break;
			case DataType.HINT:
				replace = "_Hint";
				break;
			case DataType.BG_RES:
				replace = "_BgRes";
				break;
			}
		}
		if(replace!=null){
			name = name.replace(replace, "");
		}
		View subView = setter.findView(name, view);
		if(subView==null){
			if(LibBuildConfig.DEBUG){
				System.err.println("找不到指定的SubView，id为"+name);
			}
			return null;
		}
		if(method.isAnnotationPresent(DataType.class)){
			DataType type = method.getAnnotation(DataType.class);
			switch (type.value()) {
			case DataType.BG_COLOR:
				return new BgInfo(method, subView.getId());
			case DataType.VISIBLE:
				return new VisibleInfo(method,subView.getId());
			case DataType.SRC:
				return new SrcInfo(method,subView.getId());
			case DataType.HINT:
				return new HintInfo(method,subView.getId());
			case DataType.BG_RES:
				return new BgResInfo(method, subView.getId());
			default:
				return defaultViewInfoFactory.createViewInfo(type.value(),method, subView);
			}
		}
		if(subView instanceof IValue){
			return new ValueViewInfo(method, subView.getId());
		}else if(subView instanceof TextView){
			Class<?> returnType = method.getReturnType();
			if(subView instanceof CheckBox && (returnType==Boolean.class || returnType==boolean.class)){
				return new CheckBoxInfo(method,subView.getId());
			}
			return new TextViewInfo(  method,subView.getId() );
		}else if(subView instanceof ImageView){
			return new ImageViewInfo(method, subView.getId());
		}else if(subView instanceof IMutilValue){
			return new MutilValueViewInfo(name, subView.getId());
		}
		if(LibBuildConfig.DEBUG){
			System.err.println("非支持的视图类型:" + name + " type:" + subView.getClass().getName());
		}
		return null;
	}
	
	
	public static ViewInfo create(Field field, View subView) {
		if(field.isAnnotationPresent(DataType.class)){
			DataType type = field.getAnnotation(DataType.class);
			switch (type.value()) {
			case DataType.BG_COLOR:
				return new BgInfo(field, subView.getId());
			case DataType.VISIBLE:
				return new VisibleInfo(field,subView.getId());
			case DataType.BG_RES:
				return new BgResInfo(field,subView.getId());
			default:
				return defaultViewInfoFactory.createViewInfo(type.value(),field, subView);
			}
		}
		//有可能是继承基本view
		if(subView instanceof IValue){
			return new ValueViewInfo(field, subView.getId());
		}else if(subView instanceof TextView){
			Class<?> fieldClass = field.getType();
			if(subView instanceof CheckBox && (fieldClass==Boolean.class || fieldClass==boolean.class)){
				return new CheckBoxInfo(field,subView.getId());
			}
			//包含textView
			return new TextViewInfo(field,subView.getId());
		}else if(subView instanceof ImageView){
			return new ImageViewInfo(field, subView.getId());
		}
		return null;
	}
/*
	public static IViewInfo createItemEventDataSetter(Method method,int itemId) {
		return new ItemEventViewInfo(method,itemId);
	}*/
}
