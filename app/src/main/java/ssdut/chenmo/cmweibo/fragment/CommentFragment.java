package ssdut.chenmo.cmweibo.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.activity.MainActivity;
import ssdut.chenmo.cmweibo.adapter.DividerItemDecoration;
import ssdut.chenmo.cmweibo.adapter.RecyclerViewAdapter;
import ssdut.chenmo.cmweibo.adapter.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends BaseFragment {

    Oauth2AccessToken mOauth2AccessToken;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRecyclerViewAdapter;
    List<Comment> mDatas = new ArrayList<>();

    @Override
    protected void initDatas() {
        mOauth2AccessToken = ((MainActivity)context).mOauth2AccessToken;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mRecyclerViewAdapter = new RecyclerViewAdapter(context, mDatas,
                new RecyclerViewAdapter.MultiItemTypeSupport() {
            @Override
            public int getLayoutId(int itemType) {
                return 0;
            }

            @Override
            public int getItemViewType(int position, Object o) {
                return 0;
            }
        }) {
            @Override
            public void convert(ViewHolder holder, Object o, int position) {

            }
        });
    }

    @Override
    protected int initLayoutRes() {
        return R.layout.fragment_comment;
    }

}
