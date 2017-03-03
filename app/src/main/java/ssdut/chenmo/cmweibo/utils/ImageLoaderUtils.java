package ssdut.chenmo.cmweibo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import ssdut.chenmo.cmweibo.R;

/**
 * Created by chenmo on 2017/2/26.
 */

public class ImageLoaderUtils {

    public static void bindBitmap(final ImageView imageView, final String tag, final String uri) {
        //ImageLoader.getInstance().displayImage(uri,imageView);
        ImageLoader.getInstance().loadImage(uri, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if( ((String)imageView.getTag()).equals(tag) ){
                    Log.e("true ","right pic");
                    Log.e("tag is ",(String)imageView.getTag());
                    Log.e("url is ",uri);
                    imageView.setImageBitmap(loadedImage);
                } else {
                    Log.e("false ","wrong pic");
                    Log.e("tag is ",(String)imageView.getTag());
                    Log.e("url is ",uri);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

}
