package com.android.newpos.store.sdk.demo.param;

import static com.android.newpos.store.sdk.demo.base.DownloadWorker.ACTION_DOWNLOAD_FINISH;
import static com.android.newpos.store.sdk.demo.base.DownloadWorker.KEY_PATH_DIR;
import static com.android.newpos.store.sdk.demo.base.DownloadWorker.KEY_RESULT;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.newpos.store.sdk.demo.base.AppUtils;
import com.android.newpos.store.sdk.demo.databinding.FragmentParamBinding;
import com.android.newpos.store.sdk.demo.base.BaseFragment;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.dto.AttachFile;
import com.newpos.store.android.sdk.dto.ParamDownV2Result;
import com.newpos.store.android.sdk.dto.ParamDownloadResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private String msgId = null;
    public static ParamFragment newInstance(String msgId){
        ParamFragment fragment = new ParamFragment();
        fragment.msgId = msgId;
        return fragment;
    }

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
        BaseLog.w("ParamFragment msgId:"+msgId);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DOWNLOAD_FINISH);
        requireContext().registerReceiver(receiver, filter);

        binding.query.setOnClickListener(v -> getViewModel().queryParamFile());

        binding.download.setOnClickListener(v -> getViewModel().downloadParamFile());

        binding.v2Query.setOnClickListener(v -> getViewModel().queryParamFileV2());
        binding.v2Download.setOnClickListener(v -> getViewModel().downloadParamFileV2());
        binding.v2DownloadOnekey.setOnClickListener(v -> getViewModel().downloadOneKey(msgId));

        getViewModel().getInfo().observe(getViewLifecycleOwner(), content -> {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Query Params Result:")
                    .setMessage(content)
                    .create().show();
        });

        getViewModel().getParamDownloadResponseMutableLiveData().observe(getViewLifecycleOwner(), paramDownloadResponse -> {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Download Params Result:")
                    .setMessage(paramDownloadResponse.toString())
                    .setPositiveButton("View Files", (dialog, which) -> {
                        dialog.dismiss();
                        viewFiles(paramDownloadResponse);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        });

        getViewModel().getV2ResultLiveData().observe(getViewLifecycleOwner(), paramDownV2Results -> {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Download Params Result(V2):")
                    .setMessage(paramDownV2Results.toString())
                    .setPositiveButton("View Files", (dialog, which) -> {
                        dialog.dismiss();
                        viewFilesV2(paramDownV2Results);
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

        if(!TextUtils.isEmpty(msgId)){
            getViewModel().downloadOneKey(msgId);
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Objects.equals(intent.getAction(), ACTION_DOWNLOAD_FINISH)){

                BaseLog.w("download worker has finished");
                getViewModel().dismiss();

                boolean result = intent.getBooleanExtra(KEY_RESULT, false);
                if(result){
                    String dir = intent.getStringExtra(KEY_PATH_DIR);
                    if(!TextUtils.isEmpty(dir)){
                        viewFilesV2Ex(dir);
                    }
                }else{
                    new AlertDialog.Builder(requireActivity())
                            .setTitle("Download Params Result(V2):")
                            .setMessage("Download failed, please check log!")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .create().show();
                }
            }
        }
    };

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

    public void viewFilesV2(List<ParamDownV2Result> paramDownV2Results){
        List<String> strings = new ArrayList<>();
        List<String> filePaths = new ArrayList<>();
        //TODO 注意文件名称
        for (ParamDownV2Result v2Result: paramDownV2Results){
            for (AttachFile attachFile : v2Result.attachFiles) {
                String dir = attachFile.fileDir;
                File[] files = new File(dir).listFiles();
                if(files == null){
                    continue;
                }
                for (File file: files){
                    strings.add(v2Result.packageName + "[" + file + "]");
                    filePaths.add(file.getAbsolutePath());
                }
            }
        }
        ListView listView = new ListView(requireActivity());
        listView.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, strings));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String path = filePaths.get(position);
            getViewModel().readFile(path);
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        builder.setPositiveButton("ok", (dialog, which) -> dialog.dismiss());
        builder.setView(listView);
        builder.create().show();
    }

    public void viewFilesV2Ex(String dir){
        List<String> strings = new ArrayList<>();
        File[] files = new File(dir).listFiles();
        if(files == null){
            return;
        }
        for (File file: files){
            strings.add(file.getAbsolutePath());
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