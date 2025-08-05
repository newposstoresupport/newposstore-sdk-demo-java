package com.android.newpos.store.sdk.demo.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.newpos.store.sdk.demo.base.BaseFragment;
import com.android.newpos.store.sdk.demo.databinding.FragmentCommonBinding;
import com.newpos.store.android.sdk.StoreSdk;

/**
 * @ClassName : CommonFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/7/31-17:04
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class CommonFragment extends BaseFragment<CommonViewModel> {
    private FragmentCommonBinding binding;
    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentCommonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<CommonViewModel> getClazz() {
        return CommonViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO 考虑增加输入包名或者选择列表
        binding.openAppDetail.setOnClickListener(v -> StoreSdk.getInstance().openAppDetail("com.newpos.rki"));

        binding.openDownloadList.setOnClickListener(v -> StoreSdk.getInstance().openDownloadList());

        binding.openSystemUpdate.setOnClickListener(v -> StoreSdk.getInstance().openOtaUpdate());
    }
}
