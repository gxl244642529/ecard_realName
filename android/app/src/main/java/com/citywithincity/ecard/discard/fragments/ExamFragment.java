package com.citywithincity.ecard.discard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.discard.activities.ExamInfoActivity;
import com.citywithincity.ecard.discard.vos.ExamInfo;
import com.citywithincity.ecard.models.TianYu;
import com.citywithincity.ecard.widget.ECardSelectView;
import com.citywithincity.ecard.widget.ECardSelectView.ECardSelectViewListener;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.damai.dl.PluginFragment;
import com.damai.helper.DataHolder;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.FormSubmit;
import com.damai.widget.SubmitButton;
public class ExamFragment extends PluginFragment implements
		ECardSelectViewListener {

	@Res
	private ECardSelectView ecardSelectView;

	@Res
	private SubmitButton _id_ok;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_exam, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ecardSelectView.setListener(this);
        MessageUtil.sendMessage(10,new MessageUtil.IMessageListener() {
            @Override
            public void onMessage(int i) {
                _id_ok.getJob().setTimeoutMS(30000);
            }
        });

	}

	@JobSuccess("exam/query")
	public void onQuerySuccess(final ExamInfo info) {
		switch (info.getStatus()) {
		case 0:
			info.setLocal(false);
			Alert.confirm(getActivity(), "温馨提示", "本卡已经审核完成,确认是否现在写卡?",
					new DialogListener() {
						@Override
						public void onDialogButton(int id) {
							if (id == R.id._id_ok) {
								TianYu.startExam(getActivity());
							}
						}
					});
			break;
		case 1:
			Alert.alert(getActivity(), "本卡年审已经完成");
			break;
		case 2:
			Alert.showShortToast("查询成功");
			info(info);
			break;
		case 3:
			Alert.alert(getActivity(), "本卡资料已经提交，正在进行人工审核，请耐心等待");
			break;
		case 4:
			Alert.alert(getActivity(), "温馨提示", "审核没有通过，请重新提交资料",
					new DialogListener() {

						@Override
						public void onDialogButton(int id) {
							if (id == R.id._id_ok) {
								info(info);
							}
						}
					});
			break;
		case 5:
			Alert.alert(getActivity(), "年审失败，请去e通卡营业网点办理年审");
			break;
		case 9:
			Alert.showShortToast("查询成功");
			info(info);
			break;
		default:
			break;
		}

	}

	@Override
	public void onSelectECard(String cardId) {
		_id_ok.submit(FormSubmit.SubmitPolicy_All);
	}

	private void info(ExamInfo info) {
		DataHolder.set(ExamInfoActivity.class,info);
		startActivity(new Intent(getActivity(), ExamInfoActivity.class));
	}

	/**
	 * 为其他人年审
	 */
	//@Event
	public void onOtherExam() {
		TianYu.startExam(getActivity());
	}

}
