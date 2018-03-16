package com.citywithincity.ecard.nfc;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.TagTechnology;

import com.citywithincity.ecard.ReactEnterActivity;
import com.citywithincity.ecard.react.NfcModule;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.Alert;
import com.damai.core.ILife;
import com.jzoom.nfc.DepTagAdapter;
import com.jzoom.nfc.NfcException;
import com.jzoom.nfc.NfcListener;
import com.jzoom.nfc.NfcModel;
import com.jzoom.nfc.impl.CpuCard;
import com.jzoom.nfc.impl.DefaultDepTagAdapter;

import java.io.IOException;

/**
 * Created by renxueliang on 2017/10/20.
 */

public class ECardNfcModel extends NfcModel implements NfcListener{



    private static ECardNfcModel ecardNfcModel;

    protected DepTagAdapter adapter;

    private NfcListener listener;

    private CpuCard cpuCard = new CpuCard();

    public ECardNfcModel(Activity activity){
        super(activity, IsoDep.class);
        super.setListener(this);
        if(activity instanceof ReactEnterActivity){
            ecardNfcModel = this;
            if(NfcModule.getRunningInstance()!=null){
                this.setListener(  NfcModule.getRunningInstance() );
            }
        }else{

            if(activity instanceof IViewContainer){
                ((IViewContainer)activity).addLife(new ILife() {
                    @Override
                    public void onResume(IViewContainer iViewContainer) {
                        ECardNfcModel.this.onResume(iViewContainer.getActivity());
                    }

                    @Override
                    public void onPause(IViewContainer iViewContainer) {
                        ECardNfcModel.this.onPause(iViewContainer.getActivity());
                    }

                    @Override
                    public void onNewIntent(Intent intent, IViewContainer iViewContainer) {
                        ECardNfcModel.this.onNewIntent(intent);
                    }

                    @Override
                    public void onDestroy(IViewContainer iViewContainer) {
                        ECardNfcModel.this.destroy();
                    }
                });
            }


        }



    }

    /**
     * 可能为空的
     * @return
     */
    public static ECardNfcModel getRunningInstance(){
        return ecardNfcModel;
    }



    @Override
    public void setListener(NfcListener listener) {
        this.listener = listener;


    }

    private CardReader cardReader;


    private DepTagAdapter paddingRequest;

    @Override
    public void onNfcEvent(TagTechnology tag) {
        if(adapter!=null){
            adapter.close();
        }
        if(tag instanceof  IsoDep){
            adapter = new DefaultDepTagAdapter( (IsoDep)tag);
            //通知感知到了
            try {
                adapter.connect();
                cardReader=selectFile();
                if(listener==null){
                    //padding
                    paddingRequest = adapter;

                }else{
                    listener.onNfcEvent(tag);
                }

            } catch (IOException e) {
                Alert.showShortToast("请重新贴卡");
            } catch (NfcException e) {
                Alert.showShortToast("请重新贴卡");
            } catch (Throwable e){
                e.printStackTrace();
            }

        }
    }
    protected void selectFile(DepTagAdapter adapter, boolean byName, String file) throws IOException, NfcException {
        if(file.length()%2>0){
            throw new  RuntimeException("文件标识或者名称长度必须为偶数");
        }

        adapter.send(String.format("00a4%s00%02x%s",byName ? "04":"00",file.length()/2,file));
    }
    private CardReader selectFile() throws IOException, NfcException {
        try{
            //交通不卡
            selectFile(adapter,true, "A000000632010105");
            return new TransCardReader();
        }catch (NfcException e){
            //cpu卡
            selectFile(adapter,false, "3f00");
            return new CpuCardReader();
        }
    }


    public String getCardId() throws IOException, NfcException {
        if(cardReader==null || adapter==null){
            throw new IOException();
        }
        return cardReader.getCardId(cpuCard,adapter);
    }


    /**
     *
     * @return
     * @throws IOException
     * @throws NfcException
     */
    public CardReader.ChargeInfo getOtherRechargeInfo() throws IOException, NfcException {
        if(cardReader==null || adapter==null){
            throw new IOException();
        }
        //0015
        return cardReader.getOtherRechargeInfo(cpuCard,adapter);

    }



    public void close() {
        if(adapter!=null){
            adapter.close();
        }
    }



    public String apdu(String apdu) throws IOException, NfcException {
        if(adapter!=null){
            return adapter.send(apdu).getStr();
        }
        throw new IOException();
    }



    public NfcResult getCardInfo() throws IOException, NfcException {

       return cardReader.getAll(cpuCard,adapter);





    }
}
