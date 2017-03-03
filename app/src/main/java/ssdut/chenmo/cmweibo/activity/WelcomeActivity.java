package ssdut.chenmo.cmweibo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.io.File;

import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.config.Paths;
import ssdut.chenmo.cmweibo.utils.AccessTokenKeeper;


/**
 * 进入APP的第一个界面 欢迎界面
 * 功能 ：  初始化各种东西 我还没想好
 *          判断进入哪个页面
 */
public class WelcomeActivity extends BaseActivity {

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    public DisplayImageOptions mOptions;

    public static final int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0;
    int isAbleToStartActivity = 0;
    int ableToStartActivity = 2;

    @Override
    protected void initData() {


        //判断权限
        if(ContextCompat.checkSelfPermission(WelcomeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WelcomeActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        } else {
            isAbleToStartActivity = ableToStartActivity-1;
        }




        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e("AAAAAAAA","可以用");
        } else {
            Log.e("BBBBBBBBBBBBBBBB","不能用");

        }



        //初始化ImageLoader
        mOptions = new DisplayImageOptions.Builder().showImageOnLoading(0) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
//                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//                .displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
//                .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
                .build();// 构建完成

//        String cacheDir = Paths.imagesPath;
//        File cachePath = new File(cacheDir);// 获取到缓存的目录地址
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                .discCache(new UnlimitedDiscCache(cachePath))
//                // 自定义缓存路径
                .defaultDisplayImageOptions(mOptions)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
//                .imageDownloader(
//                        new BaseImageDownloader(App.this, 5 * 1000, 30 * 1000))
//                        // readTimeout(30)// 超时时间
                .writeDebugLogs() // Remove for release app
                .threadPoolSize(3)
                .build();
        ImageLoader.getInstance().init(config);
        startNewActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //初始化文件夹路径
                    File file=new File(Paths.imagePath);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    file=new File(Paths.dbPath);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    file=new File(Paths.downloadPath);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                } else {
                    //I should ask my dear users again
                    showToast("FUCK YOU. APP will die anytime");
                }
            break;
        }
        startNewActivity();
    }


    private void startNewActivity(){
        isAbleToStartActivity++;
        if(isAbleToStartActivity ==2) {
            //通过令牌决定下一步操作
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
    }

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onExit() {

    }
}
