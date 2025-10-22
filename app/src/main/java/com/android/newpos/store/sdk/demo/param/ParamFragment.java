package com.android.newpos.store.sdk.demo.param;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.newpos.store.sdk.demo.databinding.FragmentParamBinding;
import com.android.newpos.store.sdk.demo.base.BaseFragment;
import com.newpos.store.android.sdk.dto.AttachFile;
import com.newpos.store.android.sdk.dto.ParamDownloadResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : ParamFragment
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-10:55
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamFragment extends BaseFragment<ParamViewModel> {


    private FragmentParamBinding binding;
    @Override
    public View getRoot(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentParamBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public Class<ParamViewModel> getClazz() {
        return ParamViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.query.setOnClickListener(v -> getViewModel().queryParamFile());

        binding.download.setOnClickListener(v -> getViewModel().downloadParamFile());

        getViewModel().getInfo().observe(getViewLifecycleOwner(), content -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Query Params Result:")
                    .setMessage(content)
                    .create().show();
        });

        getViewModel().getParamDownloadResponseMutableLiveData().observe(getViewLifecycleOwner(), paramDownloadResponse -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Download Params Result:")
                    .setMessage(paramDownloadResponse.toString())
                    .setPositiveButton("View Files", (dialog, which) -> {
                        dialog.dismiss();
                        viewFiles(paramDownloadResponse);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        });

        getViewModel().getShowFileContent().observe(getViewLifecycleOwner(), content -> {
            new AlertDialog.Builder(requireActivity())
                    .setMessage(content)
                    .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                    .create().show();
        });
    }

    public void viewFiles(ParamDownloadResponse paramDownloadResponse){
        List<AttachFile> attachFiles = paramDownloadResponse.attachFiles;
        List<String> strings = new ArrayList<>();
        for (AttachFile file: attachFiles){
            strings.add(file.filePath);

        }
        ListView listView = new ListView(requireActivity());
        listView.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, strings));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String path = strings.get(position);
            getViewModel().readFile(path);
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        builder.setPositiveButton("ok", (dialog, which) -> dialog.dismiss());
        builder.setView(listView);
        builder.create().show();
    }

}