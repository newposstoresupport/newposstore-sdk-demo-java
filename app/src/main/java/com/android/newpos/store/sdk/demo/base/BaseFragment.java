package com.android.newpos.store.sdk.demo.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.newpos.store.sdk.demo.R;

/**
 * @ClassName : BaseFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-10:45
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public abstract class BaseFragment<T extends BaseViewModel> extends Fragment {

    private T viewModel;

    protected AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewModel = createViewModel(getClazz());
        View root = getRoot(inflater, container);
        final TextView textView = root.findViewById(R.id.text);
        viewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        viewModel.getDialog().observe(getViewLifecycleOwner(), message -> new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.error_title)
                .setMessage(message)
                .setPositiveButton(R.string.i_see, (dialog, which) -> dialog.dismiss())
                .create().show());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getService().shutdownNow();
        viewModel = null;
    }

    public abstract View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    public abstract Class<T> getClazz();

    public T createViewModel(Class<T> clz){
        return new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(getActivity()
                        .getApplication())).get(clz);
    }

    public T getViewModel(){
        return viewModel;
    }
}
