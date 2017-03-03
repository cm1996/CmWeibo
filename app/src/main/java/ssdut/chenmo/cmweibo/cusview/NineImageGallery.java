package ssdut.chenmo.cmweibo.cusview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.utils.ImageLoaderUtils;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by chenmo on 2017/2/27.
 */

public class NineImageGallery extends GridLayout {

    protected Context mContext;

    protected ArrayList<SquareImageView> mSquareImageViews = new ArrayList<>();

    private String tag;

    public NineImageGallery(final Context context) {
        super(context);
        init(context);
    }

    public NineImageGallery(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public NineImageGallery(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        init(context);
    }


    public void init(Context context){
        mContext = context;
        WindowManager windowManager = (WindowManager)mContext.getSystemService(WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        for(int i = 0;i<9;i++) {
            SquareImageView squareImageView = new SquareImageView(mContext);
            squareImageView.setMinimumWidth(width/4);
            squareImageView.setMaxWidth(width/4);
            squareImageView.setMinimumHeight(width/4);
            squareImageView.setMaxHeight(width/4);
            mSquareImageViews.add(squareImageView);
        }
    }

    public void addImageByUrl(final List<String> urls){
        removeAllViews();
        for(int i = 0;i<urls.size();i++) {
            tag = urls.get(i);
            mSquareImageViews.get(i).setTag(tag);
            mSquareImageViews.get(i).setImageResource(R.drawable.bg_no_res_image);
            addView(mSquareImageViews.get(i));
        }
        for(int i = urls.size();i<mSquareImageViews.size();i++) {
            tag = "";
            mSquareImageViews.get(i).setTag(tag);
        }
        for(int i = 0;i<urls.size();i++) {
            ImageLoaderUtils.bindBitmap(mSquareImageViews.get(i), urls.get(i), urls.get(i));
        }
    }

    public void clearAllTag(){
        removeAllViews();
        tag = "";
        for(int i = 0;i<mSquareImageViews.size();i++) {
            mSquareImageViews.get(i).setTag(tag);
        }
    }

    public class SquareImageView extends ImageView {

        public SquareImageView(Context context) {
            super(context);
            setPadding(4,4,4,4);
            setScaleType(ScaleType.FIT_XY);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
