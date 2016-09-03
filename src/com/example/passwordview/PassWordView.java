package com.example.passwordview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

public class PassWordView extends EditText implements
		OnFocusChangeListener, TextWatcher {
	
	private boolean rmt_password_eyes_gone = false;
	private final int RMT_PASSWORD_EDITTEXT = 1;
	private final int RMT_ACCOUNT_EDITTEXT = 0;
	private int RMT_DEFAULT_EDITTEXT_TYPE = RMT_ACCOUNT_EDITTEXT;

	private Drawable mDrawable = null;
	private AttributeSet mAttrs = null;
	private Context mContext = null;

	public PassWordView(Context context) {
		this(context, null);
		mContext = context;
	}

	@SuppressLint("Recycle")
	public PassWordView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
		mContext = context;
		mAttrs = attrs;
		TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs,
				R.styleable.PassWordView);
		RMT_DEFAULT_EDITTEXT_TYPE = localTypedArray.getInteger(
				R.styleable.PassWordView_rmt_custom_edittext_type, 0);
	}

	@SuppressLint("Recycle")
	public PassWordView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mAttrs = attrs;
		TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs,
				R.styleable.PassWordView);
		RMT_DEFAULT_EDITTEXT_TYPE = localTypedArray.getInteger(
				R.styleable.PassWordView_rmt_custom_edittext_type,
				RMT_ACCOUNT_EDITTEXT);
		init();
	}

	private void init() {
		if (mDrawable == null) {
			if (RMT_DEFAULT_EDITTEXT_TYPE == RMT_ACCOUNT_EDITTEXT) {
				mDrawable = getResources().getDrawable(R.drawable.rmt_delete);
			} else {
				mDrawable = getResources().getDrawable(
						R.drawable.rmt_eyes_normal);
				rmt_password_eyes_gone = false;
			}
		}
		mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(),
				mDrawable.getIntrinsicHeight());
		if (RMT_DEFAULT_EDITTEXT_TYPE == 0) {
			setClearIconVisible(false);
		} else {
			setClearIconVisible(true);
		}
		setOnFocusChangeListener(this);
		addTextChangedListener(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (getWidth()
						- getPaddingRight() - mDrawable.getIntrinsicWidth())
						&& (event.getX() < ((getWidth() - getPaddingRight())));
				if (touchable) {
					if (RMT_DEFAULT_EDITTEXT_TYPE == RMT_ACCOUNT_EDITTEXT) {
						this.setText("");
					}
					updateDrawable();
				}
			}
		}

		return super.onTouchEvent(event);
	}

	private void updateDrawable() {
		if (RMT_DEFAULT_EDITTEXT_TYPE == RMT_PASSWORD_EDITTEXT) {
			if (!rmt_password_eyes_gone) {
				mDrawable = getResources().getDrawable(
						R.drawable.rmt_eyes_pressed);
				rmt_password_eyes_gone = true;
				setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
			} else {
				mDrawable = getResources().getDrawable(
						R.drawable.rmt_eyes_normal);
				rmt_password_eyes_gone = false;
				setTransformationMethod(PasswordTransformationMethod
						.getInstance());
			}
		}
		mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(),
				mDrawable.getIntrinsicHeight());
		Drawable right = mDrawable != null ? mDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			if (RMT_DEFAULT_EDITTEXT_TYPE == RMT_ACCOUNT_EDITTEXT) {
				setClearIconVisible(getText().length() > 0);
			}
		} else {
			if (RMT_DEFAULT_EDITTEXT_TYPE == RMT_ACCOUNT_EDITTEXT) {
				setClearIconVisible(false);
			}
		}
	}

	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (RMT_DEFAULT_EDITTEXT_TYPE == RMT_ACCOUNT_EDITTEXT) {
			setClearIconVisible(s.length() > 0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}
}
