package com.android.newpos.store.sdk.demo;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.newpos.store.sdk.demo.databinding.ActivityMainBinding;
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
    }
}