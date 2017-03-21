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

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
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
import ssdut.chenmo.cmweibo.activity.WeiboActivity;
import ssdut.chenmo.cmweibo.adapter.DividerItemDecoration;
import ssdut.chenmo.cmweibo.adapter.RcvAdapter;
import ssdut.chenmo.cmweibo.adapter.RecyclerViewAdapter;
import ssdut.chenmo.cmweibo.cusview.NineImageGallery;
import ssdut.chenmo.cmweibo.dialog.SendWeiboDialog;
import ssdut.chenmo.cmweibo.utils.BitmapUtils;
import ssdut.chenmo.cmweibo.utils.ImageLoaderUtils;
import ssdut.chenmo.cmweibo.utils.TextSpanUtils;

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

    //private boolean loading = false; //判断是否正在进行上拉加载
    List<Status> mWeibos = new ArrayList<>();
    RcvAdapter mAdapter;
    RecyclerViewAdapter mRecyclerViewAdapter;

    protected int recyclerViewScrollState;
    public static final int recyclerViewIsScroll = 0;
    public static final int recyclerViewIsNotScroll = 1;

    Status mEmptyStatus = new Status();



    @Override
    protected void initDatas() {

        mEmptyStatus.id = "-1";

        //配置RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //mRecyclerView.setAdapter(mAdapter = new RcvAdapter(context,mWeibos));
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));



        mRecyclerView.setAdapter(mRecyclerViewAdapter = new RecyclerViewAdapter(context, mWeibos,
                new RecyclerViewAdapter.MultiItemTypeSupport() {
                    //说明:我们将layoutId和Type直接相等
            @Override
            public int getLayoutId(int itemType) {
                return itemType;
            }

            @Override
            public int getItemViewType(int position, Object o) {
                if(((Status)o).id.equals("-1")){
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
                if(getItemViewType(position)==R.layout.layout_weibo_footer){
                    holder.setOnConvertViewClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mWeiboDataProvider!=null){
                                if(mWeibos.size()>1){
                                    mWeiboDataProvider.updateData(0L,Long.parseLong(mWeibos.get(mWeibos.size()-2).id),false);
                                } else {
                                    mWeiboDataProvider.updateData(0L,0L,false);

                                }
                            }
                        }
                    });
                } else if(getItemViewType(position)==R.layout.layout_weibo_item){
                    TextView text = holder.getView(R.id.weibo_item_text);
                    text.setMovementMethod(LinkMovementMethod.getInstance());
                    text.setText(TextSpanUtils.getInstance().highlightClickable(((Status) o).text,
                                    new TextSpanUtils.OnSpanClickListener() {
                                @Override
                                public void OnClick(String s) {
                                    showToast(s);
                                }
                            }));
                    holder.setText(R.id.weibo_item_name,((Status)o).user.screen_name+" "+position);
                    holder.setText(R.id.weibo_item_time_sourse,((Status)o).created_at);

                    NineImageGallery nineImageGallery = holder.getView(R.id.weibo_item_pics);
                    if(((Status)o).pic_urls!=null){
                        nineImageGallery.addImageByUrl(((Status)o).pic_urls);
                        Log.e("有 ","位置 "+position+" "+((Status)o).pic_urls.size());

                    } else {
                        ((NineImageGallery)holder.getView(R.id.weibo_item_pics)).clearAllTag();
                    }
                    String tag = "avatar"+position;
                    ImageView avatar = holder.getView(R.id.weibo_item_avatar);
                    avatar.setTag(tag);

                    ImageLoaderUtils.bindBitmap(avatar, tag,
                            ((Status)o).user.profile_image_url);
                    holder.setText(R.id.weibo_item_comment,"评论 "+((Status)o).comments_count);
                    holder.setText(R.id.weibo_item_retweet,"转发 "+((Status)o).reposts_count);
                    holder.setOnConvertViewClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(context, WeiboActivity.class));
                        }
                    });

                } else {
                    TextView text = holder.getView(R.id.weibo_item_text);
                    text.setMovementMethod(LinkMovementMethod.getInstance());
                    text.setText(TextSpanUtils.getInstance().highlightClickable(((Status) o).text,
                            new TextSpanUtils.OnSpanClickListener() {
                                @Override
                                public void OnClick(String s) {
                                    showToast(s);
                                }
                            }));
                    TextView retweetText = holder.getView(R.id.weibo_retweet_item_text);
                    retweetText.setMovementMethod(LinkMovementMethod.getInstance());
                    retweetText.setText(TextSpanUtils.getInstance().highlightClickable(
                            "@"+ ((Status)o).retweeted_status.user.screen_name+
                                    ": "+((Status)o).retweeted_status.text,
                            new TextSpanUtils.OnSpanClickListener() {
                                @Override
                                public void OnClick(String s) {
                                    showToast(s);
                                }
                            }));
                    holder.setText(R.id.weibo_item_name,((Status)o).user.screen_name+" "+position);
                    holder.setText(R.id.weibo_item_time_sourse,((Status)o).created_at);
                    NineImageGallery nineImageGallery = holder.getView(R.id.weibo_retweet_item_pics);
                    if(((Status)o).retweeted_status.pic_urls!=null){
                        nineImageGallery.addImageByUrl(((Status)o).retweeted_status.pic_urls);
                        Log.e("有 ","位置 "+position+" "+((Status)o).retweeted_status.pic_urls.size());
                    } else {
                        ((NineImageGallery)holder.getView(R.id.weibo_retweet_item_pics)).clearAllTag();
                    }
                    String tag = "avatar"+position;
                    ImageView avatar = (ImageView) holder.getView(R.id.weibo_item_avatar);
                    avatar.setTag(tag);
                    ImageLoaderUtils.bindBitmap(avatar, tag,
                            ((Status)o).user.profile_image_url);
                    holder.setText(R.id.weibo_item_comment,"评论 "+((Status)o).comments_count);
                    holder.setText(R.id.weibo_item_retweet,"转发 "+((Status)o).reposts_count);
                    holder.setOnConvertViewClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(context, WeiboActivity.class));
                        }
                    });
                    holder.setOnClickListener(R.id.rl1, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(context, WeiboActivity.class));
                        }
                    });
                }
            }
        });

      /*  mAdapter.setOnItemClickListener(new RcvAdapter.OnItemClickListener() {
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
        });*/
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

    }


    @OnClick(R.id.fab_to_top)
    public void toTop(View v){
        if(mWeibos.size()>1) {
            int position = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).
                    findLastVisibleItemPosition();
            if(position>10)
                mRecyclerView.scrollToPosition(5);
            mRecyclerView.smoothScrollToPosition(0);
        }
        mFAM.toggle();
    }

    @OnClick(R.id.fab_to_bottom)
    public void toBottom(View v){
        if(mWeibos.size()>1) {
            int position = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).
                    findLastVisibleItemPosition();
            if(mRecyclerViewAdapter.getItemCount()-position>10)
                mRecyclerView.scrollToPosition(mRecyclerViewAdapter.getItemCount()-5);
            mRecyclerView.smoothScrollToPosition(mRecyclerViewAdapter.getItemCount()-1);
        }
        mFAM.toggle();
    }

    @OnClick(R.id.fab_send_weibo)
    public void sendWeibo(View v){
        mFAM.toggle();
        SendWeiboDialog sendWeiboDialog = new SendWeiboDialog(context);
        sendWeiboDialog.setOnPoClickListener(new SendWeiboDialog.OnPoClickListener() {
            @Override
            public void onPoClick(View v, String s) {
                if(mWeiboDataProvider!=null) {
                    mWeiboDataProvider.sendNewWeibo(s);
                }
            }
        });
        sendWeiboDialog.show();
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
        void sendNewWeibo(String s);
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
                    if(msg.what==MSG1){                     //上拉加载
                        boolean isFirstTime = false;
                        if(mWeibos.size()==0){
                            isFirstTime = true;
                        }
                        mWeibos.addAll(0,statuses.statusList);
                        if(isFirstTime){
                            mWeibos.add(mEmptyStatus);
                        }

                    } else if(msg.what==MSG2) {            //下拉刷新
                        //loading = false;
                        mWeibos.remove(mWeibos.size()-1);
                        mWeibos.addAll(statuses.statusList);
                        mWeibos.add(mEmptyStatus);
                    }
                    mRecyclerViewAdapter.notifyDataSetChanged();

                }
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }
    };
}