package com.il360.xiaofeiyu.captcha;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.user.LoginActivity;


/**
 * Created by luozhanming on 2018/1/17.
 */

class PictureVertifyView extends AppCompatImageView {

    private static final int STATE_DOWN = 1;
    private static final int STATE_MOVE = 2;
    private static final int STATE_LOOSEN = 3;
    private static final int STATE_IDEL = 4;
    private static final int STATE_ACCESS = 5;
    private static final int STATE_UNACCESS = 6;


    private static final int TOLERANCE = 10;


    private int mState = STATE_IDEL;
    private PositionInfo shadowInfo;    //缺块阴影的位置
    private PositionInfo blockInfo;     //缺块图片的位置
    private Bitmap verfityBlock;        //缺块图片
    private Path blockShape;            //缺块形状
    private Paint bitmapPaint;         //绘制缺块图片的画笔
    private Paint shadowPaint;         //绘制缺块阴影的画笔
    private long startTouchTime;
    private long looseTime;
    private int blockSize = 100;

    private boolean mTouchEnable = true;   //是否可触动


    private Callback callback;

    private CaptchaStrategy mStrategy;

    private int mMode;

    private Bitmap bigImage;
    private Bitmap blockImage;
    private int topOri;
    private int currentBackgroundHeight;  // 加载后的背景大图片高度 即 自动放大后的
    private int currentBackgroundWidth;  // 加载后的背景大图片宽度 即 自动放大后的
    private int oriBackgroundHeight;    // 加载前的背景大图片高度
    private int oriBackgroundWidth;    // 加载前的背景大图片宽度



    interface Callback {
        void onSuccess(long time);

        void onFailed();
    }


    public PictureVertifyView(Context context) {
        this(context, null);
    }

    public PictureVertifyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public PictureVertifyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mStrategy = new DefaultCaptchaStrategy(context);
        shadowPaint = mStrategy.getBlockShadowPaint();
        bitmapPaint = mStrategy.getBlockBitmapPaint();
        setLayerType(View.LAYER_TYPE_SOFTWARE, bitmapPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制前，判断是否有缺块的位置信息，缺块形状和图片，没有的话要先获得它们的实例,其中mStrategy的作用，我们后面将会说到。
        if (shadowInfo == null) {
            shadowInfo = mStrategy.getBlockPostionInfo(getWidth(), getHeight(), blockSize);
            if (mMode == Captcha.MODE_BAR) {//滑动条模式
                blockInfo = new PositionInfo(0, shadowInfo.top);
            } else {
                blockInfo = mStrategy.getPositionInfoForSwipeBlock(getWidth(), getHeight(), blockSize);
            }
        }
        if (blockShape == null) {
            blockShape = mStrategy.getBlockShape(blockSize);
            blockShape.offset(shadowInfo.left, shadowInfo.top);
        }
        if (verfityBlock == null) {
            verfityBlock = createBlockBitmap();
        }
        if (mState != STATE_ACCESS) {
//            canvas.drawPath(blockShape, shadowPaint);
        }
        if (mState == STATE_MOVE || mState == STATE_IDEL || mState == STATE_DOWN || mState == STATE_UNACCESS) {
            if(verfityBlock != null) {
                canvas.drawBitmap(verfityBlock, blockInfo.left, blockInfo.top, bitmapPaint);
            }

        }

    }

    void down(int progress) {
        startTouchTime = System.currentTimeMillis();
        mState = STATE_DOWN;
        blockInfo.left = (int) (progress / 100f * (getWidth() - blockSize));
        invalidate();
    }

    void downByTouch(float x, float y) {
        mState = STATE_DOWN;
        blockInfo.left = (int) (x - blockSize / 2f);
        blockInfo.top = (int) (y - blockSize / 2f);
        startTouchTime = System.currentTimeMillis();
        invalidate();
    }

    void move(int progress) {
        mState = STATE_MOVE;
        blockInfo.left = (int) (progress / 100f * (getWidth() - blockSize));
        invalidate();
    }

    void moveByTouch(float offsetX, float offsetY) {
        mState = STATE_MOVE;
        blockInfo.left += offsetX;
        blockInfo.top += offsetY;
        invalidate();
    }

    void loose() {
        mState = STATE_LOOSEN;
        looseTime = System.currentTimeMillis();
        checkAccess();
//        invalidate();
    }


    void reset() {
        mState = STATE_IDEL;
        verfityBlock = null;
        shadowInfo = null;
        blockShape = null;
        invalidate();
    }

    void unAccess() {
        mState = STATE_UNACCESS;
        invalidate();
    }

    void access() {
        mState = STATE_ACCESS;
        invalidate();
    }

    void callback(Callback callback) {
        this.callback = callback;
    }


    void setCaptchaStrategy(CaptchaStrategy strategy) {
        this.mStrategy = strategy;
    }

    void setBlockSize(int size) {
        this.blockSize = size;
        this.blockShape = null;
        this.blockInfo = null;
        this.shadowInfo = null;
        this.verfityBlock = null;
        invalidate();
    }

    public void setBitmap(Bitmap bitmap) {
        this.blockShape = null;
        this.blockInfo = null;
        this.shadowInfo = null;
        this.verfityBlock.recycle();
        this.verfityBlock = null;
        setImageBitmap(bitmap);
    }

    void setMode(@Captcha.Mode int mode) {
        this.mMode = mode;
        this.blockShape = null;
        this.blockInfo = null;
        this.shadowInfo = null;
        this.verfityBlock = null;
        invalidate();
    }


    void setTouchEnable(boolean enable) {
        this.mTouchEnable = enable;
    }

    private Bitmap createBlockBitmap() {
        Bitmap tempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tempBitmap);
        //由于PictureVertifyView是继承ImageView，这里我们直接拿到ImageView的Drawable图片并限制其宽高以达
        getDrawable().setBounds(0, 0, getWidth(), getHeight());
//        canvas.clipPath(blockShape);
        getDrawable().draw(canvas);
        mStrategy.decoreateSwipeBlockBitmap(canvas, blockShape);

        BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
        bfoOptions.inScaled = true;
        tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dog_ag, bfoOptions);
        tempBitmap = bigImage;
        tempBitmap = resizeBitmap(tempBitmap, 278, 240);
        return tempBitmap;
    }

    public Bitmap resizeBitmap(Bitmap bitmap,int w,int h)
    {
        if(bitmap!=null)
        {
            /*BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
            bfoOptions.inScaled = true;
            Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dog_ori, bfoOptions);
            int bW = tempBitmap.getWidth();
            int bH = tempBitmap.getHeight();*/

            /*bfoOptions.inScaled = false;
            Bitmap dogAgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dog_ori, bfoOptions);
            int agH = dogAgBitmap.getHeight();
            int agW = dogAgBitmap.getWidth();
            oriBackgroundHeight = agH;
            oriBackgroundWidth = agW;*/

            int bW = bitmap.getWidth();
            int bH = bitmap.getHeight();

            oriBackgroundHeight = bH;
            oriBackgroundWidth = bW;

            int tW = getWidth();
            int tH = getHeight();

            float scaleWight2 = ((float)tW)/bW;
            float scaleHeight2 = ((float)tH)/bH;

            currentBackgroundHeight = tH;
            currentBackgroundWidth = tW;

            int topOri = this.topOri;
            int top = (tH * topOri) /bH + 1;
            blockInfo.top = top;

            int width = blockImage.getWidth();
            int height = blockImage.getHeight();
            /*int newWidth = w;
            int newHeight = h;
            float scaleWight = ((float)newWidth)/width;
            float scaleHeight = ((float)newHeight)/height;*/
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWight2, scaleHeight2);
            Bitmap res = Bitmap.createBitmap(blockImage, 0,0,width, height, matrix, true);
            return res;

        }
        else{
            return null;
        }
    }


    private Bitmap cropBitmap(Bitmap bmp) {
        Bitmap result = null;
        result = Bitmap.createBitmap(bmp, shadowInfo.left, shadowInfo.top, blockSize, blockSize);
        bmp.recycle();
        return result;
    }

    final Handler handler =  new Handler();
    /**
     * 检测是否通过
     */
    private void checkAccess() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int top = (oriBackgroundHeight*blockInfo.top) / currentBackgroundHeight;
                int left = (oriBackgroundWidth*blockInfo.left) / currentBackgroundWidth;
//              if (Math.abs(blockInfo.left - shadowInfo.left) < TOLERANCE && Math.abs(blockInfo.top - shadowInfo.top) < TOLERANCE) {
//                final boolean isMatch = doMatch(left, top);

                LoginActivity.setLeft(left + "");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        if(isMatch) {
                            access();
//                            if (callback != null) {
                                long deltaTime = looseTime - startTouchTime;
                                callback.onSuccess(deltaTime);
//                            }
//                        } else {
//                            unAccess();
//                            if (callback != null) {
//                                callback.onFailed();
//                            }
//                        }
                        invalidate();
                    }
                });

            }
        }).start();

    }

    private float tempX, tempY, downX, downY;


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && mMode == Captcha.MODE_NONBAR) {
            if (event.getX() < blockInfo.left || event.getX() > blockInfo.left + blockSize || event.getY() < blockInfo.top || event.getY() > blockInfo.top + blockSize) {
                return false;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMode == Captcha.MODE_NONBAR && verfityBlock != null && mTouchEnable) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = x;
                    downY = y;
                    downByTouch(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    loose();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float offsetX = x - tempX;
                    float offsetY = y - tempY;
                    moveByTouch(offsetX, offsetY);
                    break;
            }
            tempX = x;
            tempY = y;
        }
        return true;
    }

    public void setBitmap(Bitmap bigImage, Bitmap blockImage, int top) {
        this.bigImage = bigImage;
        this.blockImage = blockImage;
        this.topOri = top;
    }

}
