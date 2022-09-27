package com.wht.addekh.Notification;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.wht.addekh.R;

public class CustomAlertDialog extends Dialog implements View.OnClickListener {
    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;
    private int mAlertType;
    private FrameLayout mErrorFrame, frame_background;
    private FrameLayout mWarningFrame;
    private AppCompatButton mConfirmButton, mCancelButton;
    private OnCMEDialogClickListner mCancelClickListener;
    private OnCMEDialogClickListner mConfirmClickListener;
    private String mTitleText;
    private TextView mTitleTextView, mContentTextView;
    private String mContentText;
    private String mCancelText;
    private String mConfirmText;
    private ImageView mCustomImage;
    private Drawable mCustomImgDrawable;
    private int mResID = 0;
    //private BlurView blurView;
    private Context context;
    private ViewGroup dialog;


    public CustomAlertDialog(Context context) {
        this(context, NORMAL_TYPE);
    }

    public CustomAlertDialog(Context context, int alertType) {
        super(context, R.style.alert_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mAlertType = alertType;
        this.context = context;
        getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custome_notification_dialog);

        //getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        //View mDialogView = getWindow().getDecorView().findViewById( android.R.id.content );

        //View decorView = getWindow().getDecorView();
        //Activity's root View. Can also be root View of your layout (preferably)

        //blurView = (BlurView) findViewById(R.id.blurView);
        dialog = (ViewGroup) findViewById(R.id.dialog);
        if (mAlertType != PROGRESS_TYPE)
            mTitleTextView = (TextView) findViewById(R.id.title_text);
        else
            mTitleTextView = (TextView) findViewById(R.id.dialog_title_text);

        mContentTextView = (TextView) findViewById(R.id.content_text);
        mWarningFrame = (FrameLayout) findViewById(R.id.warning_frame);
        mErrorFrame = (FrameLayout) findViewById(R.id.error_frame);
        frame_background = (FrameLayout) findViewById(R.id.frame_background);
        mCustomImage = (ImageView) findViewById(R.id.custom_image);
        mConfirmButton = (AppCompatButton) findViewById(R.id.confirm_button);
        mCancelButton = (AppCompatButton) findViewById(R.id.cancel_button);
        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        setThemeColor(mResID);
        setTitleText(mTitleText);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        changeAlertType(mAlertType, true);
        setThemeColor(mResID);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        radius(20);
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        if (!fromCreate) {
            // restore all of views state before switching alert type
            restore();
        }
        switch (mAlertType) {
            case ERROR_TYPE:
                mErrorFrame.setVisibility(View.VISIBLE);
                break;
            case SUCCESS_TYPE:
                setCustomImage(getContext().getResources().getDrawable(R.drawable.completed));
                //warning_frame.setVisibility(View.VISIBLE);
                // initial rotate layout of success mask
                // mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                //  mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                break;
            case WARNING_TYPE:
                // mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                mWarningFrame.setVisibility(View.VISIBLE);
                break;
            case CUSTOM_IMAGE_TYPE:
                setCustomImage(mCustomImgDrawable);
                break;
            case PROGRESS_TYPE:
                findViewById(R.id.dialog).setVisibility(View.GONE);
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void restore() {
        mCustomImage.setVisibility(View.GONE);
        mErrorFrame.setVisibility(View.GONE);
        mWarningFrame.setVisibility(View.GONE);
        mConfirmButton.setVisibility(View.VISIBLE);
        //mConfirmButton.setBackgroundResource(R.drawable.dialog_button_background);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(CustomAlertDialog.this);
            } else {
                // Log.d("mytag", "cancel btn else");
                CustomAlertDialog.super.cancel();
                //dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(CustomAlertDialog.this);
            } else {
                //Log.d("mytag", "confirm btn else");
                CustomAlertDialog.super.dismiss();
                // dismissWithAnimation();
            }
        }


    }

    public CustomAlertDialog setCancelClickListener(OnCMEDialogClickListner listener) {
        mCancelClickListener = listener;
        return this;
    }

    public CustomAlertDialog setConfirmClickListener(OnCMEDialogClickListner listener) {
        mConfirmClickListener = listener;
        return this;
    }

    public CustomAlertDialog setCustomImage(Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    public CustomAlertDialog setCustomImage(int resourceId) {
        return setCustomImage(getContext().getResources().getDrawable(resourceId));
    }

    public CustomAlertDialog setThemeColor(int resourceId) {
        mResID = resourceId;
        //frame_background.setBackgroundColor( getContext().getResources().getColor( resourceId ) );
        if (mResID != 0 && frame_background != null) {
            frame_background.setBackgroundColor(getContext().getResources().getColor(mResID));
            GradientDrawable bgShape = (GradientDrawable) mConfirmButton.getBackground();
            bgShape.setColor(getContext().getResources().getColor(mResID));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mConfirmButton.setBackground(bgShape);
            }
        }
        return this;
    }

    public String getTitleText() {
        return mTitleText;
    }

    public CustomAlertDialog setTitleText(String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }

    public String getContentText() {
        return mContentText;
    }

    public CustomAlertDialog setContentText(String text) {
        mContentText = text;

        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            mContentTextView.setText(mContentText);
        }

        return this;
    }

    public CustomAlertDialog radius(int radius) {
        /*View decorView = getWindow().getDecorView();

        final ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        final Drawable windowBackground = decorView.getBackground();
        blurView.setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(new RenderScriptBlur(getContext()))
                .blurRadius(radius);*/
        return this;
    }

    public CustomAlertDialog setCanclable(boolean canclable) {
        setCancelable(canclable);
        setCanceledOnTouchOutside(true);
        return this;
    }

    public CustomAlertDialog showContentText(boolean isShow) {
        boolean mShowContent = isShow;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public String getCancelText() {
        return mCancelText;
    }

    public CustomAlertDialog setCancelText(String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public String getConfirmText() {
        return mConfirmText;
    }

    public CustomAlertDialog setConfirmText(String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);

        }
        return this;
    }

    public CustomAlertDialog showCancelButton(boolean isShow) {
        boolean mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public interface OnCMEDialogClickListner {
        void onClick(CustomAlertDialog customDialog);
    }

}
