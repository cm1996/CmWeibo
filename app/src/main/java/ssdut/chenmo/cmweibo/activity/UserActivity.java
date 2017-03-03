package ssdut.chenmo.cmweibo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.config.Constants;
import ssdut.chenmo.cmweibo.fragment.MainFragment;
import ssdut.chenmo.cmweibo.utils.AccessTokenKeeper;
import ssdut.chenmo.cmweibo.utils.BitmapUtils;

public class UserActivity extends AppCompatActivity {


    @BindView(R.id.bg)
    ImageView mBg;
    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.guanzhu)
    TextView mGuanzhu;
    @BindView(R.id.fensi)
    TextView mFensi;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;


    String user_name = "";
    String user_fensi = "";
    String user_guanzhu = "";
    String user_avatar = "";
    String user_description = "";
    String user_id = "";

    MainFragment mMainFragment = new MainFragment();
    Oauth2AccessToken mOauth2AccessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = this.getIntent();
        user_name = intent.getStringExtra("user_name");
        Log.e("user nam is ", ""+user_name);
        user_fensi = intent.getStringExtra("user_fensi");
        user_guanzhu = intent.getStringExtra("user_guanzhu");
        user_avatar = intent.getStringExtra("user_avatar");
        user_description = intent.getStringExtra("user_description");
        user_id = intent.getStringExtra("user_id");

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        initUser();

        mOauth2AccessToken = AccessTokenKeeper.readAccessToken(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, mMainFragment).commit();
        mMainFragment.setWeiboDataProvider(new MainFragment.WeiboDataProvider() {
            @Override
            public void updateData(long since_id, long max_id, final boolean isNew) {
                StatusesAPI mStatusesAPI = new StatusesAPI(UserActivity.this,Constants.APP_KEY,mOauth2AccessToken);
                mStatusesAPI.userTimeline(Long.parseLong(user_id), since_id, max_id, 10, 1, false, 0, false, new RequestListener() {
                    @Override
                    public void onComplete(String response) {

                        if (!TextUtils.isEmpty(response)) {
                            Log.e("LLLLLLLLLLLLL",response);
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
                                Toast.makeText(UserActivity.this, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {

                    }
                });
                /*mStatusesAPI.friendsTimeline(since_id, max_id, 20, 1, false, 0, false, new RequestListener() {
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
                                Toast.makeText(UserActivity.this, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {

                    }
                });*/
            }

            @Override
            public void sendNewWeibo(String s) {

            }
        });


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_clam, R.anim.slide_out_right);
            }
        });

    }

    @OnClick({R.id.avatar, R.id.guanzhu, R.id.fensi, R.id.description, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                break;
            case R.id.guanzhu:
                break;
            case R.id.fensi:
                break;
            case R.id.description:
                break;
            case R.id.fab:

                break;
        }
    }

    private void initUser() {
        //mToolbar.setTitle(user_name);
        mToolbarLayout.setTitle(user_name);
        mFensi.setText(mFensi.getText() + " " + user_fensi);
        mGuanzhu.setText(mGuanzhu.getText() + " " + user_guanzhu);
        mDescription.setText(user_description);
        ImageLoader.getInstance().loadImage(user_avatar,new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                Bitmap bm = BitmapUtils.blur(loadedImage, mBg, UserActivity.this);
                //Bitmap bm = loadedImage;
                mBg.setImageBitmap(bm);
                mAvatar.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
}
