package com.android.newpos.store.sdk.demo.rki;

import android.os.Bundle;
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
import com.newpos.store.android.sdk.StoreSdk;

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

        binding.bind.setOnClickListener(v -> {
            StoreSdk.getInstance().rkiAbility().bindRkiService();
        });

        binding.downloadCustomer.setOnClickListener(v -> {
            Toast.makeText(requireContext(), R.string.download_customer_keys_prompt, Toast.LENGTH_SHORT).show();
            String kdhUrl = "";//from newstore platform
            StoreSdk.getInstance().rkiAbility().downloadCustomerKeys(AppUtils.getClientId(), kdhUrl, (code, message, keyList) -> {

            });
        });

        binding.downloadCa.setOnClickListener(v -> {
            StoreSdk.getInstance().rkiAbility().downloadRootCa();
        });
    }
}
