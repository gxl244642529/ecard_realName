package com.damai.alert;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.damai.util.ViewUtil;

/**
 * Created by renxueliang on 16/12/13.
 */

public class MaxHeightView extends LinearLayout {


    private int mMaxHeight;

    public MaxHeightView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMaxHeight = (int) (ViewUtil.screenHeight * 0.7);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
                     heightSize = heightSize <= mMaxHeight ? heightSize
                               : (int) mMaxHeight;
                 }

          if (heightMode == MeasureSpec.UNSPECIFIED) {
                       heightSize = heightSize <= mMaxHeight ? heightSize
                         : (int) mMaxHeight;
              }
            if (heightMode == MeasureSpec.AT_MOST) {
                heightSize = heightSize <= mMaxHeight ? heightSize
                           : (int) mMaxHeight;
                }
           int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                     heightMode);
           super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
      }
}
