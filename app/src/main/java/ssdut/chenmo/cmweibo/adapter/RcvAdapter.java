package ssdut.chenmo.cmweibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

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

    //绑定ViewHolder   添加数据
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).rcvLoadMore.setVisibility(View.VISIBLE);
            return;
        } else if(holder instanceof NormalWeiboViewHolder) {
            ((NormalWeiboViewHolder)holder).text.setText(data.get(position).text);
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

                ((NormalWeiboViewHolder)holder).text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v,holder.getLayoutPosition());
                    }
                });
            }
        } else if(holder instanceof RetweetWeiboViewHolder) {
            ((RetweetWeiboViewHolder)holder).text.setText(data.get(position).text);
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
            ((RetweetWeiboViewHolder)holder).retweetText.setText("@"+
                    data.get(position).retweeted_status.user.screen_name+
                    ": "+
                    data.get(position).retweeted_status.text);
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

}
