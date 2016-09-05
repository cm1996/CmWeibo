package ssdut.chenmo.cmweibo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.api.share.Base;

import butterknife.BindView;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.cusview.ToolBar;

public class SelfActivity extends BaseActivity implements ToolBar.ToolBarListener{

    @BindView(R.id.toolbar)
    ToolBar mToolBar;
    @Override
    protected void initData() {
        mToolBar.setToolBarListener(this);
    }

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_self;
    }

    @Override
    protected void onExit() {

    }

    @Override
    public void onBackClicked() {
        onBackPressed();
        playExitAnimation();
    }

    @Override
    public void onMoreClicked() {

    }

    @Override
    public void onTitleLeftClicked() {

    }

    @Override
    public void onTitleRightClicked() {

    }

    @Override
    public void onInit(ImageView back, TextView titleLeft, TextView titleCenter, TextView titleRight, ImageView more) {

    }
}
