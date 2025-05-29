package com.android.newpos.store.sdk.demo.ota;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentOtaBinding.inflate(inflater, container, false).getRoot();
    }

    @Override
    public Class<OTAViewModel> getClazz() {
        return OTAViewModel.class;
    }
}
