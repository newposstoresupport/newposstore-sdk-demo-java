package com.android.newpos.store.sdk.demo.param;

import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;

import com.android.newpos.store.sdk.demo.databinding.FragmentParamBinding;
import com.android.newpos.store.sdk.demo.base.BaseFragment;

/**
 * @ClassName : ParamFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-10:55
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamFragment extends BaseFragment<ParamViewModel> {
    private FragmentParamBinding binding;
    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentParamBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<ParamViewModel> getClazz() {
        return ParamViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.query.setOnClickListener(v -> getViewModel().queryParamFile());

        binding.download.setOnClickListener(v -> getViewModel().downloadParamFile());

        getViewModel().getInfo().observe(getViewLifecycleOwner(), content -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Query Params Result:")
                    .setMessage(content)
                    .create().show();
        });
    }
}