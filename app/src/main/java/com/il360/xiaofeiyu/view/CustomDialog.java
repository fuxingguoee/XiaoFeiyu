package com.il360.xiaofeiyu.view;


import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.util.PicUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	@SuppressLint("WrongViewCast")
	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private Integer messageSize;
		private String etMessageHint1;
		private String etMessageHint;
		private String picCode;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		
		private EditText etMessage1;
		private Integer inputType1;
		
		private EditText etMessage;
		private Integer inputType;
		
		private ImageView ivMessage;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}
		
		public Builder setMessageSize(Integer messageSize) {
			this.messageSize = messageSize;
			return this;
		}
		
		public Builder setInputType1(Integer inputType1) {
			this.inputType1 = inputType1;
			return this;
		}
		
		public Builder setInputType(Integer inputType) {
			this.inputType = inputType;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setEtMessageHint1(String etMessageHint1) {
			this.etMessageHint1 = etMessageHint1;
			return this;
		}
		
		public Builder setEtMessageHint(String etMessageHint) {
			this.etMessageHint = etMessageHint;
			return this;
		}
		
		public Builder setPicCode(String picCode){
			this.picCode = picCode;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setEtMessageHint1(int etMessageHint1) {
			this.etMessageHint1 = (String) context.getText(etMessageHint1);
			return this;
		}
		
		public Builder setEtMessageHint(int etMessageHint) {
			this.etMessageHint = (String) context.getText(etMessageHint);
			return this;
		}
		
		public String getEtMessage1() {
			if (etMessage1.getText() != null) {
				return this.etMessage1.getText().toString();
			} else {
				return null;
			}
		}
		
		public String getEtMessage() {
			if (etMessage.getText() != null) {
				return this.etMessage.getText().toString();
			} else {
				return null;
			}
		}
		
		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("WrongViewCast")
		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,R.style.Dialog);
			View layout = inflater.inflate(R.layout.view_custom_dialogs, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			dialog.setCancelable(false);// 设置点击屏幕Dialog不消失
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(title);
			// set the dialog EditText
			if(etMessageHint1 != null){
				((EditText) layout.findViewById(R.id.etMessage1)).setHint(etMessageHint1);
				etMessage1 = ((EditText) layout.findViewById(R.id.etMessage1));
				if(inputType1 != null){
					etMessage1.setInputType(inputType1);
				}
			}else{
				layout.findViewById(R.id.etMessage1).setVisibility(View.GONE);
			}
			
			if(etMessageHint != null){
				((EditText) layout.findViewById(R.id.etMessage)).setHint(etMessageHint);
				etMessage = ((EditText) layout.findViewById(R.id.etMessage));
				if(inputType != null){
					etMessage.setInputType(inputType);
				}
			}else{
				layout.findViewById(R.id.etMessage).setVisibility(View.GONE);
			}
			// set the dialog ImageView
			ivMessage = ((ImageView) layout.findViewById(R.id.ivMessage));
			if(picCode != null){
				ivMessage.setImageBitmap(PicUtil.convertStringToIcon(picCode));
			} else {
				layout.findViewById(R.id.ivMessage).setVisibility(View.GONE);
			}
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
				layout.findViewById(R.id.tvline).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
				layout.findViewById(R.id.tvline).setVisibility(
						View.GONE);
			}
			
			if(negativeButtonText == null && positiveButtonText==null && title==null){
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
				layout.findViewById(R.id.title).setVisibility(
						View.GONE);
				layout.findViewById(R.id.tvline).setVisibility(
						View.GONE);
				layout.findViewById(R.id.tvline2).setVisibility(
						View.GONE);
				layout.findViewById(R.id.tvline3).setVisibility(
						View.GONE);
			}
			
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
				if(messageSize != null){
					((TextView) layout.findViewById(R.id.message)).setTextSize(messageSize);
				}
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.message))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.message)).addView(
						contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}
}