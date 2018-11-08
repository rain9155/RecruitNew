package com.example.hy.recruitnew.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * 系统状态栏变色
 * Created by 陈健宇 at 2018/11/8
 */
public class StatusBarUtil {

        private static final int INVALID_VAL = -1;
        private static final int COLOR_DEFAULT = Color.parseColor("#20000000");

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public static void compat(Activity activity, int statusColor) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(INVALID_VAL == statusColor) return;
                    activity.getWindow().setStatusBarColor(statusColor);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                int color = COLOR_DEFAULT;
                if (statusColor != INVALID_VAL) {
                    color = statusColor;
                }
                ViewGroup contentView =  activity.findViewById(android.R.id.content);
                View statusBarView = contentView.getChildAt(0);
                //改变颜色时避免重复添加statusBarView
                if (statusBarView != null && statusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
                    statusBarView.setBackgroundColor(color);
                    return;
                }
                statusBarView = new View(activity);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity)
                );
                statusBarView.setBackgroundColor(color);
                contentView.addView(statusBarView, lp);
            }
        }

    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL);
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private StatusBarUtil() {
        throw new AssertionError();
    }

}