package ssdut.chenmo.cmweibo.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ssdut.chenmo.cmweibo.R;

/**
 * Created by chenmo on 2016/9/6.
 */
public class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView tv;
    public MyViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.tv_weibo_item);
    }
}
