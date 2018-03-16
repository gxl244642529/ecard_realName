package com.citywithincity.ecard.discard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.discard.activities.DiscardBuyActivity;
import com.citywithincity.ecard.discard.activities.DiscardInfoActivity;
import com.citywithincity.ecard.discard.vos.BookInfo;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.damai.dl.PluginFragment;
import com.damai.helper.DataHolder;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.Form;
import com.damai.widget.OnSubmitListener;

import java.util.Map;

public class DiscardFragment extends PluginFragment implements OnSubmitListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_discard, container, false);
	}

	/**
	 * 0:未预约 1:未审核 2：已审核 3：审核失败 4：已付款 5：已完成 6：预约失败7:补卡
	 * 
	 * @param json
	 */
	@JobSuccess("book/query")
	public void onQuery(final BookInfo json) {
		switch (json.getStatus()) {
		case 0:
			// 进入填写资料
		{
			json.setLocal(false);
			info(json);
		}
			break;
		case 1:
			// 提示未审核
			Alert.alert(getActivity(), "温馨提示","我们将在2~3个工作日内审核完毕，请耐心等待");
			break;
		case 2:
			// 已经审核,应该去买卡
			Alert.alert(getActivity(), "温馨提示", "审核已经通过，您可以在线购卡", new DialogListener() {

				@Override
				public void onDialogButton(int id) {
					buyCard(json);
				}
			});
			break;
		case 3:
		{
			// 审核失败,直接提示
			Alert.confirm(getActivity(), "温馨提示", "审核失败，原因为:"
					+ json.getComment()
					+",是否重新提交资料？",new DialogListener() {

				@Override
				public void onDialogButton(int id) {
					if(id==R.id._id_ok){
						info(json);
					}
				}
			});
		}
			break;
		case 4:
			// 已经付款
			Alert.alert(getActivity(), "温馨提示", "本预约已经付款，请耐心等待发货");
			break;
		case 5:
			// 已经完成
			Alert.alert(getActivity(), "温馨提示", "本预约已经完成，请等待收货");
			break;
		case 6:
			// 预约失败
			Alert.alert(getActivity(), "温馨提示", "预约失败");
			break;
		case 7:
			// 补卡
			Alert.confirm(getActivity(), "温馨提示", "您已经办理过"+json.getDiscardCardName()+"，请确认是否补办一张新卡", new DialogListener() {

				@Override
				public void onDialogButton(int id) {
					if(id==R.id._id_ok){
						info(json);
					}
				}
			}).setOkText("补办卡").setCancelText("不用了");
			break;
		default:
			break;
		}
	}
	
	/**
	 * 去买卡
	 * @param data
	 */
	protected void buyCard(BookInfo data) {
		DataHolder.set(DiscardBuyActivity.class,data);
		startActivity(new Intent(getActivity(),DiscardBuyActivity.class));
	}

	/**
	 * 去填写资料
	 * @param data
	 */
	private void info(BookInfo data){
		DataHolder.set(DiscardInfoActivity.class,data);
		startActivity(new Intent(getActivity(),DiscardInfoActivity.class));
	}

    @Override
    public boolean shouldSubmit(Form formView, Map<String, Object> data) {
        String idCard = (String) data.get("idCard");
        if(idCard.length()!=18 && idCard.length()!=15){
            Alert.alert(getActivity(),"请输入正确长度的身份证号码");

            return false;
        }


        return true;
    }
}
