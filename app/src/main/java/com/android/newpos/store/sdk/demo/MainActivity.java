package com.android.newpos.store.sdk.demo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import com.android.newpos.store.sdk.demo.databinding.ActivityMainBinding;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.newpos.store.android.sdk.StoreSdk;

/**
 * @ClassName : MainActivity
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/28-16:41
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(view -> {
            Snackbar.make(view,
                    getString(R.string.sdk_version, StoreSdk.getInstance().getVersion()),
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            new AlertDialog.Builder(MainActivity.this)
//                    .setTitle(R.string.result_title)
//                    .setMessage(auth)
//                    .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
//                    .create().show();
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new MainFragment())
                .commit();

        updateHeaderVisibility(true);
        FileDownloadUtils.setDefaultSaveRootPath(getFilesDir().getAbsolutePath());
    }

    public boolean isPhysical480x480Device(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int w = dm.widthPixels;
        int h = dm.heightPixels;

        boolean square = Math.abs(w - h) <= 10;

        double wIn = w / dm.xdpi;
        double hIn = h / dm.ydpi;
        double diag = Math.hypot(wIn, hIn);

        return square && diag <= 4.5;
    }

    public void updateHeaderVisibility(boolean isMainPage) {
        if (isPhysical480x480Device(this)) {
            setHeaderVisible(isMainPage);
        } else {
            setHeaderVisible(true);
        }
    }

    private void setHeaderVisible(boolean visible) {
        View header = findViewById(R.id.header);
        View container = findViewById(R.id.container);
        ConstraintLayout root = findViewById(R.id.root);
        header.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}