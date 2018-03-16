package com.citywithincity.ecard.user;

import android.widget.ImageView;

import com.citywithincity.auto.Observer;

@Observer
public interface IState {

	void handle(ImageView imageView, ImageView bg);
}
