package com.tck.daole.util;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.tck.daole.R;

import java.util.List;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;

/**
 * Created by Administrator on 2017/12/2.
 */

public class PickerUtil {
    private static OnPickerClickListener onPickerClickListener;

    public static void showPicker(final Activity ctx, List<String> items) {
        SinglePicker<String> picker = new SinglePicker<>(ctx, items);
        picker.setCanLoop(true);//不禁用循环
        picker.setTopBackgroundColor(ContextCompat.getColor(ctx, android.R.color.white));
        picker.setTopHeight(50);
        picker.setTopLineColor(ContextCompat.getColor(ctx, R.color.lineGrey));
        picker.setTopLineHeight(1);
        picker.setCancelTextColor(ContextCompat.getColor(ctx, android.R.color.holo_blue_bright));//取消
        picker.setSubmitTextColor(ContextCompat.getColor(ctx, android.R.color.holo_blue_bright));//确定
        picker.setSelectedTextColor(ContextCompat.getColor(ctx, R.color.nomalText));//选中的颜色
        picker.setUnSelectedTextColor(ContextCompat.getColor(ctx, android.R.color.darker_gray));//未选中选中的颜色
        LineConfig config = new LineConfig();
        config.setColor(ContextCompat.getColor(ctx, R.color.lineGrey));//线颜色
        config.setAlpha(140);//线透明度
        config.setRatio((float) (1.0 / 8.0));//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(200);
        picker.setBackgroundColor(ContextCompat.getColor(ctx, android.R.color.white));
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                onPickerClickListener.onPickerClick(index, item);
            }
        });
        picker.show();
    }

    public static void showPicker(final Activity ctx, String[] items) {
        SinglePicker<String> picker = new SinglePicker<>(ctx, items);
        picker.setCanLoop(true);//不禁用循环
        picker.setTopBackgroundColor(ContextCompat.getColor(ctx, android.R.color.white));
        picker.setTopHeight(50);
        picker.setTopLineColor(ContextCompat.getColor(ctx, R.color.lineGrey));
        picker.setTopLineHeight(1);
        picker.setCancelTextColor(ContextCompat.getColor(ctx, android.R.color.holo_blue_bright));//取消
        picker.setSubmitTextColor(ContextCompat.getColor(ctx, android.R.color.holo_blue_bright));//确定
        picker.setSelectedTextColor(ContextCompat.getColor(ctx, R.color.nomalText));//选中的颜色
        picker.setUnSelectedTextColor(ContextCompat.getColor(ctx, android.R.color.darker_gray));//未选中选中的颜色
        LineConfig config = new LineConfig();
        config.setColor(ContextCompat.getColor(ctx, R.color.lineGrey));//线颜色
        config.setAlpha(140);//线透明度
        config.setRatio((float) (1.0 / 8.0));//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(200);
        picker.setBackgroundColor(ContextCompat.getColor(ctx, android.R.color.white));
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                onPickerClickListener.onPickerClick(index, item);
            }
        });
        picker.show();
    }

    public interface OnPickerClickListener {
        void onPickerClick(int index, String item);
    }

    public static void setOnPickerClickListener(OnPickerClickListener onPickerClickListener) {
        PickerUtil.onPickerClickListener = onPickerClickListener;
    }
}
