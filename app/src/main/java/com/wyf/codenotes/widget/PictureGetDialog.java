package com.wyf.codenotes.widget;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyfei on 2018/4/16.
 */

public class PictureGetDialog extends BottomSheetDialog {


    private static final String TAG = "PictureGetDialog";

    public static final int GET_TAKE_PICTURE = 0000200;
    public static final int GET_LOCAL_PICTURE = 0000201;
    public static final int PHOTO_ZOOM_PICTURE = 0000202;
    /**
     * 头像存储地址
     */
    private String avatarPath;
    private Uri imageURI;

    public Uri getImageURI() {
        return imageURI;
    }

    public PictureGetDialog(@NonNull Context context) {
        super(context);
    }

    public PictureGetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected PictureGetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(layoutResId);
        findViewById(android.support.design.R.id.design_bottom_sheet).setBackgroundColor(0x00000000);
    }


    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        findViewById(android.support.design.R.id.design_bottom_sheet).setBackgroundColor(0x00000000);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        findViewById(android.support.design.R.id.design_bottom_sheet).setBackgroundColor(0x00000000);
    }


    public void setTakePicId(@IdRes int resId){
        View v = findViewById(resId);
        if(v != null){
            findViewById(resId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPicFrom(GET_TAKE_PICTURE);
                }
            });
        }
    }

    public void setLocalPicId(@IdRes int resId){
        View v = findViewById(resId);
        if(v != null){
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPicFrom(GET_LOCAL_PICTURE);
                }
            });
        }
    }

    public void setCancleId(@IdRes int resId){
        View v = findViewById(resId);
        if(v != null){
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }


    /**
     * 获取图片资源
     *
     * @param type
     */
    private void getPicFrom(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        && ContextCompat.checkSelfPermission(getOwnerActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            return ;
        }

        switch (type) {
            case GET_TAKE_PICTURE:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getOwnerActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
                    return ;
                }

                Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageURI = createCoverUri();
                intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                getOwnerActivity().startActivityForResult(intent_photo, GET_TAKE_PICTURE);
                break;
            case GET_LOCAL_PICTURE:
                imageURI = createCoverUri();
                Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
                intent_album.setType("image/*");
                getOwnerActivity().startActivityForResult(intent_album, GET_LOCAL_PICTURE);
                break;
        }
        dismiss();
    }


    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 300);
//        intent.putExtra("aspectY", 150);
//        intent.putExtra("outputX", 300);
//        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        getOwnerActivity().startActivityForResult(intent, PHOTO_ZOOM_PICTURE);
    }

    private Uri createCoverUri() {
        File outputImage = new File(getDir(), "avatar.jpg");
        avatarPath = outputImage.getAbsolutePath();

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(outputImage);
    }


    private String getDir(){
        String dir =  Environment.getExternalStorageDirectory()+ File.separator + getContext().getPackageName();
        File file = new File(dir);
        if(!file.exists()){
            file.mkdirs();
        }
        return dir;
    }




    public void checkPermission() {
        final List<String> permissionsList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((getOwnerActivity().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);

            if ((getOwnerActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            if ((getOwnerActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if ((getOwnerActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);


            if (permissionsList.size() != 0) {
                getOwnerActivity().requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        200);
            }
        }
    }



}
