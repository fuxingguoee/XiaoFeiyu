package com.il360.xiaofeiyu.common;

import java.util.List;

import com.il360.xiaofeiyu.model.address.UserAddress;
import com.il360.xiaofeiyu.model.home.MySwitch;
import com.il360.xiaofeiyu.model.hua.CardConfig;
import com.il360.xiaofeiyu.model.hua.OutUserBank;
import com.il360.xiaofeiyu.model.user.OutUserRz;

public class GlobalPara {

	public static Boolean canResubmitPhone = false;
	public static Boolean canResubmitFund = false;

	public static Boolean getCanResubmitPhone() {
		return canResubmitPhone;
	}

	public static Boolean getCanResubmitFund() {
		return canResubmitFund;
	}

	/**
	 * 周周钱包
	 **/
	public static OutUserRz outUserRz = null;
	public static OutUserBank outUserBank = null;
	public static List<String> appNameList = null;
	public static List<CardConfig> cardConfigList = null;
	public static List<UserAddress> userAddressList = null;
	public static List<MySwitch> mySwitchList = null;
	public static Boolean canAutoVerified = false;
	
	public static int remainTimes = 0;
	public static int maxTimes = 0;
	
	public static double defaultRate = 0.0;
	
	public static String cosAppID = null;
	public static String cosName = null;
	public static String cosEndPoint = null;
	
	public static String telephone = null;


	public static String deposit = null;
	public static String valuationFee = null;
	public static String dayRent = null;
	public static String penaltyRate = null;

	public static String discoverHomeUrl = null;

	public static String getDiscoverHomeUrl(){
		return discoverHomeUrl;
	}
	
	
	public static String getDeposit() {
		return deposit;
	}
	public static String getValuationFee() {
		return valuationFee;
	}
	public static String getDayRent() {
		return dayRent;
	}
	public static String getPenaltyRate() {
		return penaltyRate;
	}
	public static String getCosAppID(){
		return cosAppID;
	}
	
	public static String getCosName(){
		return cosName;
	}
	
	public static String getCosEndPoint(){
		return cosEndPoint;
	}

	public static OutUserRz getOutUserRz() {
		return outUserRz;
	}

	public static OutUserBank getOutUserBank() {
		return outUserBank;
	}

	public static List<String> getAppNameList() {
		return appNameList;
	}
	public static List<CardConfig> getCardConfigList() {
		return cardConfigList;
	}
	
	public static List<UserAddress> getUserAddressList() {
		return userAddressList;
	}
	
	public static int getRemainTimes(){
		return remainTimes;
	}
	
	public static int getMaxTimes(){
		return maxTimes;
	}
	
	public static double getDefaultRate(){
		return defaultRate;
	}
	
	public static List<MySwitch> getMySwitchList() {
		return mySwitchList;
	}
	
	public static String getTelephone(){
		return telephone;
	}
	
	public static Boolean getCanAutoVerified() {
		if(GlobalPara.getMySwitchList() != null && GlobalPara.getMySwitchList().size() > 0){
			for (int i = 0; i < GlobalPara.getMySwitchList().size(); i++) {
				if(GlobalPara.getMySwitchList().get(i).getSwitchKey().equals("faceRecognition")
						&& GlobalPara.getMySwitchList().get(i).getSwitchValue().equals("ON")){
					return true;
				}
			}
		}
		return false;
	}

	public static void clean() {
		canResubmitPhone = false;
		canResubmitFund = false;
		outUserRz = null;
		appNameList = null;
		outUserBank = null;
		cardConfigList = null;
		userAddressList = null;
		mySwitchList = null;
		remainTimes = 0;
		maxTimes = 0;
	}
}
