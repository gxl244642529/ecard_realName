package com.citywithincity.ecard.nfc;

import com.jzoom.nfc.DepTagAdapter;
import com.jzoom.nfc.NfcException;
import com.jzoom.nfc.impl.CpuCard;

import java.io.IOException;

/**
 * Created by renxueliang on 2017/10/20.
 */

public class TransCardReader extends CpuCardReader{

    @Override
    public String getCardId(CpuCard card, DepTagAdapter adapter) throws IOException, NfcException {
        String file = card.getFile(adapter,0x15);
        return file.substring(21,40);
    }


    @Override
    public int getBalance(CpuCard card, DepTagAdapter adapter) throws IOException, NfcException {
        return card.getBalance(adapter);
    }
    @Override
    public ChargeInfo getOtherRechargeInfo(CpuCard card, DepTagAdapter adapter) throws IOException, NfcException {
        //05
        ChargeInfo info = new ChargeInfo();

        String[] arr = adapter.send(new String[]{"00A4040008A000000632010105,00b0850000",
                "00b0950000","0084000004"});

        info.file05 = arr[0];
        info.file15 = arr[1];
        info.random = arr[2];

        return info;
    }
}
