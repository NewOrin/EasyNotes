package com.neworin.easynotes.ui.fragment;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.FragmentDialogChooseBinding;
import com.neworin.easynotes.http.Response;
import com.neworin.easynotes.http.UserBizImpl;
import com.neworin.easynotes.model.User;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.GsonUtil;
import com.neworin.easynotes.utils.HttpUtil;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

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
    private String TAG = ChooseDialogFragment.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private IUserResultListener mIUserResultListener;

    public void setIUserResultListener(IUserResultListener IUserResultListener) {
        mIUserResultListener = IUserResultListener;
    }

    public interface IUserResultListener {
        void resultSuccess(User user);
    }

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

    /**
     * 验证邮箱格式
     *
     * @param email
     * @return
     */
    private boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 登录操作
     */
    private void doLogin() {
        UserBizImpl userBiz = new UserBizImpl();
        userBiz.login(getUser(), new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                closeProgressDialog();
                Response resp = response.body();
                if (resp.getMeta().isSuccess()) {
                    //登录成功
                    if (null != mIUserResultListener) {
                        User user = GsonUtil.getDateFormatGson().fromJson(resp.getData().toString(), User.class);
                        mIUserResultListener.resultSuccess(user);
                    }
                } else {
                    Snackbar.make(mBinding.getRoot(), resp.getMeta().getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Snackbar.make(mBinding.getRoot(), "登录失败" + t.getMessage(), Snackbar.LENGTH_SHORT).show();
                closeProgressDialog();
            }
        });
    }

    /**
     * 注册操作
     */
    private void doRegister() {
        UserBizImpl userBiz = new UserBizImpl();
        userBiz.register(getUser(), new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                closeProgressDialog();
                Response resp = response.body();
                if (resp.getMeta().isSuccess()) {
                    //注册成功
                    if (null != mIUserResultListener) {
                        mIUserResultListener.resultSuccess((User) resp.getData());
                    }
                } else {
                    Snackbar.make(mBinding.getRoot(), resp.getMeta().getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                closeProgressDialog();
                Snackbar.make(mBinding.getRoot(), "注册失败" + t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取EditText用户信息
     *
     * @return
     */
    private User getUser() {
        User user = new User();
        user.setEmail(getAccountEdit().getText().toString());
        user.setPassword(getPasswordEdit().getText().toString());
        return user;
    }

    @Override
    public void onClick(View v) {
        if (!HttpUtil.isNetworkConnected(getActivity())) {
            Snackbar.make(mBinding.getRoot(), R.string.personal_no_network, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (v == mBinding.chooseBtnAction) {
            if (!validateEmail(getAccountEdit().getText().toString())) {
                getAccountWrapper().setError(getString(R.string.email_validate_error));
            } else if (getPasswordEdit().getText().toString().equals("")) {
                getAccountWrapper().setErrorEnabled(false);
                getPasswordWrapper().setError(getString(R.string.password_empty_error));
            } else {
                getAccountWrapper().setErrorEnabled(false);
                getPasswordWrapper().setErrorEnabled(false);
                showProgressDialog();
                if (mIsLogin) {
                    doLogin();
                } else {
                    doRegister();
                }
            }
        }
    }

    /**
     * 显示加载对话框
     */
    protected void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();
    }

    /**
     * 关闭加载对话框
     */
    protected void closeProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }
}
