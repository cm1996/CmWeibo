package ssdut.chenmo.cmweibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.adapter.viewholder.FooterViewHolder;
import ssdut.chenmo.cmweibo.adapter.viewholder.MyViewHolder;

/**
 * Created by chenmo on 2016/9/6.
 */
public class RcvAdapter extends RecyclerView.Adapter {

    Activity mActivity;
    List<Status> data;  //数据集 肯定不是String类型  先暂时这么弄着
    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_FOOTER = 1;

    //构造函数可能需要好好设计，看看要传入什么参数
    public RcvAdapter(Context context, List<Status> data) {
        mActivity = (Activity) context;
        this.data = data;
    }


    @Override
    public int getItemViewType(int position) {

        if(data.get(position)==null)
            return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    //创建ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_NORMAL:
                MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mActivity)
                        .inflate(R.layout.layout_weibo_item,parent,false));
                return holder;
            case TYPE_FOOTER:
                FooterViewHolder footerHolder = new FooterViewHolder(LayoutInflater.from(mActivity)
                        .inflate(R.layout.layout_weibo_footer,parent,false));
                return footerHolder;
        }
        return null;//不可能执行 ！！！
    }

    //绑定ViewHolder   添加数据
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).rcvLoadMore.setVisibility(View.VISIBLE);
            return;
        }

        ((MyViewHolder)holder).tv.setText(data.get(position).text);
        if(mOnItemClickListener!=null){  //如果已经设置了监听器

            ((MyViewHolder)holder).tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v,holder.getLayoutPosition());
                }
            });
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
