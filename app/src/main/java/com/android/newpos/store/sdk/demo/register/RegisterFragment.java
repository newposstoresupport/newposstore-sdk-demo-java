package com.android.newpos.store.sdk.demo.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.newpos.store.sdk.demo.MainApplication;
import com.android.newpos.store.sdk.demo.R;
import com.android.newpos.store.sdk.demo.base.AppUtils;
import com.android.newpos.store.sdk.demo.base.BaseFragment;
import com.android.newpos.store.sdk.demo.databinding.FragmentRegisterBinding;

/**
 * @ClassName : RegisterFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/5/26-14:06
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class RegisterFragment extends BaseFragment<RegisterViewModel> {
    private FragmentRegisterBinding binding;
    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<RegisterViewModel> getClazz() {
        return RegisterViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView clientIdView = binding.clientIdView;
        clientIdView.setText(AppUtils.getClientId());

        binding.register.setOnClickListener(v -> {
            MainApplication.getInstance().initStoreSdk(AppUtils.getClientId());
        });

        binding.clientId.setOnClickListener(v -> {
            EditText editText = new EditText(requireActivity());
            editText.setHint(R.string.client_id_hint);
            editText.setText(AppUtils.getClientId());
            new AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.app_name)
                    .setView(editText)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        String content = editText.getText().toString();
                        if(TextUtils.isEmpty(content)){
                            return;
                        }
                        AppUtils.putClientId(content);
                        Toast.makeText(requireActivity(), R.string.update_success, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        clientIdView.setText(AppUtils.getClientId());
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create().show();
        });
    }
}
