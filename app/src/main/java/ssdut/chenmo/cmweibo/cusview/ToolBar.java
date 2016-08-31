package ssdut.chenmo.cmweibo.cusview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ssdut.chenmo.cmweibo.R;

/**
 * TODO: document your custom view class.
 */
public class ToolBar extends RelativeLayout {

    @BindView(R.id.back)
    ImageView backView;
    @BindView(R.id.more)
    ImageView moreView;
    @BindView(R.id.title_left)
    TextView titleLeftView;//这里的R.id.title_left和R.id.title_center分别指代"紧邻返回箭头右侧的文字"和"ActionBar中间的文字"
    @BindView(R.id.title_center)
    TextView titleCenterView;
    ToolBarListener listener;
    @BindView(R.id.title_right)
    TextView titleRightView;

    public ToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        int margin = ScreenUtil.dp2px(context, 10);
//        lp.setMargins(margin, margin, margin, margin);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toolbar, null);
        addView(view, lp);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.back)
    public void to_back() {
        if (listener != null)
            listener.onBackClicked();
    }

    @OnClick(R.id.more)
    public void to_more() {
        if (listener != null)
            listener.onMoreClicked();
    }

    @OnClick(R.id.title_left)
    public void to_left_title() {
        if (listener != null) {
            listener.onTitleLeftClicked();
        }
    }
    @OnClick(R.id.title_right)
    public void to_right_title(){
        if(listener!=null){
            listener.onTitleRightClicked();
        }
    }
    public interface ToolBarListener {

        void onBackClicked();

        void onMoreClicked();

        void onTitleLeftClicked();

        void onTitleRightClicked();

        void onInit(ImageView back, TextView titleLeft, TextView titleCenter,TextView titleRight, ImageView more);
    }

    public void setToolBarListener(ToolBarListener listener) {
        this.listener = listener;
        listener.onInit(backView, titleLeftView, titleCenterView,titleRightView, moreView);
    }

    /**
     * set the title of center textview
     */
    public void setTitle(String title){
        if(title==null)return;
        titleCenterView.setText(title);
    }
    public ImageView getMoreView(){
        return moreView;
    }
}
