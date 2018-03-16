package com.citywithincity.models;

import java.util.Timer;
import java.util.TimerTask;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;

public class MyTimer implements IDestroyable, IMessageListener {
	public interface ITimerListener {
		void onTimer(int id);
	}

	private Timer mTimer = null;
	private TimerTask mTimerTask = null;

	private int delay;
	private int id;
	private int period;

	private ITimerListener listener;

	/**
	 * 
	 * @param id
	 * @param delay
	 */
	public MyTimer(int id, int delay) {
		this(id, delay, 0);
	}

	/**
	 * 
	 * @param id
	 *            标识
	 * @param delay
	 *            间隔时间
	 * @param period
	 *            首次
	 */
	public MyTimer(int id, int delay, int period) {
		this.id = id;
		this.delay = delay;

		this.period = period;
	}

	public void config(int delay, int period) {
		this.delay = delay;

		this.period = period;
	}

	public void setListener(ITimerListener listener) {
		this.listener = listener;
	}

	public void start() {
		if (mTimer == null) {
			mTimer = new Timer();
		}

		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				MessageUtil.sendMessage(0, MyTimer.this);
			}
		};

		if (mTimer != null && mTimerTask != null) {
			if (period > 0) {
				mTimer.schedule(mTimerTask, delay, period);
			} else {
				mTimer.schedule(mTimerTask, delay);
			}
		}
	}

	public void stop() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}

	@Override
	public void destroy() {
		stop();
		listener = null;
	}

	@Override
	public void onMessage(int message) {
		if (listener != null)
			listener.onTimer(id);
	}

}
