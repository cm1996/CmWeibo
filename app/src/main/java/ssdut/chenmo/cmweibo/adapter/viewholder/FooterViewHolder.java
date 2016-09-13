package ssdut.chenmo.cmweibo.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ssdut.chenmo.cmweibo.R;

/**
 * Created by chenmo on 2016/9/7.
 */
public class FooterViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.rcv_load_more)
    public ProgressBar rcvLoadMore;

    public FooterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
