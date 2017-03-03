package ssdut.chenmo.cmweibo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ssdut.chenmo.cmweibo.R;

/**
 * Created by chenmo on 2017/3/3.
 */

public class SendWeiboDialog extends Dialog {

    @BindView(R.id.edit_text_send_weibo)
    EditText mEditText;

    Context mContext;

    protected OnPoClickListener mOnPoClickListener;

    public SendWeiboDialog(Context context) {
        super(context, R.style.SendWeiboDialogStyle);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_send_weibo);

        ButterKnife.bind(this);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

    }

    /**
     *
     * @param v
     * 该事件通过两层接口传递给Activity处理
     */
    @OnClick(R.id.btn_send_weibo)
    public void onPoClick(View v) {
        if(!TextUtils.isEmpty(mEditText.getText().toString())) {
            if(mOnPoClickListener!=null) {
                mOnPoClickListener.onPoClick(v,mEditText.getText().toString());
            }
            SendWeiboDialog.this.cancel();
        } else {
            Toast.makeText(mContext, "内容不得为空", Toast.LENGTH_SHORT).show();
        }

    }

    public void setOnPoClickListener(OnPoClickListener onPoClickListener) {
        mOnPoClickListener = onPoClickListener;
    }

    public interface OnPoClickListener {
        public void onPoClick(View v, String s);
    }
}
