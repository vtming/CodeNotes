package com.wyf.codenotes.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.wyf.codenotes.R;


public class Notice {

    public static void alert(Context context, String message) {
        alert(context, message, null);
    }

    public static void alert(Context context, String message,
                             final OnClickListener listener) {
        new AlertDialog.Builder(context).setTitle("提示").setMessage(message)
                .setPositiveButton("确定", listener).show();
    }

    public static void alert(Context context, String message,
                             String buttonText, final OnClickListener listener) {
        new AlertDialog.Builder(context).setTitle("提示").setMessage(message)
                .setPositiveButton(buttonText, listener).show();
    }

    public static void alert(Context context, String message,
                             String buttonLeft, final OnClickListener listenerL, String buttonRight, final OnClickListener listenerR) {
        new AlertDialog.Builder(context).setTitle("提示").setMessage(message)
                .setPositiveButton(buttonLeft, listenerL)
                .setNegativeButton(buttonRight, listenerR).show();
    }

    public static void alertAndFinish(final Context context, String message) {
        new AlertDialog.Builder(context).setTitle("提示").setMessage(message)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).finish();
                    }
                }).show();
    }

    public static void confirm(Context context, String message,
                               final OnClickListener yesListener,
                               final OnClickListener cancelListener) {
        new AlertDialog.Builder(context).setTitle("提示").setMessage(message)
                .setPositiveButton("确定", yesListener)
                .setNegativeButton("取消", cancelListener).show();
    }


    public static void showSnackbar(Context context,View container, String msg) {
        Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundResource(R.color.colorPrimary);
        snackbar.show();
    }

    public static void showSnackbar(Context context,View container, String msg, @Snackbar.Duration int duration) {
        Snackbar snackbar = Snackbar.make(container, msg, duration);
        snackbar.getView().setBackgroundResource(R.color.colorPrimary);
        snackbar.show();
    }


    /**
     * 数据异常
     * @param context
     */
    public static void dataException(Context context) {
        Toast.makeText(context,"数据异常",Toast.LENGTH_LONG).show();
    }


}
