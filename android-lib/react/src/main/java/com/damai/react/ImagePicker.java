package com.damai.react;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ImageUtil;
import com.citywithincity.utils.SDCardUtil;
import com.citywithincity.widget.ActionSheet;
import com.damai.error.UnexpectedException;
import com.damai.helper.ActivityResult;
import com.damai.util.AlbumUtil;
import com.damai.util.CameraUtil;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by renxueliang on 16/11/8.
 */
public class ImagePicker extends ReactContextBaseJavaModule {


    public ImagePicker(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ImagePickerModule";
    }

    @ReactMethod
    public void select(final ReadableMap data, final Callback callback){
        boolean hasAlbum = false;
        if(data.hasKey("album")){
            hasAlbum = data.getBoolean("album");
        }
        if(hasAlbum){
            new ActionSheet(getCurrentActivity(),new String[]{"从图库选择","拍照"}).setOnActionSheetListener(new ActionSheet.OnActionSheetListener() {
                @Override
                public void onActionSheet(int index) {
                    if(index==0){
                        AlbumUtil.start(getCurrentActivity(), new ActivityResult() {
                            @Override
                            public void onActivityResult(Intent intent, int resultCode, int requestCode) {
                                if(resultCode== Activity.RESULT_OK){
                                    try {
                                        invoke(data,callback,new File(AlbumUtil.getPath(getCurrentActivity(),intent)));
                                    } catch (UnexpectedException e) {
                                        Alert.alert(getCurrentActivity(),"未取到图片，请确认是否开启了相关权限");
                                    } catch (IOException e) {
                                        Alert.alert(getCurrentActivity(),"保存图片失败，请检查sd卡空间是否足够");
                                    }
                                }
                            }
                        });
                    }else{
                        //临时文件
                        trySelectCamera(data,callback);

                    }
                }
            }).setTitle("选择图片").show();
        }else{
            trySelectCamera(data,callback);
        }
    }

    private void trySelectCamera(ReadableMap data, Callback callback){
        try {
            selectCamera(data,callback);
        } catch (IOException e) {
            Alert.alert(getCurrentActivity(),"文件打开失败");
        }
    }


    private void invoke(ReadableMap data,Callback callback,File file) throws IOException {

        //是否需要压缩
        int width =data.hasKey("width") ? data.getInt("width") : 640;
        int height = data.hasKey("height") ?  data.getInt("height") : 640;
        double quality = data.hasKey("quality") ? data.getDouble("quality") : 1;

        //压缩图片
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        bitmap = ImageUtil.resize(bitmap,width,height);
        File temp = SDCardUtil.getTempImageFile(getCurrentActivity());
        ImageUtil.saveAs(bitmap,temp, (int) (quality * 100));
        file = temp;

        callback.invoke("file://"+file.getAbsolutePath());
    }

    private void selectCamera(final ReadableMap data, final Callback callback) throws IOException {
        final File temp = SDCardUtil.getTempImageFile(getCurrentActivity());

        Acp.getInstance(getCurrentActivity()).request(new AcpOptions.Builder()
                .setPermissions("android.permission.CAMERA").build(), new AcpListener() {
            @Override
            public void onGranted() {
                CameraUtil.start(getCurrentActivity(),temp, new ActivityResult() {
                    @Override
                    public void onActivityResult(Intent intent, int resultCode, int requestCode)  {
                        if(resultCode== Activity.RESULT_OK){
                            try {
                                invoke(data,callback,temp);
                            } catch (IOException e) {
                                Alert.alert(getCurrentActivity(),"保存图片失败，请检查sd卡空间是否足够");
                            }
                        }
                    }
                });
            }
            @Override
            public void onDenied(List<String> permissions) {
                Alert.alert(getCurrentActivity(),"请打开摄像头权限");
            }
        });

    }
}
