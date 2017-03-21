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
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setmOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.getLayoutPosition());
            }
        });
        holder.getBinding().getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemLongClickListener.onItemLongClick(holder.getLayoutPosition());
                return false;
            }
        });
        holder.getBinding().setVariable(mVariableId, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void updateData(List<T> datas) {
        this.mDatas.clear();
        this.mDatas = datas;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding binding;

    MyViewHolder(View itemView) {
        super(itemView);
    }

    ViewDataBinding getBinding() {
        return binding;
    }

    void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }
}