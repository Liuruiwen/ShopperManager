package com.rw.basemvp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.rw.basemvp.R;

public class TitleView  extends FrameLayout {
    public TitleView(Context context) {
        super(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.base_view_title, this);
    }

    public <T extends View> T getView(int viewId) {
        return (T) findViewById(viewId);
    }


    public TitleView setText(int viewId, String text) {
        View view = getView(viewId);
        if (view instanceof AppCompatTextView) {
            setText((AppCompatTextView) view, text);
        }
        return this;
    }

    public TitleView setText(AppCompatTextView textView, String text) {
        textView.setVisibility(VISIBLE);
        textView.setText(text);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        setText(R.id.tv_title_center, title);
    }

    public TitleView setImageResource(int viewId, int resource) {
        View view = getView(viewId);
        if (view instanceof AppCompatImageView) {
            setImageResource((AppCompatImageView) view,resource);
        }
        return this;
    }

    public TitleView setImageResource(AppCompatImageView imageView, int resource) {
        imageView.setVisibility(VISIBLE);
        imageView.setImageResource(resource);
        return this;
    }

    public TitleView setTextColor(int viewId, int color) {
        View view = getView(viewId);
        if (view instanceof AppCompatTextView) {
            ((AppCompatTextView) view).setTextColor(color);
        }
        return this;
    }

    public TitleView setBackGroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public TitleView setBackgroundResource(int viewId, int drawableRes) {
        View view = getView(viewId);
        view.setBackgroundResource(drawableRes);
        return this;
    }

    public TitleView setVisible(int viewId, boolean visible) {
        return setVisible(findViewById(viewId), visible);
    }

    public TitleView setVisible(View view, boolean visible) {
        view.setVisibility(visible ? VISIBLE : GONE);
        return this;
    }


    public TitleView setChildClickListener(View view, OnClickListener listener) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(listener);
        }
        return this;
    }

    public TitleView setChildClickListener(int viewId, OnClickListener listener) {
        return setChildClickListener(findViewById(viewId), listener);
    }


}
