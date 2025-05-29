package com.android.newpos.store.sdk.demo.cloud;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.newpos.store.sdk.demo.R;
import com.android.newpos.store.sdk.demo.databinding.FragmentCloudBinding;
import com.android.newpos.store.sdk.demo.base.BaseFragment;
import com.newpos.store.android.sdk.Constant;

/**
 * @ClassName : CloudFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/29-16:23
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class CloudFragment extends BaseFragment<CloudViewModel> {
    private FragmentCloudBinding binding;
    private CloudMessageReceiver cloudMessageReceiver;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentCloudBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<CloudViewModel> getClazz() {
        return CloudViewModel.class;
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_CLOUD_MESSAGE_ARRIVED);
        filter.addAction(Constant.ACTION_CLOUD_MESSAGE_CLICKED);
        cloudMessageReceiver = new CloudMessageReceiver();

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            Toast.makeText(requireContext(), "registerForActivityResult:"+result, Toast.LENGTH_SHORT).show();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission();
        }

        binding.register.setOnClickListener(v -> {
            requireActivity().registerReceiver(cloudMessageReceiver, filter,
                    Constant.PERMISSION_RECEIVE_CLOUD_MESSAGE, null);
            Toast.makeText(requireContext(), R.string.register_cloud_success, Toast.LENGTH_SHORT).show();
        });

        binding.unRegister.setOnClickListener(v -> requireActivity().unregisterReceiver(cloudMessageReceiver));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(cloudMessageReceiver != null) {
                requireActivity().unregisterReceiver(cloudMessageReceiver);
            }
        }catch (Exception ignore){}
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkPermission(){
        NotificationManager manager = (NotificationManager) requireContext()
                .getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(!manager.areNotificationsEnabled()){
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}