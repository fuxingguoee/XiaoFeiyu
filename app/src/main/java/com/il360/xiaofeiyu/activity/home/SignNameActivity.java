package com.il360.xiaofeiyu.activity.home;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.util.BitmapUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class SignNameActivity extends Activity {

	ImageView iv_sign;
	TextView tv_sign;
	TextView tvAmount;
	
	Context context;
	LayoutParams p;

	static final int BACKGROUND_COLOR = Color.WHITE;

	static final int BRUSH_COLOR = Color.BLACK;

	PaintView mView;

	/** The index of the current color to use. */
	int mColorIndex;
	
	private Bundle paramBundle;
	private int count=0;
	
	private Toast toast;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		final FrameLayout f= ((FrameLayout) this.findViewById(R.id.tablet_view));

		int height = f.getMeasuredHeight();
		int width = f.getMeasuredWidth();

		Log.e("tagt", "" + height + "");
		Log.e("tagt", "" + width);

		p = (LayoutParams) f.getLayoutParams();
		p.height = height;// ((FrameLayout)this.findViewById(R.id.tablet_view)).getHeight();
		p.width = width;// ((FrameLayout)this.findViewById(R.id.tablet_view)).getWidth();
		mView = new PaintView(SignNameActivity.this);
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);
		frameLayout.addView(mView);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.act_sign_name);
//		paramBundle = this.getIntent().getExtras();
		paramBundle = new Bundle();
		Button btnClear = (Button) findViewById(R.id.tablet_clear);
		btnClear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mView.clear();
			}
		});

		Button btnOk = (Button) findViewById(R.id.tablet_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (count == 1) {//1代表空白
						showInfo("未签名或签名太简单，请重新签名");
					} else {
						if(count < 60){
							mView.clear();
							showInfo("未签名或签名太简单，请重新签名");
						} else if (count > 540) {
							mView.clear();
							showInfo("签名过于复杂，请重新签名");
						} else {
							// 这个就是签名，压缩保存，上传到服务器即可
							Bitmap bitmap = mView.getCachebBitmap();
							
//							ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
//							// 30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0
//							bitmap.compress(Bitmap.CompressFormat.JPEG, 20,byteArr);
							paramBundle.putString("signNamePic",BitmapUtil.getAbsolutePath(SignNameActivity.this, bitmap));
							recycle();
							Intent intent = new Intent();
							intent.putExtras(paramBundle);
							setResult(RESULT_CANCELED, intent);
							finish();
						}
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A7));
					LogUmeng.reportError(SignNameActivity.this, e);
				}
			}
		});

		Button btnCancel = (Button) findViewById(R.id.tablet_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				 finish();
			}
		});

	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(SignNameActivity.this, info,Toast.LENGTH_SHORT);
				} else {
					toast.setText(info);
				}
				toast.setDuration(Toast.LENGTH_LONG);//显示时间1S
				toast.show();
			}
		});
	}

	/**
	 * This view implements the drawing canvas.
	 * 
	 * It handles all of the input events and drawing functions.
	 */
	class PaintView extends View {
		private Paint paint;
		private Canvas cacheCanvas;
		private Bitmap cachebBitmap;
		private Path path;

		public Bitmap getCachebBitmap() {
			return cachebBitmap;
		}

		public PaintView(Context context) {
			super(context);
			init();
		}

		private void init() {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(3);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			path = new Path();
			cachebBitmap = Bitmap.createBitmap(p.width, (p.height),
					Config.RGB_565);
			cacheCanvas = new Canvas(cachebBitmap);
			cacheCanvas.drawColor(Color.WHITE);
		}

		public void clear() {
			if (cacheCanvas != null) {
				count=0;
				paint.setColor(BACKGROUND_COLOR);
				cacheCanvas.drawPaint(paint);
				paint.setColor(Color.BLACK);
				cacheCanvas.drawColor(Color.WHITE);
				invalidate();
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// canvas.drawColor(BRUSH_COLOR);
			try {
				count++;
				canvas.drawBitmap(cachebBitmap, 0, 0, null);
				canvas.drawPath(path, paint);
			} catch (Exception e) {
			}
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {

			int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
			int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
			if (curW >= w && curH >= h) {
				return;
			}

			if (curW < w)
				curW = w;
			if (curH < h)
				curH = h;

			Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
					Bitmap.Config.ARGB_8888);
			Canvas newCanvas = new Canvas();
			newCanvas.setBitmap(newBitmap);
			if (cachebBitmap != null) {
				newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
			}
			cachebBitmap = newBitmap;
			cacheCanvas = newCanvas;
		}

		private float cur_x, cur_y;

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				cur_x = x;
				cur_y = y;
				path.moveTo(cur_x, cur_y);
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				path.quadTo(cur_x, cur_y, x, y);
				cur_x = x;
				cur_y = y;
				break;
			}

			case MotionEvent.ACTION_UP: {
				cacheCanvas.drawPath(path, paint);
				path.reset();
				break;
			}
			}

			invalidate();

			return true;
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(RESULT_CANCELED);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		setResult(RESULT_CANCELED);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		recycle();
	}
	
	private void recycle() {
		try {
			if (mView != null && mView.getCachebBitmap() != null
					&& !mView.getCachebBitmap().isRecycled()) {
				boolean re = mView.getCachebBitmap().isRecycled();
				System.out.println(re);
				mView.getCachebBitmap().recycle();
				System.gc();  //提醒系统及时回收
			}
		} catch (Exception e) {
		}
	}
}
