package com.damai.react.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.damai.react.QrUtil;
import com.google.zxing.WriterException;

/**
 * Created by renxueliang on 16/12/5.
 */

public class RCTQrView extends ImageView {

    private String content;

    public RCTQrView(Context context) {
        super(context);
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
        //
        if(!content.equals(this.content)){
            try {
                Bitmap bitmap = QrUtil.createQRImage(content,500,500);
                setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            this.content = content;

        }
    }
}
