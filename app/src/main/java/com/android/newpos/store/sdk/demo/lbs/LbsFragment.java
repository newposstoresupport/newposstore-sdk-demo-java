package com.android.newpos.store.sdk.demo.lbs;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.newpos.store.sdk.demo.databinding.FragmentLbsBinding;
import com.android.newpos.store.sdk.demo.base.BaseFragment;

/**
 * @ClassName : LbsFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/29-16:40
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class LbsFragment extends BaseFragment<LbsViewModel> {

    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentLbsBinding.inflate(inflater, container, false).getRoot();
    }

    @Override
    public Class<LbsViewModel> getClazz() {
        return LbsViewModel.class;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, 1, 0, "getLocation");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == 1){
            getViewModel().getLocation();
        }
        return super.onOptionsItemSelected(item);
    }
}