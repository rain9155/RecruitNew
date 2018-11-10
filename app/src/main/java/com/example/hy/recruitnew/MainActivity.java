package com.example.hy.recruitnew;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.example.hy.recruitnew.adapter.BaseRvAdapter;
import com.example.hy.recruitnew.util.StatusBarUtil;
import com.example.hy.recruitnew.util.ToastUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private static final long WAIT_TIME = 2000L; // 再点一次退出程序时间设置
    private static long TOUCH_TIME = 0;//第一次按下返回键的时间
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.fab_android)
    FloatingActionButton fabAndroid;
    @BindView(R.id.fab_background)
    FloatingActionButton fabBackground;
    @BindView(R.id.fab_front)
    FloatingActionButton fabFront;
    @BindView(R.id.fab_bigData)
    FloatingActionButton fabBigData;
    @BindView(R.id.rv_main)
    RecyclerView rvMain;
    @BindView(R.id.fab_main)
    FloatingActionsMenu fabMain;


    private Unbinder mUnbinder;
    private BaseRvAdapter mBaseRvAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int mLastIndex;
    private boolean isScroll = true;
    private int mFlag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.compat(this);
        mUnbinder = ButterKnife.bind(this);
        hideFloatingButton();
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBaseRvAdapter = new BaseRvAdapter(this);
        rvMain.setLayoutManager(mLinearLayoutManager);
        rvMain.setAdapter(mBaseRvAdapter);
        rvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isScroll = false;
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isScroll = true;
                int firstVisibleItemCompletePosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstVisibleItemCompletePosition == 0) {
                    hideFloatingButton();
                } else {
                    showFloatingButton();
                }
            }
        });
       // fabMain.setOnClickListener(v -> changeBackgoundAlpha(0.5f));
        fabAndroid.setOnClickListener(v -> startActivity(1));
        fabBackground.setOnClickListener(v -> startActivity(2));
        fabFront.setOnClickListener(v -> startActivity(3));
        fabBigData.setOnClickListener(v -> startActivity(4));
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            ToastUtil.toast(this, getResources().getString(R.string.exit_app));
        }
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    /**
     * 显示floatingButton
     */
    @SuppressLint("RestrictedApi")
    private void showFloatingButton() {
        if (fabMain.getVisibility() == View.INVISIBLE) {
            fabMain.setVisibility(View.VISIBLE);
            fabMain.animate().setDuration(500).setInterpolator(new BounceInterpolator()).translationY(0).start();
        }
    }

    /**
     * 隐藏floatingButton
     */
    @SuppressLint("RestrictedApi")
    private void hideFloatingButton() {
        if (fabMain.getVisibility() == View.VISIBLE) {
            ViewPropertyAnimator animator = fabMain.animate().setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).translationY(
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 400, getResources().getDisplayMetrics())
            );
            new Handler().postDelayed(() -> fabMain.setVisibility(View.INVISIBLE), 301);
            animator.start();
        }
    }

    /**
     * 改变背景透明度
     *
     * @param alpha
     */
    private void changeBackgoundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    private void startActivity(int flag) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            RegisterActivity.startActivityByExplode(this, flag);
        }else {
            RegisterActivity.startActivity(this, flag);
        }
    }

}
