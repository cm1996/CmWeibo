package ssdut.chenmo.cmweibo.activity;

import android.animation.ValueAnimator;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.application.MyApplication;
import ssdut.chenmo.cmweibo.config.Constants;
import ssdut.chenmo.cmweibo.cusview.ToolBar;
import ssdut.chenmo.cmweibo.fragment.FindFragment;
import ssdut.chenmo.cmweibo.fragment.LeftMenuFragment;
import ssdut.chenmo.cmweibo.fragment.MainFragment;
import ssdut.chenmo.cmweibo.fragment.MessageFragment;
import ssdut.chenmo.cmweibo.fragment.SettingFragment;
import ssdut.chenmo.cmweibo.models.Emotion;
import ssdut.chenmo.cmweibo.models.Emotions;
import ssdut.chenmo.cmweibo.utils.AccessTokenKeeper;
import ssdut.chenmo.cmweibo.utils.UserUtils;

public class MainActivity extends BaseFragmentActivity implements ToolBar.ToolBarListener{


    @BindView(R.id.toolbar)
    ToolBar mToolBar;
    public SlidingMenu menu;
    LeftMenuFragment mLeftMenuFragment;
    public Oauth2AccessToken mOauth2AccessToken;
    User user;

    //一些FLAG
    boolean hasGotUserInfo = false;

    //来来来 先把4个fragment实例化
    FindFragment mFindFragment = new FindFragment();
    MessageFragment mMessageFragment = new MessageFragment();
    SettingFragment mSettingFragment = new SettingFragment();
    MainFragment mMainFragment = new MainFragment();
    Fragment currentFragment;

    //ArrayList<Emotion> mEmotions;

    @Override
    protected void initData() {
        //初始化个人信息令牌
        mOauth2AccessToken = AccessTokenKeeper.readAccessToken(this);
        //初始化User信息
        initUser();
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
        /*StatusesAPI mStatusesAPI = new StatusesAPI(MainActivity.this,Constants.APP_KEY,mOauth2AccessToken);
        mStatusesAPI.emotions("face", "cnname", new RequestListener() {
            @Override
            public void onComplete(String s) {
                mEmotions = Emotions.parse(s);
                for(Emotion e: mEmotions) {
                    Log.e("test emotions",e.value+" "+e.url);
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.e("fuck","fail to get emotions of Weibo");
            }
        });*/
        mMainFragment.setWeiboDataProvider(new MainFragment.WeiboDataProvider() {

            @Override
            public void updateData(long since_id, final long max_id , final boolean isNew) {
                StatusesAPI mStatusesAPI = new StatusesAPI(MainActivity.this,
                        Constants.APP_KEY,mOauth2AccessToken);
                mStatusesAPI.friendsTimeline(since_id, max_id, 20, 1, false, 0, false,
                        new RequestListener() {
                    @Override
                    public void onComplete(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            if (response.startsWith("{\"statuses\"")) {
                                Message msg = new Message();
                                if(isNew){
                                    msg.what = 1001;
                                } else {
                                    msg.what = 1002;
                                }
                                Bundle bundle = new Bundle();
                                bundle.putString("response", response);
                                msg.setData(bundle);
                                mMainFragment.mHandler.sendMessage(msg);
                                //statuses.statusList;
                            } else {
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        showToast("出现异常001");
                    }
                });
            }

            @Override
            public void sendNewWeibo(String s) {
                StatusesAPI mStatusesAPI = new StatusesAPI(MainActivity.this,
                        Constants.APP_KEY,mOauth2AccessToken);
                mStatusesAPI.update(s, "0.0", "0.0", new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        Snackbar.make(mToolBar,"微博已发送",Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        Snackbar.make(mToolBar,"发送失败",Snackbar.LENGTH_SHORT).show();
                    }
                });

            }
        });
        Log.e("FUCK YOU", Arrays.toString(fileList()));
    }


    private void initUser() {
        UsersAPI mUsersAPI = new UsersAPI(this, Constants.APP_KEY,mOauth2AccessToken); // 获取用户信息接口
        RequestListener mListener = new RequestListener() {
            @Override
            public void onComplete(String s) {

                if(!TextUtils.isEmpty(s)){
                    user = User.parse(s);
                    UserUtils.setUser(user);
                    //改一下UI
                    changeUi();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                showToast("获取个人信息失败");    //注意后期改动到String资源文件去
                Log.e("错误代码及信息",""+e);
            }
        };
        mUsersAPI.show(Long.parseLong(mOauth2AccessToken.getUid()),mListener); //直接是异步的运行在UI线程
    }

    private void changeUi() {
        //toolbar的标题得改了
        ((TextView)findViewById(R.id.toolbar).findViewById(R.id.title_center)).setText(user.screen_name);
        //侧滑栏的昵称和个人简介
        mLeftMenuFragment.initUi(user.screen_name,user.description,user.avatar_large);

    }

    private void initLeftMenu(){
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        //为侧滑菜单设置布局
        mLeftMenuFragment = new LeftMenuFragment(); //侧滑栏所使用的Fragment
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
        //final int topBarBottom = mLeftMenuFragment.ll.getBottom();
        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                mLeftMenuFragment.ll.setVisibility(View.VISIBLE);
                mLeftMenuFragment.mListView.setVisibility(View.VISIBLE);
                /*ValueAnimator animator_fam = ValueAnimator.ofFloat(1,1.5f);
                animator_fam.setDuration(50);
                animator_fam.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float curValue = (float)animation.getAnimatedValue();
                        mMainFragment.mFAM.setScaleX(curValue);
                        mMainFragment.mFAM.setScaleY(curValue);
                    }
                });
                animator_fam.start();*/

                ValueAnimator animator = ValueAnimator.ofInt(0,320);
                animator.setDuration(200);

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int curValue = (int)animation.getAnimatedValue();
                        mLeftMenuFragment.ll.layout(
                                mLeftMenuFragment.ll.getLeft(),
                                mLeftMenuFragment.ll.getTop(),
                                mLeftMenuFragment.ll.getRight(),
                                curValue);
                        mLeftMenuFragment.mListView.layout(
                                mLeftMenuFragment.mListView.getLeft(),
                                740-curValue,
                                mLeftMenuFragment.mListView.getRight(),
                                mLeftMenuFragment.mListView.getBottom()
                        );
                    }
                });
                animator.start();
            }
        });
        menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                ValueAnimator animator = ValueAnimator.ofInt(320,0);
                animator.setDuration(200);

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int curValue = (int)animation.getAnimatedValue();
                        mLeftMenuFragment.ll.layout(mLeftMenuFragment.ll.getLeft(),
                                mLeftMenuFragment.ll.getTop(),
                                mLeftMenuFragment.ll.getRight(),
                                curValue);
                        mLeftMenuFragment.mListView.layout(
                                mLeftMenuFragment.mListView.getLeft(),
                                740-curValue,
                                mLeftMenuFragment.mListView.getRight(),
                                mLeftMenuFragment.mListView.getBottom()
                        );
                    }
                });
                animator.start();

            }
        });
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
        titleCenter.setText(mOauth2AccessToken.getUid());
    }
}
