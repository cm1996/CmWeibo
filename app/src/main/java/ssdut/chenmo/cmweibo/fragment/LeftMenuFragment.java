package ssdut.chenmo.cmweibo.fragment;


import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.activity.BaseFragmentActivity;
import ssdut.chenmo.cmweibo.activity.MainActivity;
import ssdut.chenmo.cmweibo.activity.SelfActivity;
import ssdut.chenmo.cmweibo.activity.WelcomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftMenuFragment extends BaseFragment {


    @BindView(R.id.head_imageview)
    CircleImageView mHead;
    @BindView(R.id.screen_name)
    TextView mScreenName;
    @BindView(R.id.tv_description)
    TextView mDescription;
    @BindView(R.id.menu_listview)
    ListView mListView;

    String[] mStrings = {"首页","消息","发现","设置","待补充1","待补充2"};
    ArrayAdapter<String> mAdapter;

    @Override
    protected void initDatas() {
        //初始化适配器,为ListView装入数据
        mAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,mStrings);
        mListView.setAdapter(mAdapter);
    }

    @OnClick(R.id.head_imageview)
    public void onHeadClick(View v){
        startActivity(new Intent(context,SelfActivity.class));
        playOpenAnimation();
    }

    @OnItemClick(R.id.menu_listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        ((MainActivity)context).replaceFragment(position);
        showToast("可以想想怎么设计嘿 "+position);
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
