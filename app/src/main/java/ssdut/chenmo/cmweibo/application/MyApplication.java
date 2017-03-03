package ssdut.chenmo.cmweibo.application;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ssdut.chenmo.cmweibo.activity.MainActivity;
import ssdut.chenmo.cmweibo.activity.WelcomeActivity;
import ssdut.chenmo.cmweibo.config.Constants;
import ssdut.chenmo.cmweibo.models.Emotion;
import ssdut.chenmo.cmweibo.models.Emotions;
import ssdut.chenmo.cmweibo.utils.AccessTokenKeeper;

/**
 * Created by chenmo on 2016/10/20.
 */

public class MyApplication extends Application {

    Oauth2AccessToken mOauth2AccessToken;
    ArrayList<Emotion> mEmotions;
    public Map<String,Bitmap> mBitmapEmotions = new HashMap<>();
    private static MyApplication instance;

    public static MyApplication getInstance(){
        return instance;
    }



    @Override
    public void onCreate() {
        mOauth2AccessToken = AccessTokenKeeper.readAccessToken(this);
        StatusesAPI mStatusesAPI = new StatusesAPI(MyApplication.this, Constants.APP_KEY,mOauth2AccessToken);
        mStatusesAPI.emotions("face", "cnname", new RequestListener() {
            @Override
            public void onComplete(String s) {
                mEmotions = Emotions.parse(s);
                for(Emotion e: mEmotions) {
                    Log.e("test emotions",e.value+" "+e.url);
                }
                for(final Emotion e: mEmotions) {
                    ImageLoader.getInstance().loadImage(e.url, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    mBitmapEmotions.put(e.value,loadedImage);
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {

                                }
                            });

                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.e("fuck","fail to get emotions of Weibo");
            }
        });
        instance = this;
    }

}
