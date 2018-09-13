package com.il360.xiaofeiyu.captcha;

/**
 * Created by cdc4512 on 2018/2/5.
 */

public class PositionInfo {
    //缺块在整张图片的左上角x轴位置
    int left;
    //缺块在整张图片的左上角y轴位置
    int top;

    public PositionInfo(int left, int top) {
        this.left = left;
        this.top = top;
    }
}
