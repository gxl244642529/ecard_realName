package com.citywithincity.ecard.selling.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.SDCardUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by renxueliang on 16/12/1.
 */

public class DiyUtils {


    public static byte[] getDiy(Context activity, String path) throws IOException {

        File tempFile = new File(SDCardUtil.getSDCardDir(activity,"png"), System.currentTimeMillis() + ".jpg");

        Bitmap bm = BitmapFactory.decodeFile(path);
        bm.compress(Bitmap.CompressFormat.JPEG, 95, new FileOutputStream(tempFile));
        byte[] bytes = IoUtil.readBytes(tempFile);
        tempFile.delete();

        return bytes;

    }



}
