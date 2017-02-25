package ssdut.chenmo.cmweibo.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ssdut.chenmo.cmweibo.R;

/**
 * Created by chenmo on 2016/9/18.
 */
public class RetweetWeiboViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.weibo_item_avatar)
    public ImageView avatar;
    @BindView(R.id.weibo_item_name)
    public TextView name;
    @BindView(R.id.weibo_item_time_sourse)
    public  TextView timeAndSourse;
    @BindView(R.id.weibo_item_text)
    public TextView text;

    @BindView(R.id.weibo_retweet_item_text)
    public TextView retweetText;
    @BindView(R.id.weibo_retweet_item_pics)
    public GridLayout gl_pics;

    @BindView(R.id.weibo_item_retweet)
    public TextView retweet;
    @BindView(R.id.weibo_item_comment)
    public TextView comment;
    @BindView(R.id.rl1)
    public RelativeLayout retweetCard;
    @BindView(R.id.rl)
    public RelativeLayout baseCard;

    public RetweetWeiboViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
