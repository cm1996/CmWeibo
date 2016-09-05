package ssdut.chenmo.cmweibo.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import butterknife.BindView;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.cusview.ToolBar;
import ssdut.chenmo.cmweibo.fragment.FindFragment;
import ssdut.chenmo.cmweibo.fragment.LeftMenuFragment;
import ssdut.chenmo.cmweibo.fragment.MainFragment;
import ssdut.chenmo.cmweibo.fragment.MessageFragment;
import ssdut.chenmo.cmweibo.fragment.SettingFragment;
import ssdut.chenmo.cmweibo.utils.AccessTokenKeeper;

public class MainActivity extends BaseFragmentActivity implements ToolBar.ToolBarListener{

    @BindView(R.id.toolbar)
    ToolBar mToolBar;
    SlidingMenu menu;
    Oauth2AccessToken mOauth2AccessToken;
    //来来来 先把三个fragment实例化
    FindFragment mFindFragment = new FindFragment();
    MessageFragment mMessageFragment = new MessageFragment();
    SettingFragment mSettingFragment = new SettingFragment();
    MainFragment mMainFragment = new MainFragment();
    Fragment currentFragment;

    @Override
    protected void initData() {
        //初始化个人信息令牌
        mOauth2AccessToken = AccessTokenKeeper.readAccessToken(this);
        //初始化顶部栏
        mToolBar.setToolBarListener(this);
        //初始化侧滑栏
        initLeftMenu();
        //初始化主界面的内容部分（也是一个Fragment）
        getSupportFragmentManager().beginTransaction().replace(R.id.id_layout_main, mMainFragment).commit();
        currentFragment = mMainFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.id_layout_main,mFindFragment).hide(mFindFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.id_layout_main,mMessageFragment).hide(mMessageFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.id_layout_main,mSettingFragment).hide(mSettingFragment).commit();





    }

    private void initLeftMenu(){
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        //为侧滑菜单设置布局
        LeftMenuFragment mLeftMenuFragment = new LeftMenuFragment(); //侧滑栏所使用的Fragment
        menu.setMenu(R.layout.layout_leftmenu);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_layout_leftmenu, mLeftMenuFragment).commit();
        //然后配置相应的各种模式
        menu.setMode(SlidingMenu.LEFT);
        // 设置滑动菜单视图的宽度,这个宽度是指右边剩余的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置触摸屏幕的模式,允许滑动边缘打开
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        //设置菜单内容的移动速度？？？
        menu.setBehindScrollScale(0.5f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         * 补充：因为我没有用title/ActionBar之类的，我是用的自己的ToolBar，所以这两个都行
         */
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
    }

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onExit() {

    }



    public void replaceFragment(int position){
        getSupportFragmentManager().beginTransaction().hide(currentFragment).commit();
        switch (position){
            case 0:
                getSupportFragmentManager().beginTransaction().show(mMainFragment).commit();
                currentFragment = mMainFragment;
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().show(mMessageFragment).commit();
                currentFragment = mMessageFragment;

                break;
            case 2:
                getSupportFragmentManager().beginTransaction().show(mFindFragment).commit();
                currentFragment = mFindFragment;

                break;
            case 3:
                getSupportFragmentManager().beginTransaction().show(mSettingFragment).commit();
                currentFragment = mSettingFragment;

                break;
        }
        menu.showContent();
    }


    @Override
    public void onBackClicked() {
        menu.showMenu();
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
        back.setImageResource(R.mipmap.hamburger);
        if(mOauth2AccessToken==null){
            Log.e("1111111111","1111111111");
        } else if(mOauth2AccessToken.getUid()==null){
            Log.e("2222222","22222222222");
        } else
        titleCenter.setText(mOauth2AccessToken.getUid());  // 等上数据了再改过来
    }
}
