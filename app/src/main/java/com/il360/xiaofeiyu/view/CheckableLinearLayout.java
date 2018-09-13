package com.il360.xiaofeiyu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
 
    // checked状态
    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
 
    // 是否选中
    private boolean mChecked = false;
 
    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    /**
     * 判断是否选中
     */
    public boolean isChecked() {
        return mChecked;
    }
 
    /**
     * 设置选中状态
     */
    public void setChecked(boolean b) {
        if (b != mChecked) {
            mChecked = b;
            refreshDrawableState();
        }
    }
 
    /**
     * 切换当前的选中状态
     */
    public void toggle() {
        setChecked(!mChecked);
    }
 
    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        // 在原有状态中添加一个空间space用于保存checked状态
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            // 将checked状态合并到原有的状态数组中
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
}