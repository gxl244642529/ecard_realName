package com.citywithincity.ecard.react.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;

/**
 * Created by renxueliang on 17/3/24.
 */

public class RCTBusQrView extends ImageView {

    private String content;

    private int imgWidth;
    private int imgHeight;

    public RCTBusQrView(Context context) {
        super(context);
    }


    public void setImgWidth(int imgWidth){
        this.imgWidth = imgWidth;
    }


    public void setImgHeight(int imgHeight){
        this.imgHeight = imgHeight;
    }

    /**
     *
     * @param content
     */
    public void setContent(String content) {
        if(TextUtils.isEmpty(content)){
            this.content = content;
            setImageBitmap(null);
            return;
        }

//        if(!content.equals(this.content)){
//            if(imgWidth < 500){
//                imgWidth = 500;
//            }
//            if(imgHeight < 500){
//                imgHeight = 500;
//            }
//            ByteArrayOutputStream stream = QRCode.from(content).withSize(imgWidth, imgHeight).stream();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(),0,stream.toByteArray().length);
//            setImageBitmap(bitmap);
//            this.content = content;
//
//        }
    }
}
