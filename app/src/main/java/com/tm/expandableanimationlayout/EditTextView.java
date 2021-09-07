package com.tm.expandableanimationlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;


public class EditTextView extends LinearLayout {

    private TextView mTvEditTitle;
    private EditText mEt;
    private TextView mTvContent;

    private boolean mEditEnabled = true;

    public EditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);
        View view = inflate(context, R.layout.view_edit_text, this);
        mTvEditTitle = (TextView) view.findViewById(R.id.tv_edit_title);
        mEt = (EditText) view.findViewById(R.id.et_edit_content);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditTextView);
        String title = ta.getNonResourceString(R.styleable.EditTextView_title_text);
        String editText = ta.getNonResourceString(R.styleable.EditTextView_edit_text);
        String editHint = ta.getNonResourceString(R.styleable.EditTextView_edit_hint);
        mEditEnabled = ta.getBoolean(R.styleable.EditTextView_edit_enabled, true);
        boolean isInputTypeNumber = ta.getBoolean(R.styleable.EditTextView_edit_input_type_number, false);
        ta.recycle();

        mTvEditTitle.setText(title);

        initEditEnabledUI();
        if (mEditEnabled) {
            mEt.setText(editText);
            if (!TextUtils.isEmpty(editHint)) {
                mEt.setHint(editHint);
            }
            if (isInputTypeNumber) {
                //输入类型限制为浮点数
                mEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
        } else {
            mTvContent.setText(editText);
        }

    }

    private void initEditEnabledUI() {
        if (!mEditEnabled) {
        }
        if (mEditEnabled) {
            mEt.setVisibility(View.VISIBLE);
            mTvContent.setVisibility(View.GONE);
        } else {
            mEt.setVisibility(View.GONE);
            mTvContent.setVisibility(View.VISIBLE);
        }
    }

    public String getEditString() {
        if (mEditEnabled && mEt != null) {
            return mEt.getText().toString();
        } else if (mTvContent != null) {
            return mTvContent.getText().toString();
        }
        return "";
    }

    public void setEditText(String str) {
        if (mEditEnabled && mEt != null) {
            mEt.setText(str);
        } else if (mTvContent != null) {
            mTvContent.setText(str);
        }
    }

    public void setTitleText(SpannableStringBuilder ssb) {
        if (mTvEditTitle != null) {
            mTvEditTitle.setText(ssb);
        }
    }

    public void setEditEnabled(boolean b) {
        if (mEditEnabled && mEt != null) {
            mEt.setEnabled(false);
        }
    }

    public void setEditClick(OnClickListener l) {
        mEditEnabled = false;
        initEditEnabledUI();
        EditTextView.this.setOnClickListener(l);
    }

    public String getTitleText() {
        if (mTvEditTitle != null) {
            return mTvEditTitle.getText().toString();
        }
        return "";
    }
}
