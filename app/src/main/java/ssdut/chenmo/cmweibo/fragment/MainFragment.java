package ssdut.chenmo.cmweibo.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import  android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.adapter.RcvAdapter;

/**
 * A simple {@link Fragment} subclass.
 * This fragment(with its adapter of RecyclerView) will be used in several Activities to show a list of weibo.
 *
 * So what we should know is that although the form of the list item is the same, but their datas are not
 * so create a interface to each activity and let's make difference in activity but not fragment
 *
 * fuck!
 * fuck!
 * fuck!
 * WRONG!!!
 */
public class MainFragment extends BaseFragment {

    public static int MSG1 = 1001;

    @BindView(R.id.rv_weibo)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean loading = false; //判断是否正在进行上拉加载
    List<Status> mWeibos = new ArrayList<>();
    RcvAdapter mAdapter;


    @Override
    protected void initDatas() {

        //配置RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter = new RcvAdapter(context,mWeibos));

        /*if(mWeiboDataProvider!=null){
            mWeiboDataProvider.updateData();
        }*/

        //处理下拉刷新
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新的时候做什么
                if(mWeiboDataProvider!=null){
                    if(mWeibos.isEmpty())
                        mWeiboDataProvider.updateData(0L);
                    else
                        mWeiboDataProvider.updateData(Long.parseLong(mWeibos.get(0).id));
                }

            }
        });

        //上拉加载
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int totalItemCount = layoutManager.getItemCount();

                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!loading && totalItemCount < (lastVisibleItem + 2)) {
                    if(mWeibos.get(mWeibos.size()-1)!=null){
                        mWeibos.add(null);
                        mAdapter.notifyDataSetChanged();
                    }

                    showToast("你正在上拉加载");
                    loading = true;  //表示正在联网加载新数据
                    // 。。。。。。好了加载完了

                   /* if(mWeiboDataProvider!=null){
                        mStrings = mWeiboDataProvider.getDownData();
                    }*/

                    loading = false;
                    mWeibos.remove(mWeibos.size()-1);
                    mAdapter.notifyDataSetChanged();

                }


            }
        });
    }



    @Override
    protected int initLayoutRes() {
        return R.layout.fragment_main;
    }

    /**
     *  接口中的三个函数还不确定，取决于微博API提供了怎样要数据的方法
     *  */
    public interface WeiboDataProvider{
        void updateData(long since_id);
    }

    private WeiboDataProvider mWeiboDataProvider;

    public void setWeiboDataProvider(WeiboDataProvider weiboDataProvider){
        mWeiboDataProvider = weiboDataProvider;
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==MSG1){
                String response = msg.getData().getString("response");
                StatusList statuses = StatusList.parse(response);
                if (statuses != null && statuses.total_number > 0) {
                    Toast.makeText(MainFragment.this.context,
                            "获取微博信息流成功, 条数: " + statuses.statusList.size(),
                            Toast.LENGTH_LONG).show();
                    mWeibos.addAll(0,statuses.statusList);
                    mAdapter.notifyDataSetChanged();

                }
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }
    };
}
