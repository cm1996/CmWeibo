package ssdut.chenmo.cmweibo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import butterknife.BindView;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.cusview.ToolBar;
import ssdut.chenmo.cmweibo.fragment.LeftMenuFragment;

public class MainActivity extends BaseFragmentActivity implements ToolBar.ToolBarListener{

    @BindView(R.id.toolbar)
    ToolBar mToolBar;
    SlidingMenu menu;
    @Override
    protected void initData() {
        //初始化顶部栏
        mToolBar.setToolBarListener(this);
        //初始化侧滑栏
        initLeftMenu();





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
        titleCenter.setText("这里应该是用户名");  // 等上数据了再改过来
    }
}
