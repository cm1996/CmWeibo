package ssdut.chenmo.cmweibo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by chenmo on 2017/2/25.
 * 这个Adapter是看的hongyang博客关于RecyclerView的通用Adapter设计而写的
 *
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public RecyclerViewAdapter(Context context, List<T> datas, MultiItemTypeSupport<T> multiItemSupport){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mMultiItemTypeSupport = multiItemSupport;

    }

    @Override
    public int getItemViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position,mDatas.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        ViewHolder holder = ViewHolder.get(mContext, parent, layoutId);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        convert((ViewHolder) holder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(ViewHolder holder, T t, int position);

    public interface MultiItemTypeSupport<T> {

        int getLayoutId(int itemType);


        int getItemViewType(int position, T t);
    }

}
