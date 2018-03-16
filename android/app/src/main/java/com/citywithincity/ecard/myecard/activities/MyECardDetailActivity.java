package com.citywithincity.ecard.myecard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.ReactEnterActivity;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.ecard.models.vos.ECardVo;
import com.citywithincity.ecard.myecard.models.MyECardModel;
import com.citywithincity.ecard.react.ECardReactUtils;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.damai.auto.DMActivity;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.core.DMAccount;
import com.damai.core.DMLib;
import com.damai.helper.a.InitData;
import com.damai.helper.a.Model;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.DetailView;
import com.damai.widget.TitleMenuButton;
import com.damai.widget.TitleMenuButton.TitleMenuListener;

public class MyECardDetailActivity extends DMActivity implements TitleMenuListener{

	@InitData
	private ECardVo data;

	@Res
	private TextView ecard_info_title;
	@Res
	private TitleMenuButton detailMenu;
	
	
	@Model
	private MyECardModel model;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_myecard_ecard_detail);

        DetailView detailView = (DetailView) findViewById(R.id.detailView);
        detailView.getJob().put("cardFlag",data.getCardFlag());

		updateTitle();
		detailMenu.setTitleMenuListener(this);
		ecard_info_title.setText("本查询结果最多显示三个月内20条记录");

		ImageView imageView = (ImageView) findViewById(R.id.biaozhi);
		ImageView cardFlag = (ImageView)findViewById(R.id.realFlag);

		if(data.getCardFlag()==0){
            cardFlag.setVisibility(View.GONE);
		}else{
            imageView.setVisibility(View.GONE);
		}
        if(data.getCreateDate() != null){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.isreal_flag));
        }
	}
	
	@Override
	public void onTitleMenu(int index) {
		switch (index) {
		case 0:
			//解除绑定
			Alert.confirm(this, "确认要解除绑定吗?", new DialogListener() {
				
				@Override
				public void onDialogButton(int id) {
					if(id==R.id._id_ok){
						if(isFinishing()){
							return;
						}
						model.unbind(data.getCardid());
					}
				}
			});
			break;

		case 1:
			//重命名
			startActivityForResult(RenameECardActivity.class, data,REQUEST_CODE_RENAME);
			break;
			case 2:
			{
				ECardUserInfo userInfo = DMAccount.get();
				if(data.getCreateDate() !=null){
					ECardReactUtils.notifyGotoReal(data,true);
                    LifeManager.popupTo(ReactEnterActivity.class);
				}else{
					if(userInfo.isValid()){
						ECardReactUtils.notifyGotoReal(data,true);
                        LifeManager.popupTo(ReactEnterActivity.class);
					}else{
						//查询是否已经实名
                        model.isReal();
					}
				}

			}
				break;
		}
	}

    @Override
    public void onInitView(View view) {
        Button button = (Button) view.findViewById(R.id.show_real);
        button.setText(data.getCreateDate()!=null ? "开通挂失详情" : "开通挂失" );
    }


    @JobSuccess(MyECardModel.is)
	public void onGetIsReal(boolean value){
		//通知到
        ECardReactUtils.notifyGotoReal(data,value);
		LifeManager.popupTo(ReactEnterActivity.class);
		//finish();
	}

	
	private static final int REQUEST_CODE_RENAME = 100;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == REQUEST_CODE_RENAME){
				updateTitle();
			}
		}
	}

	private void updateTitle() {
		if(TextUtils.isEmpty(data.getCardName())){
			setTitle(data.getCardid());
		}else{
			setTitle(data.getCardName());
		}
	}

	
	@JobSuccess(MyECardModel.unbind)
	public void onUnbindSuccess(Object value){
		finish();
	}


}
