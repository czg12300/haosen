package com.heneng.heater.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heneng.heater.R;
import com.heneng.heater.utils.LogUtils;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/9/18.
 */
public class HeaderView extends RelativeLayout implements View.OnClickListener {

    @InjectView(R.id.header_tv_left)
    TextView tv_left;
    @InjectView(R.id.header_tv_title)
    TextView tv_title;
    @InjectView(R.id.header_tv_right)
    TextView tv_right;

    private String title;
    private String leftText;
    private String rightText;
    private float titleTextSize;


    //监听点击代理
    private onClickLeftListener leftListener;

    public onClickRightListener getRightListener() {
        return rightListener;
    }

    public void setRightListener(onClickRightListener rightListener) {
        this.rightListener = rightListener;
    }

    public onClickLeftListener getLeftListener() {
        return leftListener;
    }

    public void setLeftListener(onClickLeftListener leftListener) {
        this.leftListener = leftListener;
    }

    private onClickRightListener rightListener;


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.header_tv_left:
                if (leftListener != null) {
                    leftListener.onLeftClick(view);
                }
                break;
            case R.id.header_tv_right:
                if (rightListener != null) {
                    rightListener.onRightClick(view);
                }
                break;
        }
    }

    public void setTitle(String title) {
        this.title = title;
        tv_title.setText(title);
    }

    public interface onClickLeftListener {
        void onLeftClick(View view);
    }

    public interface onClickRightListener {
        void onRightClick(View view);
    }

    public HeaderView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View _view = View.inflate(context, R.layout.a1_view_header, this);
        ButterKnife.inject(_view);

        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        if (leftText == null || "".equals(leftText)) {
            tv_left.setVisibility(View.GONE);
        } else {
            tv_left.setText(leftText);
        }

        if (rightText == null || "".equals(rightText)) {
            tv_right.setText("");
        } else {
            tv_right.setText(rightText);
        }
        tv_title.setText(title);

//        LogUtils.e("titleTextSize:"+titleTextSize);

      //  tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize);
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);


    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);


        leftText = a.getString(R.styleable.HeaderView_headerLeftText);
        rightText = a.getString(R.styleable.HeaderView_headerRightText);
        title = a.getString(R.styleable.HeaderView_headerTitle);

        titleTextSize = a.getDimension(R.styleable.HeaderView_headerTitleTextSize, context.getResources().getDimension(R.dimen.view_header_titlesize));

        a.recycle();
        init(context);
    }
}
