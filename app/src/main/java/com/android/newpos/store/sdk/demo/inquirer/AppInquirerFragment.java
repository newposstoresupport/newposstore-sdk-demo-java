package com.android.newpos.store.sdk.demo.inquirer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.newpos.store.sdk.demo.base.BaseFragment;
import com.android.newpos.store.sdk.demo.databinding.FragmentAppInquirerBinding;

/**
 * @ClassName : AppInquirerFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/12/25-14:51
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppInquirerFragment extends BaseFragment<AppInquirerViewModel> {
    private FragmentAppInquirerBinding binding;

    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentAppInquirerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<AppInquirerViewModel> getClazz() {
        return AppInquirerViewModel.class;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewModel().queryStatus();
        getViewModel().getStatus().observe(getViewLifecycleOwner(), status -> {
            String title = "App Inquirer Status:";
            binding.status.setText(title+status);
            binding.ready.setEnabled(status);
            binding.ready.setChecked(getViewModel().isReadyToUpdate());
        });

        getViewModel().getAppStatus().observe(getViewLifecycleOwner(), appIsReadyToUpdate -> {
            if(appIsReadyToUpdate){
                binding.appStatus.setText("App is idl, could be updated by STORE CLIENT APP now!");
            }else {
                binding.appStatus.setText("App is busy, can not be updated by STORE CLIENT APP now!");
            }
        });

        binding.ready.setOnCheckedChangeListener((buttonView, isChecked) -> getViewModel().updateAppStatus(isChecked));
    }
}
