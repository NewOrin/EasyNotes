package com.neworin.easynotes.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.FragmentDialogChooseBinding;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NewOrin Zhang on 2017/4/21.
 * Project : com.neworin.easynotes.ui.fragment
 * Description:
 */

public class ChooseDialogFragment extends DialogFragment implements View.OnClickListener {

    private View mView;
    private FragmentDialogChooseBinding mBinding;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    private Boolean mIsLogin;//默认是登录

    public static ChooseDialogFragment newInstance(Bundle bundle) {
        ChooseDialogFragment fragment = new ChooseDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_dialog_choose, container, false);
        mBinding = DataBindingUtil.bind(mView);
        mIsLogin = getArguments().getBoolean(Constant.ARG0, true);
        initView();
        initEvent();
        return mView;
    }

    private void initView() {
        getAccountWrapper().setHint(getString(R.string.email_hint));
        getPasswordWrapper().setHint(getString(R.string.password_hint));
        if (mIsLogin) {
            mBinding.chooseDialogTitle.setText(R.string.login);
            mBinding.chooseBtnAction.setText(R.string.login);
        } else {
            mBinding.chooseDialogTitle.setText(R.string.register);
            mBinding.chooseBtnAction.setText(R.string.register);
        }
    }

    private void initEvent() {
        mBinding.chooseBtnAction.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDialogSize();
    }

    private void setDialogSize() {
        int width = Integer.parseInt(SharedPreferenceUtil.getString(getActivity(), Constant.SCREEN_WIDTH));
        int height = Integer.parseInt(SharedPreferenceUtil.getString(getActivity(), Constant.SCREEN_HEIGHT));
        getDialog().getWindow().setLayout((int) (width * 0.9), (int) (height * 0.55));
    }

    private TextInputLayout getAccountWrapper() {
        return mBinding.chooseAccountWrapper;
    }

    private TextInputLayout getPasswordWrapper() {
        return mBinding.choosePasswordWrapper;
    }

    private EditText getAccountEdit() {
        return mBinding.chooseAccountEdit;
    }

    private EditText getPasswordEdit() {
        return mBinding.choosePasswordEdit;
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onClick(View v) {
        if (v == mBinding.chooseBtnAction) {
            if (!validateEmail(getAccountEdit().getText().toString())) {
                getAccountWrapper().setError(getString(R.string.email_validate_error));
            } else if (getPasswordEdit().getText().toString().equals("")) {
                getAccountWrapper().setErrorEnabled(false);
                getPasswordWrapper().setError(getString(R.string.password_empty_error));
            } else {
                getAccountWrapper().setErrorEnabled(false);
                getPasswordWrapper().setErrorEnabled(false);
            }
        }
    }
}
