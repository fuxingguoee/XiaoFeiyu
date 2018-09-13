package com.il360.xiaofeiyu.activity.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.WelcomeActivity;
import com.il360.xiaofeiyu.util.AESEncryptor;
import com.il360.xiaofeiyu.util.PicUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.GetObjectRequest;
import com.tencent.cos.task.listener.IDownloadTaskListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EActivity(R.layout.act_slider)
public class SliderActivity extends Activity {

//    Captcha captcha;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_slider);
//        captcha = (Captcha)findViewById(R.id.captCha);
//    }

    private Handler handler = null;

    private String picPath = "";

    protected ProgressDialog transDialog;

    @ViewById
    Button button;
    @ViewById
    TextView textView;
    @ViewById
    ImageView imageView;
    @ViewById
    ImageView imageView2;
//    @ViewById
//    Captcha captcha;

    @AfterViews
    void init(){

    }

    @Click
    void button(){
        textView.setText(AESEncryptor.decrypt(AESEncryptor.KEY, "7iMyrmcWnrfhc3lTULKYQg=="));
//        ImageFromTxyUtil.loadImage(SliderActivity.this,"http://twinseffect-1253261650.cossh.myqcloud.com/slider/a456e9d3bb7d41e08112eecf3a73bb3d_1.jpg",
//                imageView);
//
//        ImageFromTxyUtil.loadImage(SliderActivity.this,"http://twinseffect-1253261650.cossh.myqcloud.com/slider/a456e9d3bb7d41e08112eecf3a73bb3d_2.png",
//                imageView2);
//        loadPic("");
    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable  runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            Bitmap signBitmap = PicUtil.getSmallBitmap(picPath);
            imageView.setImageBitmap(signBitmap);
            if (transDialog != null && transDialog.isShowing()) {
                transDialog.dismiss();
            }
        }

    };

    private void loadPic(String sign) {

        final String headUrl = UserUtil.getUserInfo().getTxyUserPic();
        final String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "load";

        GetObjectRequest getObjectRequest = new GetObjectRequest(headUrl, savePath);
        getObjectRequest.setSign(sign);
        getObjectRequest.setListener(new IDownloadTaskListener() {
            @Override
            public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
            }

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {

                new Thread() {
                    public void run() {
                        String fileName = getNameFromUrl(headUrl);
                        picPath = savePath + File.separator + fileName;
                        handler.post(runnableUi);
                    }
                }.start();

                Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
            }

            @Override
            public void onFailed(COSRequest COSRequest, COSResult cosResult) {
                if (transDialog != null && transDialog.isShowing()) {
                    transDialog.dismiss();
                }
                Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
            }

            @Override
            public void onCancel(COSRequest arg0, COSResult arg1) {
                if (transDialog != null && transDialog.isShowing()) {
                    transDialog.dismiss();
                }
            }
        });

        WelcomeActivity.getCOSClient().getObject(getObjectRequest);
    }

    private String getNameFromUrl(String loadUrl) {
        String[] a = loadUrl.split("/");
        String s = a[a.length - 1];
        return s;
    }
















//    void init(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                getCaptcha();
//            }
//        }).start();
//    }
//
//    private  void getCaptcha() {
//        try {
//            String result = HttpUtils.get("http://192.168.0.85:8088/captcha-service/cs/captcha");
//            JSONObject json = new JSONObject(result);
//            int code = json.getInt("code");
//            if(code == 1) {
//                String bigImage = json.getString("bigImage");
//                String blockImage = json.getString("blockImage");
//                final Bitmap bitmap = decodeImage(bigImage);
//                final Bitmap blockBitmap = decodeImage(blockImage);
//                final  int top = json.getInt("top");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        captcha.setBitmap(bitmap, blockBitmap, top);
//                    }
//                });
//
//            }
//
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public Bitmap decodeImage(String bmMsg){
//        byte [] input = Base64.decode(bmMsg, Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(input, 0, input.length);
//        return  bitmap;
//    }

}
