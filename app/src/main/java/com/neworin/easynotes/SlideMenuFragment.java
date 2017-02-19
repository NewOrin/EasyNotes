package com.neworin.easynotes;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neworin.easynotes.databinding.FragmentSlideMenuBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NewOrin Zhang
 * 02/16/2017
 * Project: EasyNotes
 * Email: neworin@163.com
 */

public class SlideMenuFragment extends BaseFragment {

    private FragmentSlideMenuBinding binding;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_slide_menu, container, false);
        binding = DataBindingUtil.bind(mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<SlideMenuNoteBookModel> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add(new SlideMenuNoteBookModel("笔记本", "2", false));
        }
        CommonAdapter<SlideMenuNoteBookModel> adapter = new CommonAdapter<>(getActivity(), datas, R.layout.item_slide_menu_layout, com.neworin.easynotes.BR.notebookBean);
        binding.setAdapter(adapter);
//        binding.slideMenuListview.setAdapter(adapter);
    }
}