package ssdut.chenmo.cmweibo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ssdut.chenmo.cmweibo.adapter.RcvAdapter;
import ssdut.chenmo.cmweibo.application.MyApplication;

/**
 * Created by chenmo on 2017/2/26.
 */

public class TextSpanUtils {

    public static TextSpanUtils instance = new TextSpanUtils();

    private TextSpanUtils(){}

    public static TextSpanUtils getInstance(){
        return instance;
    }

    Context mContext = MyApplication.getInstance();

    public SpannableStringBuilder highlightClickable(String text,
                                                     OnSpanClickListener onSpanClickListener) {
        mOnSpanClickListener = onSpanClickListener;
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        //所有 @ 都高亮可点击
        boolean isFindAt = false;
        int startIndex = 0;
        int endIndex = 0;
        for(int i = 0; i<text.length();i++) {
            if(!isFindAt) {
                if(text.charAt(i)=='@') {
                    startIndex = i;
                    isFindAt = true;
                }
            } else {
                if(!(text.charAt(i)>='a'&&text.charAt(i)<='z'
                        ||text.charAt(i)>='A'&&text.charAt(i)<='Z'
                        ||text.charAt(i)>='0'&&text.charAt(i)<='9'
                        ||text.charAt(i)=='_'
                        ||text.charAt(i)=='-'
                        ||text.charAt(i)>=0x4E00&&text.charAt(i)<=0x9FA5)) {
                    endIndex = i;
                    builder.setSpan(new MyClickableSpan(text.substring(startIndex,endIndex))
                            , startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if(text.charAt(i)=='@') {
                        startIndex = i;
                        isFindAt = true;
                    } else {
                        isFindAt = false;
                    }
                } else if(i == text.length()-1) {

                    endIndex = text.length();
                    builder.setSpan(new MyClickableSpan(text.substring(startIndex,endIndex))
                            , startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        //所有##都可点击
        boolean isFindSharp = false;
        startIndex = 0;
        endIndex = 0;
        for(int i = 0; i<text.length();i++) {
            if(text.charAt(i)=='#'){
                if(!isFindSharp) {
                    startIndex = i;
                    isFindSharp = true;
                } else {
                    endIndex = i+1;
                    isFindSharp = false;
                    if(!text.substring(startIndex, endIndex).contains("@")) {
                        builder.setSpan(new MyClickableSpan(text.substring(startIndex,endIndex))
                                , startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        //实现表情
        boolean isFindEmotion = false;
        startIndex = 0;
        endIndex = 0;
        for(int i = 0; i<text.length();i++) {
            if(!isFindEmotion) {
                if(text.charAt(i)=='[') {
                    startIndex = i;
                    isFindEmotion =true;
                }
            } else {
                if(text.charAt(i)==']') {
                    endIndex = i+1;
                    isFindEmotion = false;
                    Log.e("find a emotion",text.substring(startIndex,endIndex));
                    if(((MyApplication)mContext)
                            .mBitmapEmotions.containsKey(text.substring(startIndex,endIndex))) {

                        Bitmap bitmap = ((MyApplication)mContext.getApplicationContext())
                                .mBitmapEmotions.get(text.substring(startIndex,endIndex));

                        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                        bitmapDrawable.setBounds(0,0,50,50);
                        builder.setSpan(new ImageSpan(bitmapDrawable),startIndex
                                ,endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                }
            }
        }

        return builder;
    }

    class MyClickableSpan extends ClickableSpan {

        String highlightText = "";

        MyClickableSpan(String highlightText) {
            this.highlightText = highlightText;
        }

        @Override
        public void onClick(View widget) {
            if(mOnSpanClickListener!=null){
                mOnSpanClickListener.OnClick(highlightText);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    public OnSpanClickListener mOnSpanClickListener;

    public interface OnSpanClickListener{
        public void OnClick(String s);
    }
}
