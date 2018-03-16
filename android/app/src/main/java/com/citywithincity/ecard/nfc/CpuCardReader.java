package com.citywithincity.ecard.nfc;

import com.jzoom.nfc.DepTagAdapter;
import com.jzoom.nfc.NfcException;
import com.jzoom.nfc.impl.CpuCard;
import com.jzoom.nfc.vos.CardTradeVo;

import java.io.IOException;
import java.util.List;

/**
 * Created by renxueliang on 2017/10/20.
 */

public class CpuCardReader implements  CardReader {


    @Override
    public int getBalance(CpuCard card, DepTagAdapter adapter) throws IOException, NfcException {
        selectFile(adapter,false,"3f01");
        return card.getBalance(adapter);
    }

    protected void selectFile(DepTagAdapter adapter, boolean byName, String file) throws IOException, NfcException {
        if(file.length()%2>0){
            throw new  RuntimeException("文件标识或者名称长度必须为偶数");
        }

        adapter.send(String.format("00a4%s00%02x%s",byName ? "04":"00",file.length()/2,file));
    }

    @Override
    public String getCardId(CpuCard card, DepTagAdapter adapter) throws IOException, NfcException {
        String file = card.getFile(adapter,0x05);
        return file.substring(40,56);
    }

    @Override
    public NfcResult getAll(CpuCard card, DepTagAdapter adapter) throws IOException, NfcException {
        String cardId = getCardId(card,adapter);

        //对于cpu卡，余额在3f01
        //对于交通部卡，不需要改变

        int balance = getBalance(card,adapter);
        List<CardTradeVo> logs = card.getTradeLogs(adapter);
        NfcResult result = new NfcResult();
        result.setCardId(cardId);
        result.setCash(String.format("%.2f",  (float)balance/100));
        result.setList(logs);
        return result;
    }

    @Override
    public ChargeInfo getOtherRechargeInfo(CpuCard card, DepTagAdapter adapter) throws IOException, NfcException {

        //05
        ChargeInfo info = new ChargeInfo();

        String[] arr = adapter.send(new String[]{
                "00a40000023f00,00b0850000",
                "00a40000023f01,00b0950000",
                "0084000004"});

        info.file05 = arr[0];
        info.file15 = arr[1];
        info.random = arr[2];

        return info;

    }
}
