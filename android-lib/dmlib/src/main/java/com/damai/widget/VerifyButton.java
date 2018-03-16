package com.damai.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.citywithincity.models.LocalData;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;

public class VerifyButton extends SubmitButton implements ApiListener,
		IMessageListener {

	private static final int DEFAULT_TIMER = 60;

	private Timer timer;
	private TimerTask timerTask;
	private String timerFormat;
	private String normalFormat;

	private static final String timerButtonFetchKey = "timerButtonFetch";

	public VerifyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode())
			return;
		setOnClickListener(onClickListener);
		getJob().setWaitingMessage("正在获取验证码...");
		getJob().setApiListener(this);

		timerFormat = "再次获取\n(%d)";
		normalFormat = getText().toString();

	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 开始发送
			submit();
		}
	};

	/*
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		String fetchTime = LocalData.read()
				.getString(timerButtonFetchKey, null);
		long ltime = 0;
		if (fetchTime != null) {
			try {
				ltime = Long.parseLong(fetchTime);
			} catch (Throwable t) {

			}
		}

		if (ltime > 0) {
			int interval = (int) ((System.currentTimeMillis() - ltime) / 1000);
			if (interval < DEFAULT_TIMER) {
				// 开始计时
				startTimer(DEFAULT_TIMER - interval);
			}
		}

	}
*/
	private void setTimerLabel() {
		setText(String.format(timerFormat, time));
	}

	private int time;

	private void startTimer(int time) {
		setEnabled(false);
		this.time = time;
		setTimerLabel();
		timer = new Timer(true);
		timerTask = new TimerTask() {
			@Override
			public void run() {
				MessageUtil.sendMessage(VerifyButton.this);
			}
		};
		timer.schedule(timerTask, 1000, 1000);
	}

	private void setNormal() {
		setEnabled(true);
		setText(normalFormat);
		if (timer != null) {
			try {
				timerTask.cancel();
				timer.purge();
				timer.cancel();
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			} finally {
				timer = null;
				timerTask = null;
			}
		}

	}

	@Override
	public void onMessage(int message) {
		time--;
		if (time <= 0) {
			setNormal();
			return;
		}
		setTimerLabel();
	}

	private Object verifyId;

	public Object getVerifyId() {
		return verifyId;
	}

	@Override
	public void onJobSuccess(ApiJob job) {
		verifyId = job.getData();
		Alert.showShortToast("获取验证码成功...");
		LocalData.write().putString(timerButtonFetchKey,
				String.valueOf(System.currentTimeMillis()));
		startTimer(DEFAULT_TIMER);
	}

	@Override
	public boolean onJobError(ApiJob job) {
		return false;
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		return false;
	}
}
