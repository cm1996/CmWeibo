package ssdut.chenmo.cmweibo.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.List;

import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.activity.WelcomeActivity;
import ssdut.chenmo.cmweibo.adapter.viewholder.FooterViewHolder;
import ssdut.chenmo.cmweibo.adapter.viewholder.NormalWeiboViewHolder;
import ssdut.chenmo.cmweibo.adapter.viewholder.RetweetWeiboViewHolder;
import ssdut.chenmo.cmweibo.application.MyApplication;

/**
 * Created by chenmo on 2016/9/6.
 */
public class RcvAdapter extends RecyclerView.Adapter {

    Activity mActivity;
    List<Status> data;
    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_FOOTER = 1;
    public final static int TYPE_RETWEET = 2;


    //构造函数可能需要好好设计，看看要传入什么参数
    public RcvAdapter(Context context, List<Status> data) {
        mActivity = (Activity) context;
        this.data = data;
    }


    @Override
    public int getItemViewType(int position) {

        if(data.get(position)==null)
            return TYPE_FOOTER;
        if(data.get(position).retweeted_status==null)
            return TYPE_NORMAL;
        return TYPE_RETWEET;
    }

    //创建ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_NORMAL:
                NormalWeiboViewHolder holder = new NormalWeiboViewHolder(LayoutInflater.from(mActivity)
                        .inflate(R.layout.layout_weibo_item,parent,false));
                return holder;
            case TYPE_FOOTER:
                FooterViewHolder footerHolder = new FooterViewHolder(LayoutInflater.from(mActivity)
                        .inflate(R.layout.layout_weibo_footer,parent,false));
                return footerHolder;
            case TYPE_RETWEET:
                RetweetWeiboViewHolder retweetHolder = new RetweetWeiboViewHolder(LayoutInflater.from(mActivity)
                        .inflate(R.layout.layout_weibo_retweet_item,parent,false));
                return retweetHolder;

        }
        return null;//不可能执行 ！！！
    }

    private SpannableStringBuilder highlightClickable(String text) {
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
                    if(((MyApplication)mActivity.getApplicationContext())
                            .mBitmapEmotions.containsKey(text.substring(startIndex,endIndex))) {

                        Bitmap bitmap = ((MyApplication)mActivity.getApplicationContext())
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
            Toast.makeText(mActivity,"点击了 "+highlightText,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }




    //绑定ViewHolder   添加数据
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).rcvLoadMore.setVisibility(View.VISIBLE);
            return;
        } else if(holder instanceof NormalWeiboViewHolder) {
            /**
             *  非转发类微博的数据装载
             */


           // Log.e("ID is "," "+data.get(position).id);
            ((NormalWeiboViewHolder)holder).text.
                    setMovementMethod(LinkMovementMethod.getInstance());
            ((NormalWeiboViewHolder)holder).text.
                    setText(highlightClickable(data.get(position).text));
            ((NormalWeiboViewHolder)holder).name.
                    setText(data.get(position).user.screen_name);
            //暂时只显示时间 因为来源似乎需要解析一下
            ((NormalWeiboViewHolder)holder).timeAndSourse.
                    setText(data.get(position).created_at/*+" "+data.get(position).source*/);



            if(data.get(position).thumbnail_pic!=null)
            Log.e("图片地址",data.get(position).thumbnail_pic);


            ImageLoader.getInstance().loadImage(data.get(position).user.profile_image_url
                    , WelcomeActivity.mOptions,new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {}
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    ((NormalWeiboViewHolder)holder).avatar.setImageBitmap(loadedImage);
                }
                @Override
                public void onLoadingCancelled(String imageUri, View view) {}
            });

            GridLayout gl = ((NormalWeiboViewHolder)holder).gl_pics;
            if(data.get(position).pic_urls==null)
            Log.e("@@@"+position,"");

            if(data.get(position).pic_urls!=null){
                Log.e("@@@"+position,"  "+data.get(position).pic_urls.size());
                gl.removeAllViews();

                for(int i =0;i<data.get(position).pic_urls.size();i++){
                    final ImageView iv = new ImageView(mActivity);
                    int l = gl.getWidth();
                    iv.setPadding(4,4,4,4);

                    iv.setMinimumWidth(l/3);
                    iv.setMaxWidth(l/3);
                    iv.setMaxHeight(l/3);
                    iv.setMinimumHeight(l/3);
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    gl.addView(iv);
                    ImageLoader.getInstance().loadImage(data.get(position).pic_urls.get(i)
                            , WelcomeActivity.mOptions,
                            new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {}

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    iv.setImageBitmap(loadedImage);
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {}
                            });
                }
            }
            ((NormalWeiboViewHolder)holder).retweet.setText("转发 "+data.get(position).comments_count);
            ((NormalWeiboViewHolder)holder).comment.setText("评论 "+data.get(position).reposts_count);






            if(mOnItemClickListener!=null){  //如果已经设置了监听器

                ((NormalWeiboViewHolder)holder).baseCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v,holder.getLayoutPosition());
                    }
                });
            }
            if(mOnItemAvatarClickListener!=null) {
                ((NormalWeiboViewHolder)holder).avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemAvatarClickListener.onItemAvatarClick(v,holder.getLayoutPosition());
                    }
                });
            }



        } else if(holder instanceof RetweetWeiboViewHolder) {
            ((RetweetWeiboViewHolder)holder).text.
                    setMovementMethod(LinkMovementMethod.getInstance());
            ((RetweetWeiboViewHolder)holder).text.
                    setText(highlightClickable(data.get(position).text));
            ((RetweetWeiboViewHolder)holder).name.
                    setText(data.get(position).user.screen_name);
            //暂时只显示时间 因为来源似乎需要解析一下
            ((RetweetWeiboViewHolder)holder).timeAndSourse.
                    setText(data.get(position).created_at/*+" "+data.get(position).source*/);

            ImageLoader.getInstance().loadImage(data.get(position).user.profile_image_url
                    , WelcomeActivity.mOptions,new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {}
                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            ((RetweetWeiboViewHolder)holder).avatar.setImageBitmap(loadedImage);
                        }
                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {}
                    });
            ((RetweetWeiboViewHolder)holder).retweetText.
                    setMovementMethod(LinkMovementMethod.getInstance());
            ((RetweetWeiboViewHolder)holder).retweetText.setText(highlightClickable("@"+
                    data.get(position).retweeted_status.user.screen_name+
                    ": "+
                    data.get(position).retweeted_status.text));
            ((RetweetWeiboViewHolder)holder).retweet.setText("转发 "+data.get(position).comments_count);
            ((RetweetWeiboViewHolder)holder).comment.setText("评论 "+data.get(position).reposts_count);
            GridLayout gl = ((RetweetWeiboViewHolder)holder).gl_pics;
            if(data.get(position).retweeted_status.pic_urls!=null){
                gl.removeAllViews();
                for(int i =0;i<data.get(position).retweeted_status.pic_urls.size();i++){
                    final ImageView iv = new ImageView(mActivity);
                    int l = gl.getWidth();
                    iv.setPadding(4,4,4,4);
                    iv.setMinimumWidth(l/3);
                    iv.setMaxWidth(l/3);
                    iv.setMaxHeight(l/3);
                    iv.setMinimumHeight(l/3);
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    gl.addView(iv);
                    ImageLoader.getInstance().loadImage(data.get(position).retweeted_status.pic_urls.get(i)
                            , WelcomeActivity.mOptions,
                            new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {}

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    iv.setImageBitmap(loadedImage);
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {}
                            });
                }
            }

            ((RetweetWeiboViewHolder)holder).text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((RetweetWeiboViewHolder)holder).text.getSelectionStart() == -1
                            && ((RetweetWeiboViewHolder)holder).text.getSelectionEnd() == -1)
                    Toast.makeText(mActivity,"text",Toast.LENGTH_SHORT).show();
                }
            });

            if(mOnItemClickListener!=null){  //如果已经设置了监听器

                ((RetweetWeiboViewHolder)holder).baseCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v,holder.getLayoutPosition());
                    }
                });
            }
            if(mOnItemAvatarClickListener!=null) {
                ((RetweetWeiboViewHolder)holder).avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemAvatarClickListener.onItemAvatarClick(v,holder.getLayoutPosition());
                    }
                });
            }
            if(mOnItemAvatarClickListener!=null) {
                ((RetweetWeiboViewHolder)holder).retweetCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemRetweetClickListener.onItemRetweetClick(v,holder.getLayoutPosition());
                    }
                });
            }
        }


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    //实现点击事件的接口
    public interface OnItemClickListener{
        void onItemClick(View view ,int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){

        mOnItemClickListener = onItemClickListener;
    }

    //实现转发原内容点击事件
    public interface OnItemRetweetClickListener{
        void onItemRetweetClick(View view ,int position);
    }

    private OnItemRetweetClickListener mOnItemRetweetClickListener;

    public void setOnItemRetweetClickListener(OnItemRetweetClickListener onItemRetweetClickListener){

        mOnItemRetweetClickListener = onItemRetweetClickListener;
    }
    //实现头像点击事件
    public interface OnItemAvatarClickListener{
        void onItemAvatarClick(View view ,int position);
    }

    private OnItemAvatarClickListener mOnItemAvatarClickListener;

    public void setOnItemAvatarClickListener(OnItemAvatarClickListener onItemAvatarClickListener){

        mOnItemAvatarClickListener = onItemAvatarClickListener;
    }

}
