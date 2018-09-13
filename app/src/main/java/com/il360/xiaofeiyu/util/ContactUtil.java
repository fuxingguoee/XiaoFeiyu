package com.il360.xiaofeiyu.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.il360.xiaofeiyu.model.home.Contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

public class ContactUtil {
	private static List<Contact> contactList = new ArrayList<Contact>();
	/** 获取库Phon表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER };
	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	public static List<Contact> getContactList(Context mContext) {
		if(contactList != null && contactList.size() > 0){
			contactList.clear();
		}
		getPhoneContacts(mContext);
		getSIMContacts(mContext);
		return contactList;
	}

	/** 得到手机通讯录联系人信息 **/
	private static void getPhoneContacts(Context mContext) {
		ContentResolver resolver = mContext.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumberStr = phoneCursor.getString(PHONES_NUMBER_INDEX);
				String phoneNumber = handleNum(phoneNumberStr);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				if (TextUtils.isEmpty(contactName) || contactName.length() > 50) {
					continue;
				}
				Contact contact = new Contact();
				contact.setName(contactName);
				contact.setPhone(phoneNumber);
				contactList.add(contact);
			}

			phoneCursor.close();
		}
	}

	/** 得到手机SIM卡联系人人信息 **/
	private static void getSIMContacts(Context mContext) {
		ContentResolver resolver = mContext.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		try {
			Cursor phoneCursor = resolver.query(uri, null, null, null, null);
			if (phoneCursor != null) {
				while (phoneCursor.moveToNext()) {
	
					// 得到手机号码
					String phoneNumberStr = phoneCursor.getString(phoneCursor.getColumnIndex(People.NUMBER));
					String phoneNumber = handleNum(phoneNumberStr);
					// 当手机号码为空的或者为空字段 跳过当前循环
					if (TextUtils.isEmpty(phoneNumber))
						continue;
					// 得到联系人名称
					String contactName = phoneCursor.getString(phoneCursor.getColumnIndex(People.NAME));
					if (TextUtils.isEmpty(contactName) || contactName.length() > 50) {
						continue;
					}
					Contact contact = new Contact();
					contact.setName(contactName);
					contact.setPhone(phoneNumber);
					contactList.add(contact);
				}
	
				phoneCursor.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private static String handleNum(String num) {
		if (TextUtils.isEmpty(num)) {
			return "";
		} else {
			String newNum2 = "";
			String newNum = num.replaceAll("-", "").replaceAll("[\\+]", "").replaceAll("[\\*]", "")
					.replaceAll("[\\#]", "").replaceAll(" ", "").trim();

			if (newNum.contains(";")) {
				String[] array = newNum.split(";");
				if (isNumeric(array[0])) {
					newNum = array[0];
				} else {
					if (isNumeric(array[1])) {
						newNum = array[1];
					} else {
						newNum = "";
					}
				}
			}
			
			if (isNumeric(newNum)) {
				if (newNum.startsWith("86")) {
					newNum2 = newNum.substring(2);
				} else if(newNum.startsWith("600")){
					newNum2 = newNum.substring(3);
				} else {
					newNum2 = newNum;
				}
			} 

			return newNum2;
		}
	}
	
	
	private static boolean isNumeric(String str) {//判断是否纯数字
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
