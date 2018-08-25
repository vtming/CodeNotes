package com.wyf.codenotes;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AvatarActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AvatarActivity";

    private static final int GETPIC_TAKEPICTURE = 200;
    private static final int GETPIC_CHOOSEPIC = 201;
    private static final int CROP_CHOOSE = 202;

    private Uri iconUrl, iconCrop;


    private boolean bPermission;
    private ImageView avatar_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        avatar_iv = (ImageView)findViewById(R.id.avatar_iv);

        bPermission = checkCropPermission();
    }


    protected void onRightTextViewClick(View v){
        Intent intent  = new Intent();
        if(iconCrop != null && !TextUtils.isEmpty(iconCrop.getPath())){
//            intent.putExtra(AppConstants.RETURN_EXTRA_GENERAL, iconCrop.getPath());
            intent.setData(iconCrop);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }else {
            return;
        }
    }

    @Override
    public void onClick(View v) {

//        if(v.getId() == R.id.takePicture_bt){
//            getPicFrom(GETPIC_TAKEPICTURE);
//        }else if(v.getId() == R.id.choosePic_bt){
//            getPicFrom(GETPIC_CHOOSEPIC);
//        }
    }

    /**
     * 获取图片资源
     *
     * @param type
     */
    private void getPicFrom(int type) {
        if (!bPermission){
//            Toast.makeText(this, getString(R.string.tip_no_permission), Toast.LENGTH_SHORT).show();
            return;
        }
        switch (type) {
            case GETPIC_TAKEPICTURE:
                Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                iconUrl = createCoverUri("_icon","");
                intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, iconUrl);
                startActivityForResult(intent_photo, GETPIC_TAKEPICTURE);
                break;
            case GETPIC_CHOOSEPIC:
                iconUrl = createCoverUri("_select_icon","");
                Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
                intent_album.setType("image/*");
                startActivityForResult(intent_album, GETPIC_CHOOSEPIC);
                break;
        }
    }

    private Uri createCoverUri(String type, String filename) {
//        String filename = AppInfo.loginInfo.getUserId()+ type + ".jpg";
        File outputImage = new File(Environment.getExternalStorageDirectory(), filename);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_PERMISSION_REQ_CODE);
            return null;
        }
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

    public void startPhotoZoom(Uri uri) {
        iconCrop = createCoverUri("_icon_crop","");

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 300);
        intent.putExtra("aspectY", 300);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iconCrop);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
//            SxbLog.e(TAG, "onActivityResult->failed for request: " + requestCode + "/" + resultCode);
            return;
        }

        switch (requestCode) {
            case GETPIC_TAKEPICTURE:
                startPhotoZoom(iconUrl);
                break;
            case GETPIC_CHOOSEPIC:
//        String path = UIUtils.getPath(this, data.getData());
                String path = "";
                if (null != path) {
//            SxbLog.e(TAG, "startPhotoZoom->path:" + path);
                    File file = new File(path);
                    startPhotoZoom(Uri.fromFile(file));
                }
                break;
            case CROP_CHOOSE:
//                mUploadHelper.uploadCover(iconCrop.getPath());

//            Bitmap bitmap = BitmapFactory.decodeFile(iconCrop.getPath());
//            avatar_iv.setImageBitmap(bitmap);

                File file = new File(iconCrop.getPath());

                avatar_iv.setImageURI(iconCrop);

                break;
        }
    }




    private boolean checkCropPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<String>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)){
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0){
                ActivityCompat.requestPermissions(this,
                        (String[]) permissions.toArray(new String[0]),
                        100);
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                for (int ret : grantResults){
                    if (ret != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                }
                bPermission = true;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
//        avatar_iv.setImageBitmap(null);
        super.onDestroy();

    }

    /**
     * 图片选择对话框
     */
    private void showPhotoDialog() {
//        final Dialog pickDialog = new Dialog(this, R.style.floag_dialog);
        final Dialog pickDialog = new Dialog(this);
//        pickDialog.setContentView(R.layout.pic_choose);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int)(display.getWidth()); //设置宽度

        pickDialog.getWindow().setAttributes(lp);

//        TextView camera = (TextView) pickDialog.findViewById(R.id.chos_camera);
//        TextView picLib = (TextView) pickDialog.findViewById(R.id.pic_lib);
//        TextView cancel = (TextView) pickDialog.findViewById(R.id.btn_cancel);
//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getPicFrom(GETPIC_TAKEPICTURE);
//                pickDialog.dismiss();
//            }
//        });
//
//        picLib.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getPicFrom(GETPIC_CHOOSEPIC);
//                pickDialog.dismiss();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickDialog.dismiss();
//            }
//        });

        pickDialog.show();
    }


    private void checkPermission() {
        final List<String> permissionsList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);

            if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);

            if ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);

            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);


//            if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
//                permissionsList.add(Manifest.permission.CAMERA);


            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        200);
            }else {
//                progressBar.setVisibility(View.VISIBLE);
//                startPlugin(this);
            }
        }else {
//            progressBar.setVisibility(View.VISIBLE);
//            startPlugin(this);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults ,int ff) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 200){
            for (int ret : grantResults){
                if (ret != PackageManager.PERMISSION_GRANTED){
                    showAlertDialog();
                    return;
                }
            }
            //所有权限都被授权! 进入插件
//            progressBar.setVisibility(View.VISIBLE);
//            startPlugin(this);
        }
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("请开启定位、读写存储卡、读取电话状态权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);

                        finish();
                    }
                })
                .show();
    }


}
