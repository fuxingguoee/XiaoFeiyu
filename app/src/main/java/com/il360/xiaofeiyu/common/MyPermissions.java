package com.il360.xiaofeiyu.common;

import java.util.ArrayList;

import android.Manifest;

public class MyPermissions {
	
    /**
     * 必要全选,如果这几个权限没通过的话,就无法使用APP
     */
	public static final ArrayList<String> FORCE_REQUIRE_PERMISSIONS = new ArrayList<String>() {
		{
			add(Manifest.permission.CAMERA);
			add(Manifest.permission.READ_EXTERNAL_STORAGE);
			add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			add(Manifest.permission.CALL_PHONE);
			add(Manifest.permission.READ_CONTACTS);
			add(Manifest.permission.RECEIVE_SMS);
			add(Manifest.permission.READ_SMS);
			add(Manifest.permission.READ_PHONE_STATE);
			add(Manifest.permission.ACCESS_COARSE_LOCATION);
			add(Manifest.permission.ACCESS_FINE_LOCATION);
		}
	};
}
