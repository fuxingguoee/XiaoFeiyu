package com.ydtong.autoupdate;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AuUpdateActivity extends Activity {

	private TextView au_update_now;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_au_update);
		
		au_update_now = (TextView) findViewById(R.id.au_update_now);
		
		au_update_now.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AuUpdateDialog.Builder builder = new AuUpdateDialog.Builder(AuUpdateActivity.this);  
		        builder.setMessage("1 这个就是自定义的提示框信息生活是的是的\n2 这个就是自定义的提示框\n3 这个就是自定义的提示框sdsdsdfsdf\n4 这个就是自定义的提示框sdsdsdfsdf"
		        		+ "\n5 这个就是自定义的提示框sdsdsdfsdf\n6 这个就是自定义的提示框sdsdsdfsdf\n7 这个就是自定义的提示框sdsdsdfsdf");  
		        builder.setTitle("自动更新");  
		        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
		            public void onClick(DialogInterface dialog, int which) {  
		                dialog.dismiss();  
		                //设置你的操作事项  
		            }  
		        });  
		  
		        builder.setNegativeButton("取消",  
		                new android.content.DialogInterface.OnClickListener() {  
		                    public void onClick(DialogInterface dialog, int which) {  
		                        dialog.dismiss();  
		                    }  
		                });  
		  
		        builder.create().show(); 
			}
		});
	}
}
