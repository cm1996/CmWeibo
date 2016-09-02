package ssdut.chenmo.cmweibo.activity;

import android.annotation.TargetApi;
import android.app.Activity;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import ssdut.chenmo.cmweibo.R;

public abstract class BaseFragmentActivity extends FragmentActivity {


    protected Activity mBaseAty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseAty=this;
        initStatusBar();  //沉浸式通知栏

        if (initLayoutRes() != 0) {
            setContentView(initLayoutRes());
        }else {
            throw new NullPointerException("you may have forgot to set layout resource id on function initLayoutRes()");
        }
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 在此函数中初始化数据和视图
     */
    protected abstract void initData();

    /**
     * 在此函数中给出本Activity所用的布局
     */
    protected abstract int initLayoutRes();

    /**
     * 用户确认退出界面时回调的方法
     */
    protected abstract void onExit();

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * 实现经典的Activity打开动画
     */
    protected void playOpenAnimation(){
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_clam);
    }
    /**
     * 实现经典的Activity退出动画
     */
    protected void playExitAnimation(){
        overridePendingTransition(R.anim.slide_clam,R.anim.slide_out_right);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.tianyi_blue);//通知栏所需颜色
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
