package com.neworin.easynotes.ui.fragment;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.FragmentSettingBinding;
import com.neworin.easynotes.http.FeedBackBizImpl;
import com.neworin.easynotes.model.FeedBack;
import com.neworin.easynotes.ui.BaseFragment;
import com.neworin.easynotes.utils.DateUtil;
import com.neworin.easynotes.utils.DialogUtils;
import com.neworin.easynotes.utils.GenerateSequenceUtil;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NewOrin Zhang on 2017/3/22.
 * E-mail : NewOrinZhang@Gmail.com
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener {

    private FragmentSettingBinding mBinding;
    private java.lang.String TAG = SettingFragment.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.bind(getRootView());
        initEvent();
    }

    private void initEvent() {
        mBinding.settingFeedbackLayout.setOnClickListener(this);
        mBinding.settingOpenSourceLayout.setOnClickListener(this);
    }

    private void showFeedBackDialog() {
        final DialogUtils dialogUtils = new DialogUtils(getActivity());
        dialogUtils.showTextFieldDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProgressDialog();
                FeedBackBizImpl feedBackBiz = new FeedBackBizImpl();
                feedBackBiz.submitFeedBack(getFeedBack(dialogUtils), new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        closeProgressDialog();
                        showSnackBar(getRootView(), getString(R.string.setting_feedback_thank));
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        closeProgressDialog();
                        showSnackBar(getRootView(), getString(R.string.setting_feedback_submit_failed) + t.getMessage());
                    }
                });
            }
        });
    }

    /**
     * 获取反馈
     *
     * @param dialogUtils
     * @return
     */
    private FeedBack getFeedBack(DialogUtils dialogUtils) {
        FeedBack feedBack = new FeedBack();
        feedBack.setContent(dialogUtils.getEditText());
        feedBack.setCreateTime(DateUtil.getNowTime());
        feedBack.setId(GenerateSequenceUtil.generateSequenceNo());
        feedBack.setUserId(SharedPreferenceUtil.getUserId(getActivity()));
        feedBack.setPhoneInfo(Build.BRAND + " " + Build.MODEL);
        feedBack.setPhoneOs(String.valueOf(Build.VERSION.SDK_INT));
        return feedBack;
    }

    @Override
    public void onClick(View v) {
        if (v == mBinding.settingFeedbackLayout) {
            showFeedBackDialog();
        }
        if (v == mBinding.settingOpenSourceLayout) {

        }
    }
}
