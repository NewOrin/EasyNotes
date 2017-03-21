package com.neworin.easynotes.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.neworin.easynotes.adapter.ListViewCommonAdapter;
import com.neworin.easynotes.R;
import com.neworin.easynotes.event.SlideMenuEvent;
import com.neworin.easynotes.handlers.SlideMenuEventHandler;
import com.neworin.easynotes.model.NoteBookModel;
import com.neworin.easynotes.databinding.FragmentSlideMenuBinding;
import com.neworin.easynotes.ui.BaseFragment;
import com.neworin.easynotes.utils.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NewOrin Zhang
 * 02/16/2017
 * Project: EasyNotes
 * Email: neworin@163.com
 */

public class SlideMenuFragment extends BaseFragment {

    private FragmentSlideMenuBinding mBinding;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_slide_menu, container, false);
        mBinding = DataBindingUtil.bind(mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initEvent();
    }

    private void initView() {
        mBinding.setHandler(new SlideMenuEventHandler(getActivity()));
        List<NoteBookModel> datas = new ArrayList<>();
        datas.add(new NoteBookModel("笔记本", "1", true));
        datas.add(new NoteBookModel("笔记本", "3", false));
        datas.add(new NoteBookModel("笔记本", "2", true));
        ListViewCommonAdapter<NoteBookModel> adapter = new ListViewCommonAdapter<>(getActivity(), datas, R.layout.item_slide_menu_layout, com.neworin.easynotes.BR.notebookBean);
        mBinding.setAdapter(adapter);
        View footViewSettings = LayoutInflater.from(getActivity()).inflate(R.layout.slide_menu_footer_settings_layout, null);
        View footViewEdit = LayoutInflater.from(getActivity()).inflate(R.layout.slide_menu_footer_edit_layout, null);
        mBinding.slideMenuListview.addFooterView(footViewSettings);
        mBinding.slideMenuListview.addFooterView(footViewEdit);
        footViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SlideMenuEvent.ListItemEvent(Constant.SLIDE_ITEM_EDIT));
            }
        });
        footViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SlideMenuEvent.ListItemEvent(Constant.SLIDE_ITEM_SETTINGS));
            }
        });
    }

    private void initEvent() {
        mBinding.slideMenuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new SlideMenuEvent.ListItemEvent(position));
            }
        });
    }
}