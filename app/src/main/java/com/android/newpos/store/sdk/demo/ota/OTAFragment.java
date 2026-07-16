package com.android.newpos.store.sdk.demo.ota;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.newpos.store.sdk.demo.databinding.FragmentOtaBinding;
import com.android.newpos.store.sdk.demo.base.BaseFragment;

/**
 * @ClassName : OTAFragmemnt
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-10:37
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class OTAFragment extends BaseFragment<OTAViewModel> {

    private FragmentOtaBinding binding;

    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentOtaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<OTAViewModel> getClazz() {
        return OTAViewModel.class;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.query.setOnClickListener(v -> getViewModel().queryFirmware());

        binding.download.setOnClickListener(v -> getViewModel().downloadFirmware());

        binding.install.setOnClickListener(v -> {
            // 安装固件请按终端 ROM 能力自行接入（不依赖公开 sdk.jar）
        });

        getViewModel().getInfo().observe(getViewLifecycleOwner(), content -> {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Query Firmware Result:")
                    .setMessage(content)
                    .create().show();
        });

        getViewModel().getStatusMutableLiveData().observe(getViewLifecycleOwner(), firmwareDownloadStatus -> {
            binding.speed.setText(firmwareDownloadStatus.speed);
            binding.url.setText(firmwareDownloadStatus.firmwareInfo.url);
            if (firmwareDownloadStatus.total == -1) {
                // chunked transfer encoding data
                binding.progressBar.setIndeterminate(true);
            } else {
                binding.progressBar.setMax(firmwareDownloadStatus.total);
                binding.progressBar.setProgress(firmwareDownloadStatus.sofar);
            }

            binding.detail.setText(String.format("sofar: %d total: %d percent:%s",
                    firmwareDownloadStatus.sofar, firmwareDownloadStatus.total, firmwareDownloadStatus.percent));
        });
    }
}
