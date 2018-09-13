package com.il360.xiaofeiyu.util;

import com.il360.xiaofeiyu.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class Dialog {
	
	/**
	 * 显示对话框（标题固定、内容、确定按钮无事件）
	 * @param Context context 上下文
	 * @param int message 对应的资源ID
	 */
	public static void showDialog(Context context,  final String message) {
		showDialog(context, context.getString(R.string.dialog_title), message);
	}
	
	/**
	 * 显示对话框（标题固定、内容、确定按钮无事件）
	 * @param Context context 上下文
	 * @param int message 对应的资源ID
	 */
	public static void showDialog(Context context,  final int message) {
		showDialog(context, R.string.dialog_title, message);
	}

	/**
	 * 显示对话框（标题、内容、确定按钮无事件）
	 * @param Context context 上下文
	 * @param int title 对应的资源ID
	 * @param int message 对应的资源ID
	 */
	public static void showDialog(Context context, int title, final int message) {
		showDialog(context, context.getString(title), context.getString(message));
	}

	/**
	 * 显示对话框（标题、内容、确定按钮无事件）
	 * @param Context context 上下文
	 * @param String title 标题
	 * @param String message 消息内容
	 */
	public static void showDialog(Context context, String title,
			final String message) {
		AlertDialog.Builder builder = createDialog(context, title, message);
		builder.setPositiveButton(
				title, null);
		builder.create().show();
	}

	
	/**
	 * 显示对话框（标题，内容，按钮名称，按钮事件）
	 * @param Context context 上下文
	 * @param int titleId 标题的资源ID
	 * @param int messageId 消息内容的资源ID
	 * @param int okTextId 按钮名称的资源ID
	 * @param OnClickListener oklistener 单击事件
	 */
	public static void alert(Context context, int title, int message,
			int okText, OnClickListener oklistener) {
		alert(context, context.getString(title), context.getString(message),
				context.getString(okText), oklistener);
	}

	/**
	 * 显示对话框（标题，内容，按钮名称，按钮事件）
	 * @param Context context 上下文
	 * @param CharSequence titleId 标题
	 * @param CharSequence messageId 消息内容
	 * @param CharSequence okTextId 按钮名称的资源ID
	 * @param OnClickListener oklistener 单击事件
	 */
	public static void alert(Context context, String title,
			String message, String okText,
			OnClickListener oklistener) {
		AlertDialog.Builder builder = createDialog(context, title, message);
		builder.setPositiveButton(okText, oklistener);
		builder.create().show();
	}

	/**
	 * 显示对话框（标题，内容，确定按钮名称，确定按钮事件,取消按钮名称，取消按钮事件）
	 * @param Context context 上下文
	 * @param int titleId 标题的资源ID
	 * @param int messageId 消息内容的资源ID
	 * @param int okTextId 确定按钮名称的资源ID
	 * @param OnClickListener oklistener 确定按钮单击事件
	 * @param int cancelTextId 取消按钮名称的资源ID
	 * @param OnClickListener cancellistener 取消按钮单击事件
	 */
	public static void confirm(Context context, int title, int message,
			int okText, OnClickListener oklistener, int cancelText,
			OnClickListener cancellistener) {
		confirm(context, context.getString(title), context.getString(message),
				context.getString(okText), oklistener,
				context.getString(cancelText), cancellistener);
	}

	public static void confirm(Context context, CharSequence title,
			CharSequence message, CharSequence okText,
			OnClickListener oklistener, CharSequence cancelText,
			OnClickListener cancellistener) {
		AlertDialog.Builder builder = createDialog(context, title, message);
		builder.setCancelable(false);// 设置点击屏幕Dialog不消失
		builder.setPositiveButton(okText, oklistener);
		builder.setNegativeButton(cancelText, cancellistener);
		builder.create().show();
	}

	
	private static AlertDialog.Builder createDialog(Context context,
			CharSequence title, CharSequence message) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);
		if (title != null) {
			builder.setTitle(title);
		}
		return builder;
	}

}
