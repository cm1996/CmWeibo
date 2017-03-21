package ssdut.chenmo.cmweibo.fragment;


import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sina.weibo.sdk.api.share.Base;

import butterknife.BindView;
import ssdut.chenmo.cmweibo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    Fragment[] mFragments;

    @Override
    protected void initDatas() {
        mFragments = new Fragment[]{new CommentFragment(), new AtFragment(), new ChatFragment()};
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return new String[]{"评论","@","私信"}[position];
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    protected int initLayoutRes() {
        return R.layout.fragment_message;
    }
}