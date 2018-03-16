package com.citywithincity.auto.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.auto.tools.AutoCreator.IBinddataViewSetterCreator;
import com.citywithincity.interfaces.IJsonTaskManager;
import com.citywithincity.interfaces.IViewDataSetter;

public class ViewDataSetter implements IViewDataSetter {

	private static Map<Class<?>, Map<Integer, Object[][]>> fieldDataSetterMap = new LinkedHashMap<Class<?>, Map<Integer, Object[][]>>();

	protected static IJsonTaskManager jsonTaskManager;

	public static void setJsonTaskManager(IJsonTaskManager value) {
		jsonTaskManager = value;
	}
	
	
	
	public static IBinddataViewSetterCreator CREATOR = new IBinddataViewSetterCreator() {
		@Override
		public void setBinddataViewValues(Object data, View view, int layoutID) {
			// TODO Auto-generated method stub
			Class<?> clazz = data.getClass();
			if (!clazz.isAnnotationPresent(Databind.class)) {
				//throw new Error("使用databind设置视图数据必须使用Databind标注");
				return;
			}
			IViewDataSetter setter = (IViewDataSetter) view.getTag();
			if (setter == null) {
				setter = createViewDataSetter(clazz, layoutID, view);
				view.setTag(setter);
			}
			try {
				setter.setData(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setBinddataViewValues(Object data, Activity view) {
			Class<?> clazz = data.getClass();
			if (!clazz.isAnnotationPresent(Databind.class)) {
				throw new Error("要使用binddata必须设置Binddata标注");
			}
			Field[] fields = clazz.getFields();
			try {
				for (Field f : fields) {
					if (f.isAnnotationPresent(ViewId.class)) {
						ViewId element = (ViewId) f.getAnnotation(ViewId.class);
						View v = view.findViewById(element.id());
						if (v instanceof CheckBox) {
							Class<?> type = f.getType();
							if (type.getName().equals("int")) {
								((CheckBox) v).setChecked(f.getInt(data) == 1);

							} else {
								((CheckBox) v).setChecked(f.getBoolean(data));
							}

						} else if (v instanceof TextView) {
							Object obj = f.get(data);
							((TextView) v).setText(obj==null?"":obj.toString());

						} else if (v instanceof ImageView) {
							jsonTaskManager.setImageSrc(
									(String) f.get(data), (ImageView) v);
						} else if (v instanceof EditText) {
							((EditText) v).setText(String.valueOf(f.get(data)));
						}
					}
				}

				Method[] methods = clazz.getMethods();
				for (Method m : methods) {
					if (m.isAnnotationPresent(ViewId.class)) {
						ViewId element = m.getAnnotation(ViewId.class);
						View v = view.findViewById(element.id());
						if (v instanceof TextView) {
							((TextView) v).setText(String.valueOf(m.invoke(data)));
						} else if (v instanceof ImageView) {
							jsonTaskManager.setImageSrc(
									(String) m.invoke(data), (ImageView) v);
						} else if (v instanceof EditText) {
							((EditText) v).setText(String.valueOf(m.invoke(data)));
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	public static IViewDataSetter createViewDataSetter(Class<?> dataClass,
			int layoutID, View view) {
		Map<Integer, Object[][]> groupMap = fieldDataSetterMap.get(dataClass);
		if (groupMap == null) {
			groupMap = new LinkedHashMap<Integer, Object[][]>();
			fieldDataSetterMap.put(dataClass, groupMap);
		}

		Object[][] objects = groupMap.get(layoutID);
		if (objects == null) {
			// 分析有哪些
			List<Object[]> fieldsObjects = new ArrayList<Object[]>();
			try {
				Field[] fields = dataClass.getFields();
				for (Field f : fields) {
					if (f.isAnnotationPresent(ViewId.class)) {
						ViewId viewId = f.getAnnotation(ViewId.class);
						if (view.findViewById(viewId.id()) != null) {
							// 记录下来
							fieldsObjects.add(new Object[] { viewId.id(), f,
									false, viewId.type() });
						}
					}
				}

				Method[] methods = dataClass.getMethods();
				for (Method m : methods) {
					if (m.isAnnotationPresent(ViewId.class)) {
						ViewId viewId = m.getAnnotation(ViewId.class);
						if (view.findViewById(viewId.id()) != null) {
							fieldsObjects.add(new Object[] { viewId.id(), m,
									true, viewId.type() });
						}

					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			objects = new Object[fieldsObjects.size()][];
			fieldsObjects.toArray(objects);

			groupMap.put(layoutID, objects);
		}

		return new ViewDataSetter(objects, view);
	}

	private ISubViewDataSetter[] list;

	protected ViewDataSetter(Object[][] objs, View view) {
		list = new ISubViewDataSetter[objs.length];
		int index = 0;
		for (Object[] object : objs) {
			int id = (Integer) object[0];
			int type = (Integer) object[3];
			boolean isMethod = (Boolean) object[2];
			View child = view.findViewById(id);
			switch (type) {
			case ViewId.TYPE_VALUE: {
				if (isMethod) {
					Method method = (Method) object[1];

					if (child instanceof CompoundButton) {
						list[index] = new CompoundButtonMethodDataSetter(
								method, (CompoundButton) child);
					} else if (child instanceof TextView) {
						list[index] = new TextViewMethodDataSetter(method,
								(TextView) child);
					} else if (child instanceof ImageView) {
						list[index] = new ImageViewtMethodDataSetter(method,
								(ImageView) child);
					} else if (child instanceof EditText) {
						list[index] = new EditTextMethodDataSetter(method,
								(EditText) child);
					}

				} else {
					Field field = (Field) object[1];
					if (child instanceof CompoundButton) {
						list[index] = new CompoundButtonFieldDataSetter(field,
								(CompoundButton) child);
					} else if (child instanceof TextView) {
						list[index] = new TextViewFieldDataSetter(field,
								(TextView) child);
					} else if (child instanceof ImageView) {
						list[index] = new ImageViewFieldDataSetter(field,
								(ImageView) child);
					} else if (child instanceof EditText) {
						list[index] = new EditTextFieldDataSetter(field,
								(EditText) child);
					}

				}
				break;
			}
			case ViewId.TYPE_VISIBILITY:
				if (isMethod) {
					list[index] = new VisibleMethodDataSetter(
							(Method) object[1], child);
				} else {
					list[index] = new VisibleFieldDataSetter((Field) object[1],
							child);
				}
				break;
			case ViewId.TYPE_ENABLE:
				if (isMethod) {
					list[index] = new EnableMethodDataSetter(
							(Method) object[1], child);

				} else {
					list[index] = new EnableFieldDataSetter((Field) object[1],
							child);

				}
				break;
			case ViewId.TYPE_BACKGROUND_RESOURCE: {
				if (isMethod) {
					list[index] = new BackgroundResourceMethodDataSetter(
							(Method) object[1], child);

				} else {
					list[index] = new BackgroundResourceFieldDataSetter(
							(Field) object[1], child);

				}
				break;
			}

			}

			index++;
		}
	}

	@Override
	public void setData(Object data) throws Exception {
		for (ISubViewDataSetter setter : list) {
			setter.setData(data);
		}
	}

	private static interface ISubViewDataSetter {
		void setData(Object data) throws Exception;
	}

	/**
	 * 
	 * @author Randy
	 * 
	 */
	private static abstract class ViewFieldDataSetter implements
			ISubViewDataSetter {
		protected final Field field;

		public ViewFieldDataSetter(Field field) {
			this.field = field;
		}
	}

	private static class CompoundButtonFieldDataSetter extends
			ViewFieldDataSetter {
		public CompoundButtonFieldDataSetter(Field field,
				CompoundButton textView) {
			super(field);
			this.textView = textView;
		}

		protected final CompoundButton textView;

		@Override
		public void setData(Object data) throws Exception {
			textView.setChecked((Boolean) field.get(data));
		}
	}

	private static class BackgroundResourceFieldDataSetter extends
			ViewFieldDataSetter {
		public BackgroundResourceFieldDataSetter(Field field, View textView) {
			super(field);
			this.textView = textView;
		}

		protected final View textView;

		@Override
		public void setData(Object data) throws Exception {
			textView.setBackgroundResource((Integer) field.get(data));
		}
	}

	private static class EnableFieldDataSetter extends ViewFieldDataSetter {
		public EnableFieldDataSetter(Field field, View textView) {
			super(field);
			this.textView = textView;
		}

		protected final View textView;

		@Override
		public void setData(Object data) throws Exception {
			textView.setEnabled((Boolean) field.get(data));
		}
	}

	private static class TextViewFieldDataSetter extends ViewFieldDataSetter {
		public TextViewFieldDataSetter(Field field, TextView textView) {
			super(field);
			this.textView = textView;
		}

		protected final TextView textView;

		@Override
		public void setData(Object data) throws Exception {
			Object object = field.get(data);
			textView.setText(object==null?"":object.toString());
		}
	}

	private static class EditTextFieldDataSetter extends ViewFieldDataSetter {
		public EditTextFieldDataSetter(Field field, EditText text) {
			super(field);
			this.textView = text;
		}

		protected final EditText textView;

		@Override
		public void setData(Object data) throws Exception {
			textView.setText(String.valueOf(field.get(data)));
		}
	}

	private static class ImageViewFieldDataSetter extends ViewFieldDataSetter {
		public ImageViewFieldDataSetter(Field field, ImageView imageView) {
			super(field);
			this.imageView = imageView;
		}

		protected final ImageView imageView;

		@Override
		public void setData(Object data) throws Exception {

			jsonTaskManager.setImageSrc((String) field.get(data), imageView);
		}
	}

	private static class CompoundButtonMethodDataSetter extends
			ViewMethodDataSetter {
		public CompoundButtonMethodDataSetter(Method method,
				CompoundButton textView) {
			super(method);
			this.textView = textView;
		}

		protected final CompoundButton textView;

		@Override
		public void setData(Object data) throws Exception {
			textView.setChecked((Boolean) method.invoke(data));
		}
	}

	private static class EditTextMethodDataSetter extends ViewMethodDataSetter {
		public EditTextMethodDataSetter(Method method, EditText text) {
			super(method);
			this.textView = text;
		}

		protected final EditText textView;

		@Override
		public void setData(Object data) throws Exception {
			textView.setText((String) method.invoke(data));
		}
	}

	private static class ImageViewtMethodDataSetter extends
			ViewMethodDataSetter {
		public ImageViewtMethodDataSetter(Method method, ImageView imageView) {
			super(method);
			this.imageView = imageView;
		}

		protected final ImageView imageView;

		@Override
		public void setData(Object data) throws Exception {
			jsonTaskManager.setImageSrc((String) method.invoke(data), imageView);
		}
	}

	private static abstract class ViewMethodDataSetter implements
			ISubViewDataSetter {
		protected final Method method;

		public ViewMethodDataSetter(Method method) {
			this.method = method;
		}
	}

	private static class VisibleMethodDataSetter extends ViewMethodDataSetter {
		public VisibleMethodDataSetter(Method method, View textView) {
			super(method);
			this.textView = textView;
		}

		protected final View textView;

		@Override
		public void setData(Object data) throws Exception {
			textView.setVisibility((Integer) method.invoke(data));
		}
	}

	private static class BackgroundResourceMethodDataSetter extends
			ViewMethodDataSetter {
		public BackgroundResourceMethodDataSetter(Method method, View textView) {
			super(method);
			this.textView = textView;
		}

		protected final View textView;

		@Override
		public void setData(Object data) throws Exception {
			textView.setBackgroundResource((Integer) method.invoke(data));
		}
	}

	private static class EnableMethodDataSetter extends ViewMethodDataSetter {
		public EnableMethodDataSetter(Method method, View textView) {
			super(method);
			this.textView = textView;
		}

		protected final View textView;

		@Override
		public void setData(Object data) throws Exception {
			textView.setEnabled((Boolean) method.invoke(data));
		}
	}

	private static class TextViewMethodDataSetter extends ViewMethodDataSetter {
		public TextViewMethodDataSetter(Method method, TextView textView) {
			super(method);
			this.textView = textView;
		}

		protected final TextView textView;

		@Override
		public void setData(Object data) throws Exception {
			String result = (String) method.invoke(data);
			
			textView.setText(result==null?"":result);
		}
	}

	private static class VisibleFieldDataSetter extends ViewFieldDataSetter {
		public VisibleFieldDataSetter(Field field, View textView) {
			super(field);
			this.textView = textView;
		}

		protected final View textView;

		@Override
		public void setData(Object data) throws Exception {
			textView.setVisibility((Integer) field.get(data));
		}
	}

}
