package ssdut.chenmo.cmweibo.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ssdut.chenmo.cmweibo.R;
import ssdut.chenmo.cmweibo.activity.BaseFragmentActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftMenuFragment extends BaseFragment {


    @BindView(R.id.head_imageview)
    CircleImageView mHead;
    @BindView(R.id.menu_listview)
    ListView mListView;
    String[] mStrings = {"消息","发现","设置","待补充1","待补充2"};
    ArrayAdapter<String> mAdapter;

    @Override
    protected void initDatas() {
        //初始化适配器,为ListView装入数据
        mAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,mStrings);
        mListView.setAdapter(mAdapter);
    }

    @OnItemClick(R.id.menu_listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        showToast("可以想想怎么设计嘿 "+position);
    }

    @Override
    protected int initLayoutRes() {
        return R.layout.fragment_left_menu;
    }
}
