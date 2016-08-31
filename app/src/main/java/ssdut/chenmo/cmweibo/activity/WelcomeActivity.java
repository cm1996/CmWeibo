package ssdut.chenmo.cmweibo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sina.weibo.sdk.api.share.Base;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.text.SimpleDateFormat;

import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.utils.AccessTokenKeeper;


/**
 * 进入APP的第一个界面 欢迎界面
 * 功能 ：  初始化各种东西 我还没想好
 *          判断进入哪个页面
 */
public class WelcomeActivity extends BaseActivity {

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;


    @Override
    protected void initData() {

        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            //上次的登录信息存在且可用  那就直接进入 MainActivity
            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            playOpenAnimation();
            finish();
        } else {
            //不存在或者不可用 进入 LoginActivity
            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            playOpenAnimation();
            finish();
        }
    }


    @Override
    protected int initLayoutRes() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onExit() {

    }
}
