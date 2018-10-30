package com.tck.daole.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.tck.daole.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2017/11/29.
 */

public class ImageLoadUtil {

    public static void showImage(String str, ImageView photo) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)
                .showImageOnFail(R.mipmap.def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(str, photo, options);
    }
}
