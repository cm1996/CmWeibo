package ssdut.chenmo.cmweibo.fragment;


import android.content.Intent;
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

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.activity.UserActivity;
import ssdut.chenmo.cmweibo.adapter.DividerItemDecoration;
import ssdut.chenmo.cmweibo.adapter.RcvAdapter;
import ssdut.chenmo.cmweibo.adapter.RecyclerViewAdapter;

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
    public static int MSG2 = 1002;


    @BindView(R.id.rv_weibo)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.fab_menu)
    public FloatingActionsMenu mFAM;

    private boolean loading = false; //判断是否正在进行上拉加载
    List<Status> mWeibos = new ArrayList<>();
    RcvAdapter mAdapter;


    @Override
    protected void initDatas() {

        //配置RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //mRecyclerView.setAdapter(mAdapter = new RcvAdapter(context,mWeibos));
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setAdapter(new RecyclerViewAdapter(context, mWeibos,
                new RecyclerViewAdapter.MultiItemTypeSupport() {
            @Override
            public int getLayoutId(int itemType) {
                return itemType;
            }

            @Override
            public int getItemViewType(int position, Object o) {
                if(o==null){
                    return R.layout.layout_weibo_footer;
                } else if(((Status)o).retweeted_status==null){ //说明是原创微博
                    return R.layout.layout_weibo_item;
                } else {
                    return R.layout.layout_weibo_retweet_item;
                }
            }
        }) {
            @Override
            public void convert(ssdut.chenmo.cmweibo.adapter.ViewHolder holder, Object o, int position) {
                switch (getItemViewType(position)){

                }
            }
        });

        mAdapter.setOnItemClickListener(new RcvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showToast("fuckyou");
            }
        });
        mAdapter.setOnItemAvatarClickListener(new RcvAdapter.OnItemAvatarClickListener() {
            @Override
            public void onItemAvatarClick(View view, int position) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("user_name", mWeibos.get(position).user.screen_name);
                intent.putExtra("user_guanzhu", ""+mWeibos.get(position).user.friends_count);
                intent.putExtra("user_fensi", ""+mWeibos.get(position).user.followers_count);
                intent.putExtra("user_description", mWeibos.get(position).user.description);
                intent.putExtra("user_avatar", mWeibos.get(position).user.avatar_large);
                intent.putExtra("user_id", mWeibos.get(position).user.id);

                startActivity(intent);
                playOpenAnimation();
            }
        });
        mAdapter.setOnItemRetweetClickListener(new RcvAdapter.OnItemRetweetClickListener() {
            @Override
            public void onItemRetweetClick(View view, int position) {

            }
        });
        if(mWeiboDataProvider!=null){
            mWeiboDataProvider.updateData(0L,0L,true);
        }

        //处理下拉刷新
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新的时候做什么
                if(mWeiboDataProvider!=null){
                    if(mWeibos.isEmpty())
                        mWeiboDataProvider.updateData(0L,0L,true);
                    else
                        mWeiboDataProvider.updateData(Long.parseLong(mWeibos.get(0).id),0L,true);
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

                    if(mWeiboDataProvider!=null){
                        if(mWeibos.size()>1){
                            mWeiboDataProvider.updateData(0L,Long.parseLong(mWeibos.get(mWeibos.size()-2).id),false);
                        } else {
                            mWeiboDataProvider.updateData(0L,0L,false);

                        }
                    }
                }


            }
        });
        /*new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){
                    try {
                        sleep(1000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.e(""+context,"!!!"+mSwipeRefreshLayout.getHeight()+"  "+mRecyclerView.getHeight());

                    Log.e(""+context,"???"+mSwipeRefreshLayout.getMeasuredHeight()+"  "+mRecyclerView.getMeasuredHeight());
                }
            }
        }.start();*/

    }


    @OnClick(R.id.fab_to_top)
    public void toTop(View v){
        int position = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).
                findLastVisibleItemPosition();
        if(position>10)
            mRecyclerView.scrollToPosition(5);
        mRecyclerView.smoothScrollToPosition(0);
        mFAM.toggle();
    }

    @OnClick(R.id.fab_to_bottom)
    public void toBottom(View v){
        int position = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).
                findLastVisibleItemPosition();
        if(mAdapter.getItemCount()-position>10)
            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-5);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
        mFAM.toggle();
    }

    @OnClick(R.id.fab_send_weibo)
    public void sendWeibo(View v){
        mFAM.toggle();
    }

    @Override
    protected int initLayoutRes() {
        return R.layout.fragment_main;
    }

    /**
     *  接口中的三个函数还不确定，取决于微博API提供了怎样要数据的方法
     *  */
    public interface WeiboDataProvider{
        void updateData(long since_id, long max_id , boolean isNew);
    }

    private WeiboDataProvider mWeiboDataProvider;

    public void setWeiboDataProvider(WeiboDataProvider weiboDataProvider){
        mWeiboDataProvider = weiboDataProvider;
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==MSG1||msg.what==MSG2){
                String response = msg.getData().getString("response");
                StatusList statuses = StatusList.parse(response);
                if (statuses != null && statuses.statusList!=null && statuses.total_number > 0) {
                    Toast.makeText(MainFragment.this.context,
                            "获取微博信息流成功, 条数: " + statuses.statusList.size(),
                            Toast.LENGTH_LONG).show();
                    if(msg.what==MSG1){
                        mWeibos.addAll(0,statuses.statusList);
                    } else if(msg.what==MSG2) {
                        loading = false;
                        mWeibos.remove(mWeibos.size()-1);
                        mWeibos.addAll(statuses.statusList);
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.notifyDataSetChanged();

                }
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }
    };
}