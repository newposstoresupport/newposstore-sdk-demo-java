package com.android.newpos.store.sdk.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.newpos.store.sdk.demo.app.AppFragment;
import com.android.newpos.store.sdk.demo.common.CommonFragment;
import com.android.newpos.store.sdk.demo.inquirer.AppInquirerFragment;
import com.android.newpos.store.sdk.demo.lbs.LbsFragment;
import com.android.newpos.store.sdk.demo.ota.OTAFragment;
import com.android.newpos.store.sdk.demo.param.ParamFragment;
import com.android.newpos.store.sdk.demo.register.RegisterFragment;
import com.android.newpos.store.sdk.demo.rki.RkiFragment;
import com.android.newpos.store.sdk.demo.cloud.CloudFragment;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private static final String KEY_SCROLL_Y = "key_scroll_y";

    private int savedScrollY = 0;
    private ScrollView scrollView;

    private static class MenuItem {
        int iconRes;
        String title;
        Class<? extends Fragment> fragmentClass;

        MenuItem(int iconRes, String title, Class<? extends Fragment> fragmentClass) {
            this.iconRes = iconRes;
            this.title = title;
            this.fragmentClass = fragmentClass;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        scrollView = root.findViewById(R.id.scrollView);

        GridLayout gridLayout = root.findViewById(R.id.gridMenu);

        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(R.mipmap.common, getString(R.string.menu_common), CommonFragment.class));
        items.add(new MenuItem(R.mipmap.app, getString(R.string.menu_app), AppFragment.class));
        items.add(new MenuItem(R.mipmap.register, getString(R.string.menu_register), RegisterFragment.class));
        items.add(new MenuItem(R.mipmap.inquirer, getString(R.string.menu_inquirer), AppInquirerFragment.class));
        items.add(new MenuItem(R.mipmap.position, getString(R.string.menu_lbs), LbsFragment.class));
        items.add(new MenuItem(R.mipmap.params, getString(R.string.menu_param), ParamFragment.class));
        items.add(new MenuItem(R.mipmap.ota, getString(R.string.menu_ota), OTAFragment.class));
        items.add(new MenuItem(R.mipmap.cloud, getString(R.string.menu_cloud), CloudFragment.class));
        items.add(new MenuItem(R.mipmap.rki, getString(R.string.menu_rki), RkiFragment.class));

        for (MenuItem item : items) {
            View itemView = inflater.inflate(R.layout.item_menu_icon_title, gridLayout, false);
            ImageView icon = itemView.findViewById(R.id.icon);
            TextView title = itemView.findViewById(R.id.title);

            icon.setImageResource(item.iconRes);
            title.setText(item.title);

            itemView.setOnClickListener(v -> {
                try {
                    Fragment fragment = item.fragmentClass.newInstance();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            gridLayout.addView(itemView);
        }

        return root;
    }
    @Override
    public void onPause() {
        super.onPause();
        if (scrollView != null) {
            savedScrollY = scrollView.getScrollY();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            savedScrollY = savedInstanceState.getInt(KEY_SCROLL_Y, savedScrollY);
        }
        if (scrollView != null) {
            scrollView.post(() -> scrollView.scrollTo(0, savedScrollY));
        }
    }
}
