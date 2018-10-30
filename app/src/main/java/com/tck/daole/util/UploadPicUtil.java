package com.tck.daole.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tck.daole.R;
import com.tck.daole.view.HeadPopuWindow;

import java.io.File;

/**
 * 从相机相册获取图片
 */
public class UploadPicUtil {
    private static File mCameraFile;
    private static File mCropFile = new File(Environment.getExternalStorageDirectory(), "crop.jpg");

    /**
     * 显示Dialog
     */
    public static void showDia(final Activity ctx) {
        LayoutInflater in = LayoutInflater.from(ctx);
        View pickView = in.inflate(R.layout.pick_photo, null);
        final HeadPopuWindow pickPicPop = new HeadPopuWindow(ctx, pickView);

        TextView album = (TextView) pickView.findViewById(R.id.album);
        TextView takePhoto = (TextView) pickView.findViewById(R.id.takePhoto);
        TextView canclePic = (TextView) pickView.findViewById(R.id.canclePic);

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//相册   2
                Intent getAlbum = new Intent(Intent.ACTION_PICK);
                getAlbum.setType("image/*");
                ctx.startActivityForResult(getAlbum, 2);
                pickPicPop.dismiss();
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//相机   1
                doTakePhoto(ctx);
                pickPicPop.dismiss();
            }
        });
        canclePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPicPop.dismiss();
            }
        });
        pickPicPop.show();
    }

    /**
     * 打开相机
     */
    private static void doTakePhoto(Activity ctx) {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCameraFile = new File(Environment.getExternalStorageDirectory(), "crop.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uriFromFile = FileProvider.getUriForFile(ctx, "com.example.daole.fileprovider", mCameraFile);
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFromFile);
            takeIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takeIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));
        }

        ctx.startActivityForResult(takeIntent, 1);
    }

    /**
     * 获取相机图片
     */
    public static Uri getPicCamera(Activity act) {
        Uri inputUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            inputUri = FileProvider.getUriForFile(act, "com.example.daole.fileprovider", mCameraFile);//通过FileProvider创建一个content类型的Uri
        } else {
            inputUri = Uri.fromFile(mCameraFile);
        }
        return inputUri;
    }

    /**
     * 获取真实的Uri，获取手机图片的路径
     */
    public static Uri getRealUri(Activity act, Uri mUri) {
        if (mUri == null) {
            return null;
        }
        Uri realUri = null;
        String scheme = mUri.getScheme();
        if (scheme == null) {
            realUri = mUri;
        } else if ("file".equals(scheme)) {//获取协议名
            realUri = mUri;
        } else {
            //通过编号来查询真实的图片的地址
            Cursor mCursor = act.getContentResolver().query(mUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (mCursor != null && mCursor.moveToFirst()) {//如果能查到一条数据的话
                int index = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);//获取字段的下标
                String realPath = mCursor.getString(index);//获取该条数据的值
                realUri = Uri.fromFile(new File(realPath));
                mCursor.close();//关闭游标
            }
        }
        return realUri;
    }

    /**
     * 获取裁剪图片
     */
    public static Bitmap getCroppingPhone(Activity act) {
        Uri inputUri = FileProvider.getUriForFile(act, "com.example.daole.fileprovider", mCropFile);//通过FileProvider创建一个content类型的Uri
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(act.getContentResolver().openInputStream(inputUri));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 裁剪方法   5
     */
    public static void startPhotoZoom(Activity act, Uri uri) {
        if (uri == null) {
            Log.e("error", "The uri is not exist.");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri outPutUri = Uri.fromFile(mCropFile);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            Uri outPutUri = Uri.fromFile(mCropFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String url = GetImagePath.getPath(act, uri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(uri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }


        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("outputFormat", "JPEG");

        act.startActivityForResult(intent, 5);
    }
}
