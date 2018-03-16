package com.damai.alert;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.models.LocalData;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.Alert.ICDCDialogListener;
import com.citywithincity.utils.Alert.IDialog;
import com.citywithincity.utils.Alert.IInputListener;
import com.citywithincity.utils.Alert.IOnSelect;
import com.citywithincity.utils.Alert.IOnSelectData;
import com.citywithincity.utils.KeyboardUtil;
import com.damai.lib.R;
public class AlertImpl implements IAlert {

	
	/**
	 * 显示自定义的视图
	 * 
	 * @param context
	 * @param contentView
	 * @param title
	 * @param type
	 * @param listener
	 */
	public IDialog popup(Context context, View contentView,
			String title, int type, DialogListener listener) {

		MyDialog myDialog = new MyDialog(context);
		myDialog.setTitle(title);
		myDialog.setContentView(contentView);
		myDialog.setType(type);
		myDialog.setDialogListener(listener);
		myDialog.show();

		return myDialog;
	}
	@Override
	public void alert(Context context,String message) {
		if (context instanceof Activity) {
			if (((Activity) context).isFinishing()) {
				return;
			}
		}
		MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.setTitle(message);
		dialog.setMessage(null);
		dialog.show();
	}
	public void alert(Context context, String title, String message) {
		if (context instanceof Activity) {
			if (((Activity) context).isFinishing()) {
				return;
			}
		}
		MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.show();
	}
	public void confirmCheck(Context context, int dialogId,
			int okTextId, String title, int resID, ICDCDialogListener listener) {
		
		
		
		
		if (LocalData.read().getBoolean(
				ConfirmDialogWithCheck.SP_KEY + dialogId, false)) {
			// 直接
			listener.onCDCDialogEvent(R.id._id_ok, dialogId);
			return;
		}

		ConfirmDialogWithCheck check = new ConfirmDialogWithCheck(context,
				dialogId, okTextId, listener);
		check.setContentView(resID);
		check.setTitle(title);
		check.show();

	}
	
	/**
	 * 弹出对话框
	 * 
	 * @param context
	 * @return
	 */
	public IDialog alert(Context context, String title, String message,
			DialogListener listener) {
		MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.setTitle(title);
		dialog.setDialogListener(listener);
		dialog.setMessage(message);
		dialog.show();

		return dialog;
	}
	
	public IDialog dialog(Context context, int type) {
		MyDialog dialog = new MyDialog(context);
		dialog.setType(type);
		return dialog;
	}
	public Dialog rawPopup(Context context, View contentView) {
		Dialog dialog = new Dialog(context, R.style._dialog_center_popup);
		dialog.setContentView(contentView);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

		return dialog;
	}
	public IDialog confirm(Context context, String title,
			String message, DialogListener listener) {
		ConfirmDialog dialog = new ConfirmDialog(context);
		dialog.setMessage(message);
		dialog.setTitle(title);
		dialog.setDialogListener(listener);
		dialog.show();
		return dialog;
	}
	public <T> void showSelect(Context context,
			final List<T> selectData, final IOnSelectData<T> onSelectListener) {
		String[] arr = new String[selectData.size()];
		int i = 0;
		for (T data : selectData) {
			arr[i++] = onSelectListener.getLabel(data);
		}
		Dialog dialog = new AlertDialog.Builder(context)
				.setItems(arr, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						onSelectListener.onSelectData(which,
								selectData.get(which));
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				}).create();
		dialog.show();
	}
	
	public <T> void select(Context context, String title,
			final List<T> selectData, int selectedIndex,
			final IOnSelectData<T> onSelectListener) {

		final ListView listView = new ListView(context);
		final String[] options = new String[selectData.size()];
		int index = 0;
		for (T data : selectData) {
			options[index++] = onSelectListener.getLabel(data);
		}
		listView.setAdapter(new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_single_choice, options));
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		if (selectedIndex >= 0) {
			listView.setItemChecked(selectedIndex, true);
		}

		popup(context, listView, title, Alert.OK, new DialogListener() {

			@Override
			public void onDialogButton(int id) {
				if(id==R.id._id_ok){
					int position = listView.getCheckedItemPosition();
					if(position>=0 && position < selectData.size()){
						onSelectListener.onSelectData(position,selectData.get(position));
					}
				}
				
			}
		});
	}
	
	/**
	 * 选择
	 * 
	 * @param context
	 * @param options
	 * @param onSelectListener
	 */
	public void select(Context context, String title,
			final String[] options, int selectedIndex,
			final IOnSelect<String> onSelectListener) {

		final ListView listView = new ListView(context);
		listView.setAdapter(new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_single_choice, options));
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		if (selectedIndex >= 0) {
			listView.setItemChecked(selectedIndex, true);
		}

		popup(context, listView, title, Alert.OK, new DialogListener() {

			@Override
			public void onDialogButton(int id) {
				int position = listView.getCheckedItemPosition();
				if(position>=0 && position < options.length){
					onSelectListener.onSelectData(position, options[position]);
				}
			}
		});

	}
	public void showSelect(Context context, String[] selectData,
			DialogInterface.OnClickListener onSelectListener) {
		Dialog dialog = new AlertDialog.Builder(context)
				.setItems(selectData, onSelectListener)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				}).create();
		dialog.show();
	}
	/**
	 * 
	 * @param context
	 * @param title
	 * @param dialogType
	 */
	public IDialog input(final Context context, String title,
			int dialogType, String defaultValue, final IInputListener listener) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.cus_dialog_text, null);
		final EditText editText = (EditText) view.findViewById(R.id._edit_text);
		if (!TextUtils.isEmpty(defaultValue)) {
			editText.setText(defaultValue);
		}
		IDialog dialog = popup(context, view, title, dialogType,
				new DialogListener() {
					@Override
					public void onDialogButton(int id) {
						if (id == R.id._id_ok) {
							listener.onInput(editText.getText().toString());
						} else {
							listener.onInputCancel();
						}
						KeyboardUtil.hideSoftKeyboard(context, editText);
					}
				});

		KeyboardUtil.showSoftKeyboard(context, editText);
		return dialog;
	}

}
