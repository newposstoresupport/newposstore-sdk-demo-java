package com.android.newpos.store.sdk.demo.terminallocation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.newpos.store.sdk.demo.R;
import com.android.newpos.store.sdk.demo.base.BaseFragment;
import com.android.newpos.store.sdk.demo.databinding.FragmentTerminalLocationBinding;

/**
 * 单独一页：选 provider、点定位、看 address/lat/lon/provider/time。
 */
public class TerminalLocationFragment extends BaseFragment<TerminalLocationViewModel> {

    private FragmentTerminalLocationBinding binding;

    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentTerminalLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<TerminalLocationViewModel> getClazz() {
        return TerminalLocationViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewModel().address.observe(getViewLifecycleOwner(), binding.tvTlAddress::setText);
        getViewModel().lat.observe(getViewLifecycleOwner(), binding.tvTlLat::setText);
        getViewModel().lon.observe(getViewLifecycleOwner(), binding.tvTlLon::setText);
        getViewModel().provider.observe(getViewLifecycleOwner(), binding.tvTlProvider::setText);
        getViewModel().time.observe(getViewLifecycleOwner(), binding.tvTlTime::setText);
        getViewModel().raw.observe(getViewLifecycleOwner(), binding.tvTlRaw::setText);

        binding.rgTlProvider.setOnCheckedChangeListener((group, checkedId) ->
                applyProviderFromCheckedId(checkedId));

        binding.btnGetTerminalLocation.setOnClickListener(v -> getViewModel().getLocation());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // RadioGroup 在此时才恢复勾选，且不触发 listener；按当前勾选同步 ViewModel
        if (binding != null) {
            applyProviderFromCheckedId(binding.rgTlProvider.getCheckedRadioButtonId());
        }
    }

    private void applyProviderFromCheckedId(int checkedId) {
        if (checkedId == R.id.rb_tl_google) {
            getViewModel().setSelectedProvider("google");
        } else if (checkedId == R.id.rb_tl_baidu) {
            getViewModel().setSelectedProvider("baidu");
        } else {
            getViewModel().setSelectedProvider(null);
        }
    }
}
