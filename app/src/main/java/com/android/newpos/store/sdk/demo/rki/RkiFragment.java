package com.android.newpos.store.sdk.demo.rki;

import static com.newpos.store.android.sdk.Constant.CLOUD_MESSAGE_TYPE_RKI_DOWN_CUSTOMER_KEYS;
import static com.newpos.store.android.sdk.Constant.CM_MSGID;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.newpos.store.sdk.demo.R;
import com.android.newpos.store.sdk.demo.base.AppUtils;
import com.android.newpos.store.sdk.demo.base.BaseFragment;
import com.android.newpos.store.sdk.demo.databinding.FragmentRkiBinding;
import com.newpos.store.android.sdk.Constant;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.ability.RkiAbility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName : RkiFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/5/29-10:34
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class RkiFragment extends BaseFragment<RkiViewModel> {
    private FragmentRkiBinding binding;
    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentRkiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<RkiViewModel> getClazz() {
        return RkiViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.bind.setOnClickListener(v -> getViewModel().bind());

        binding.downloadCustomer.setOnClickListener(v -> getViewModel().download());

        getViewModel().mKdh.observe(getViewLifecycleOwner(), this::appendLog);

    }
    private void appendLog(String message) {
        String old = binding.tvRkiResult.getText().toString();
        binding.tvRkiResult.setText(old + "\n" + message);
        binding.scrollResult.post(() -> binding.scrollResult.fullScroll(View.FOCUS_DOWN) );
    }
}


