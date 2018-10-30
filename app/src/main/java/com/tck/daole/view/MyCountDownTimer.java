package com.tck.daole.view;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 倒计时
 */
public class MyCountDownTimer extends CountDownTimer {
    private Context context;
    private TextView textView;

    public MyCountDownTimer(Context context, TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setClickable(false);
        textView.setText(millisUntilFinished / 1000 + "s");
        textView.setBackgroundColor(Color.parseColor("#C7C7C7"));
//        textView.setTextColor(ContextCompat.getColor(context, R.color.blue));
//        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.round_bottom_right_grey_8));
    }

    @Override
    public void onFinish() {
        textView.setText("重新获取");
        textView.setClickable(true);
        textView.setBackgroundColor(Color.parseColor("#EE8F29"));

//        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
//        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.round_bottom_right_blue_8));
    }
}
