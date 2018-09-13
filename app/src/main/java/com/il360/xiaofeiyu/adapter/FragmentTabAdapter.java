package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener{
	/** 一个tab页面对应一个Fragment **/
    private List<Fragment> fragments;
    /** 用于切换tab **/
    private RadioGroup rgs;
    /** Fragment所属的Activity **/
    private FragmentActivity fragmentActivity;
    /** Activity中所要被替换的区域的id **/
    private int fragmentContentId;
    /** 当前Tab页面索引 **/
    private int currentTab;
    /** 用于让调用者在切换tab时候增加新的功能 **/
    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener;

    public FragmentTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, RadioGroup rgs, int showIndex) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(showIndex),String.valueOf(showIndex));
        ft.commit();
        RadioButton view = (RadioButton) rgs.getChildAt(showIndex);
        view.setChecked(true);
        rgs.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        int showi = 0;
        for(int i = 0; i < rgs.getChildCount(); i++){
            if(rgs.getChildAt(i).getId() == checkedId){
                Fragment fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag(String.valueOf(i));
                FragmentTransaction ft = obtainFragmentTransaction(i);
                getCurrentFragment().onPause(); // 暂停当前tab
//                getCurrentFragment().onStop(); // 暂停当前tab

                if(fragment!=null && fragment.isAdded()){
//                  fragment.onStart(); // 启动目标tab的onStart()
                  fragment.onResume(); // 启动目标tab的onResume()
                }else{
                    ft.add(fragmentContentId, fragments.get(i),String.valueOf(i));
                }
                ft.commit();
                showi = i;
                // 如果设置了切换tab额外功能功能接口
                if(null != onRgsExtraCheckedChangedListener){
                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId, i);
                }

            }
        }
        showTab(showi); // 显示目标tab

    }

    /**
     * 切换tab
     * @param idx
     */
    public void showTab(int idx){
        FragmentTransaction ft = obtainFragmentTransaction(idx);
        for(int i = 0; i < fragments.size(); i++){
            Fragment fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag(String.valueOf(i));
            if(fragment!=null){
                if(idx == i){
                    ft.show(fragment);
                }else{
                    ft.hide(fragment);
                }
            }
        }
        ft.commit();
        currentTab = idx; // 更新目标tab为当前tab
        RadioButton view = (RadioButton) rgs.getChildAt(currentTab);
        view.setChecked(true);
    }



    /**
     * 获取一个带动画的FragmentTransaction
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index){
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        // 设置切换动画
        if(index >= currentTab){
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        }else{
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment(){
        return fragments.get(currentTab);
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    /**
     *  切换tab额外功能功能接口
     */
    public static class OnRgsExtraCheckedChangedListener{
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index){

        }
    }

}
