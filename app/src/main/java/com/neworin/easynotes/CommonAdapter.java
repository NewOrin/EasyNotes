package com.neworin.easynotes;

import java.util.List;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommonAdapter<T> extends BaseAdapter {

    public Context mContext;
    public List<T> mDatas;
    public LayoutInflater mInflater;
    private int mLayoutId;
    private int mVariableId;

    public CommonAdapter(Context context, List<T> datas, int layoutId, int variableId) {
        this.mContext = context;
        this.mDatas = datas;
        this.mInflater = LayoutInflater.from(context);
        this.mLayoutId = layoutId;
        this.mVariableId = variableId;
    }

    public int getCount() {
        return mDatas.size();
    }

    public T getItem(int arg0) {
        return mDatas.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding mViewDataBinding;
        if (convertView == null) {
            mViewDataBinding = DataBindingUtil.inflate(mInflater, mLayoutId, parent, false);
        } else {
            mViewDataBinding = DataBindingUtil.getBinding(convertView);
        }
        mViewDataBinding.setVariable(mVariableId, mDatas.get(position));
        return mViewDataBinding.getRoot();
    }
}