package com.android.newpos.store.sdk.demo.lbs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.newpos.store.sdk.demo.databinding.FragmentLbsBinding;
import com.android.newpos.store.sdk.demo.base.BaseFragment;

/**
 * @ClassName : LbsFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/29-16:40
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class LbsFragment extends BaseFragment<LbsViewModel> {

    private FragmentLbsBinding binding;

    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentLbsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<LbsViewModel> getClazz() {
        return LbsViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView locationView = binding.location;
        getViewModel().mLocation.observe(getViewLifecycleOwner(), locationView::setText);

        binding.getLocation.setOnClickListener(v -> getViewModel().getLocation());

        getViewModel().logs.observe(getViewLifecycleOwner(), this::appendLog);
    }

    private void appendLog(String text) {
        String old = binding.tvLbsResult.getText().toString();
        binding.tvLbsResult.setText((old + "\n" + text).trim());
        binding.scrollResult.post(() -> binding.scrollResult.fullScroll(View.FOCUS_DOWN));
    }



}