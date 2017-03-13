package com.neworin.easynotes.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by NewOrin Zhang on 2017/2/26.
 * E-Mail : NewOrinZhang@Gmail.com
 */

public class RecyclerViewCommonAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {

    private Context mContext;
    private int mLayoutId;
    private List<T> mDatas;
    private int mVariableId;

    public RecyclerViewCommonAdapter(Context context, List<T> datas, int layoutId, int variableId) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mDatas = datas;
        this.mVariableId = variableId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), mLayoutId, parent, false);
        MyViewHolder holder = new MyViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.getBinding().setVariable(mVariableId, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding binding;

    public MyViewHolder(View itemView) {
        super(itemView);
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }
}