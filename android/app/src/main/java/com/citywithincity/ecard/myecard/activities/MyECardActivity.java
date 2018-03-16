package com.citywithincity.ecard.myecard.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.base.BaseActivity;
import com.citywithincity.ecard.models.vos.ECardVo;
import com.citywithincity.ecard.myecard.models.MyECardModel;
import com.citywithincity.ecard.nfc.ECardNfcModel;
import com.citywithincity.ecard.ui.activity.MyECardScanActivity;
import com.citywithincity.ecard.utils.MyECardUtil;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.utils.Alert;
import com.damai.helper.a.Event;
import com.damai.helper.a.Model;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.StateListView;
import com.jzoom.nfc.NfcException;
import com.jzoom.nfc.NfcListener;

import java.io.IOException;

/**
 * 选择e通卡
 * @author renxueliang
 *
 */
public class MyECardActivity extends BaseActivity implements IOnItemClickListener<ECardVo>,NfcListener {
	
	
	private boolean selectMode;
	
	@Res
	StateListView<ECardVo> _list_view;

	private ECardNfcModel nfcModel;

    @Model
    private MyECardModel model;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_my_ecard);
		StateListView<ECardVo> listView = _list_view;
		listView.getJob().put("last", 0);
		listView.getJob().put("moneyCard",true);
		listView.getJob().execute();
		listView.setOnItemClickListener(this);
		String action = getIntent()!=null ? getIntent().getAction() : null;
		if(action!=null){
			setTitle("选择e通卡");
			selectMode = true;
		}else{
			setTitle("我的e通卡");
			selectMode = false;
		}

		nfcModel = new ECardNfcModel(this);
		nfcModel.setListener(this);
	}
	
	private boolean updateSuccess;

	@JobSuccess({"ecard/bind",MyECardModel.unbind,"ecard/update",MyECardModel.bindBarcode})
	public void onRenameSuccess(Object value){
		updateSuccess = true;
        if(resumed){
            _list_view.refreshWithState();
            updateSuccess = false;
        }

	}
	
	//@Event
	public void onScan(){
		startActivity(MyECardScanActivity.class);
	}

    private boolean resumed;


    @Override
    protected void onPause() {
        resumed = false;
        super.onPause();
    }

    @Override
	protected void onResume() {
		if(updateSuccess){
			_list_view.refreshWithState();
			updateSuccess = false;
		}
        resumed = true;
		super.onResume();
	}

	@Event
	public void id_bind(){
		startActivity(new Intent(this,BindECardActivity.class));
	}
	
	@Override
	public void onItemClick(Activity activity, ECardVo data, int position) {
		if(selectMode){
			MyECardUtil.setResult(this,data.getCardid());
		}else{
			startActivity(MyECardDetailActivity.class,data);
		}
	}


    @Override
    public void onNfcEvent(TagTechnology tag) {

        //

        try {
            final String cardId = nfcModel.getCardId();
            Alert.confirm(this, "确定要绑定卡号"+cardId+"吗", new DialogListener() {
                @Override
                public void onDialogButton(int i) {
                    if(i==R.id._id_ok){
                        model.bindCard(cardId,"");
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NfcException e) {
            e.printStackTrace();
        }


    }
}
