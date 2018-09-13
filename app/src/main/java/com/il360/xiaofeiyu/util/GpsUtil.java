package com.il360.xiaofeiyu.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.il360.xiaofeiyu.common.MyApplication;

import android.content.Context;
import android.location.LocationManager;

public class GpsUtil {
	
	private static Map<String, Integer> cityMap = null;

	static {
		cityMap = new HashMap<String, Integer>();
		InputStream ins = null;
		try {
			ins = MyApplication.getContextObject().getAssets()
					.open("gps_city.txt");
			int size = ins.available();
			byte[] buffer= new byte[size];
			ins.read(buffer);
			String citys = new String(buffer);
			String[] array = citys.split("\n");
			for(String str : array) {
				String[] arr = str.split("=");
				cityMap.put(arr[1].trim(), Integer.valueOf(arr[0].trim()));
			}
		} catch (Exception e) {
		}

		if (ins != null) {
			try {
				ins.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static Integer getCityCode(String cityName) {
		return cityMap.get(cityName);
	}
	
	/** 
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的 
     * @param context 
     * @return true 表示开启 
     */  
    public static final boolean isOPen(final Context context) {  
        LocationManager locationManager   
                                 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）  
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  
        if (gps || network) {  
            return true;  
        }  
  
        return false;  
    }  

}
