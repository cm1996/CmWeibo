package ssdut.chenmo.cmweibo.fragment;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.activity.BaseActivity;
import ssdut.chenmo.cmweibo.activity.BaseFragmentActivity;
import ssdut.chenmo.cmweibo.activity.MainActivity;
import ssdut.chenmo.cmweibo.activity.SelfActivity;
import ssdut.chenmo.cmweibo.activity.WelcomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftMenuFragment extends BaseFragment {


    @BindView(R.id.head_imageview)
    public CircleImageView mHead;
    @BindView(R.id.screen_name)
    public TextView mScreenName;
    @BindView(R.id.tv_description)
    public TextView mDescription;
    @BindView(R.id.menu_listview)
    public ListView mListView;
    @BindView(R.id.ll1)
    public LinearLayout ll;


    String[] mStrings = {"首页","消息","发现","设置"};
    BaseAdapter mBaseAdapter;

    @Override
    protected void initDatas() {
        //初始化适配器,为ListView装入数据
        //mListView.setAdapter(mAdapter);
        mBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LinearLayout ll = (LinearLayout)LayoutInflater.from(context).
                        inflate(R.layout.layout_left_menu_list_item, parent,false);
                ImageView iv = (ImageView) ll.findViewById(R.id.icon);
                TextView tv = (TextView) ll.findViewById(R.id.text);
                tv.setText(mStrings[position]);
                switch (position){
                    case 0:
                        iv.setImageResource(R.mipmap.home);
                        break;
                    case 1:
                        iv.setImageResource(R.mipmap.message);
                        break;
                    case 2:
                        iv.setImageResource(R.mipmap.find);
                        break;
                    case 3:
                        iv.setImageResource(R.mipmap.setting);
                        break;
                }
                return ll;
            }
        };
        mListView.setAdapter(mBaseAdapter);

    }

    @OnClick(R.id.head_imageview)
    public void onHeadClick(View v){
        startActivity(new Intent(context,SelfActivity.class));
        playOpenAnimation();
    }

    @OnItemClick(R.id.menu_listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        ((MainActivity)context).replaceFragment(position);
    }

    @Override
    protected int initLayoutRes() {
        return R.layout.fragment_left_menu;
    }

    public void initUi(String screen_name, String description,String avatar){
        mScreenName.setText(screen_name);
        mDescription.setText(description);
        ImageLoader.getInstance().loadImage(avatar, WelcomeActivity.mOptions,new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                mHead.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
}
