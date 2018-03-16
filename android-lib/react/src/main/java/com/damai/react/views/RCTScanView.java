package com.damai.react.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.damai.auto.LifeManager;
import com.damai.react.R;
import com.damai.react.camera.CameraManager;
import com.damai.react.camera.decoding.CaptureActivityHandler;
import com.damai.react.camera.decoding.InactivityTimer;
import com.damai.react.camera.interfaces.IScanActivity;
import com.damai.react.camera.view.ViewfinderView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by renxueliang on 16/11/12.
 */

public class RCTScanView extends FrameLayout implements IScanActivity,Callback {
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;



    private static final long VIBRATE_DURATION = 200L;
    /**
     * 需要被重写
     */

    protected Vector<BarcodeFormat> decodeFormats;
    protected String characterSet;
    protected CaptureActivityHandler handler;
    protected ViewfinderView viewfinderView;
    SurfaceView surfaceView ;
    public RCTScanView(Context context) {
        super(context);



    }

    @Override
    protected void onDetachedFromWindow() {



        stopSession();
        inactivityTimer.shutdown();
        this.removeView(viewfinderView);
        this.removeView(surfaceView);
        viewfinderView = null;
        surfaceView = null;
        super.onDetachedFromWindow();
    }


    public void startSession(){
        Context context = getContext();
        surfaceView = new SurfaceView(context);
        this.addView(surfaceView,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

        CameraManager.init(context);
        viewfinderView = new ViewfinderView(context);
        addView(viewfinderView,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        hasSurface = false;
        inactivityTimer = new InactivityTimer();
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            try {
                initCamera(surfaceHolder);
            } catch (Throwable e) {
                MessageUtil.sendMessage(new MessageUtil.IMessageListener() {

                    @Override
                    public void onMessage(int message) {
                        Alert.alert(LifeManager.getActivity(), "温馨提示", "无摄像头权限",
                                new DialogListener() {
                                    @Override
                                    public void onDialogButton(int id) {
                                        if (id == R.id._id_ok) {

                                        }
                                    }
                                });
                    }
                });

                return;
            }
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getContext().getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }


    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            LifeManager.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }
    public void stopSession(){
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    private void initCamera(SurfaceHolder surfaceHolder) throws IOException {
        CameraManager.get().openDriver(surfaceHolder);
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }


    @Override
    public Handler runningHandler() {
        return handler;
    }

    @Override
    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        sendDecodeData(obj, barcode);
    }

    /**
     * 重新启动
     */
    protected void restart() {
        if (handler != null) {
            handler.quitSynchronously();
        }
        handler = new CaptureActivityHandler(this, decodeFormats, characterSet);

    }



    protected void sendDecodeData(Result obj, Bitmap barcode) {
      /*  Intent intent = new Intent();
        intent.putExtra("result", obj.getText());
        intent.putExtra("format", obj.getBarcodeFormat().getName());
        setResult(RESULT_OK, intent);
        finish();*/

        WritableMap event = Arguments.createMap();
        event.putString("code", obj.getText());
        event.putString("type", obj.getBarcodeFormat().toString());
        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                "topChange",
                event);
    }

    protected void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    @Override
    public void setResult(int resultOk, Intent obj) {

    }


    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    @Override
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            try {
                initCamera(holder);
            } catch (Throwable e) {
                e.printStackTrace();
                MessageUtil.sendMessage(new MessageUtil.IMessageListener() {

                    @Override
                    public void onMessage(int message) {
                        Alert.alert(LifeManager.getActivity(), "温馨提示", "无摄像头权限",
                                new DialogListener() {
                                    @Override
                                    public void onDialogButton(int id) {
                                        if (id == R.id._id_ok) {

                                        }
                                    }
                                });
                    }
                });

            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }
}
