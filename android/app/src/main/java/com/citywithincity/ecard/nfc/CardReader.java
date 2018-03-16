package com.citywithincity.ecard.nfc;

import com.jzoom.nfc.DepTagAdapter;
import com.jzoom.nfc.NfcException;
import com.jzoom.nfc.impl.CpuCard;

import java.io.IOException;

/**
 * Created by renxueliang on 2017/10/20.
 */

public interface CardReader {
    int getBalance(CpuCard card, DepTagAdapter adapter) throws IOException,NfcException;

    public static class ChargeInfo{
        public String file05;
        public String file15;
        public String random;

    }


    String getCardId(CpuCard card, DepTagAdapter adapter) throws IOException,NfcException;

    NfcResult getAll(CpuCard card, DepTagAdapter adapter) throws IOException,NfcException;

    ChargeInfo getOtherRechargeInfo(CpuCard card, DepTagAdapter adapter) throws IOException,NfcException;


}
