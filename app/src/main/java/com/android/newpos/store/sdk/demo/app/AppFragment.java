package com.android.newpos.store.sdk.demo.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.newpos.store.sdk.demo.R;
import com.android.newpos.store.sdk.demo.base.BaseFragment;
import com.android.newpos.store.sdk.demo.databinding.FragmentAppBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : AppFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/7/31-17:23
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppFragment extends BaseFragment<AppViewModel> {
    private FragmentAppBinding binding;
    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentAppBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<AppViewModel> getClazz() {
        return AppViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.getApps.setOnClickListener(v -> getViewModel().getAppsByPackageName());
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        AppAdapter appAdapter = new AppAdapter(R.layout.item_app, new ArrayList<>());
        appAdapter.openLoadAnimation();
        appAdapter.setListener(storeApp -> getViewModel().downloadForUpdates(storeApp));
        recyclerView.setAdapter(appAdapter);
        getViewModel().getAppList().observe(getViewLifecycleOwner(), appAdapter::setNewData);
    }


}
