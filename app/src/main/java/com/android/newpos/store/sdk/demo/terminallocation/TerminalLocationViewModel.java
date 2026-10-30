package com.android.newpos.store.sdk.demo.terminallocation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.android.newpos.store.sdk.demo.app.LoadingOption;
import com.android.newpos.store.sdk.demo.base.BaseViewModel;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.TerminalLocationResponse;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Demo：选 provider 后调 {@code terminalLocationAbility}，展示字段与缓存 time。
 */
public class TerminalLocationViewModel extends BaseViewModel {

    public final MutableLiveData<String> address = new MutableLiveData<>();
    public final MutableLiveData<String> lat = new MutableLiveData<>();
    public final MutableLiveData<String> lon = new MutableLiveData<>();
    public final MutableLiveData<String> provider = new MutableLiveData<>();
    public final MutableLiveData<String> time = new MutableLiveData<>();
    public final MutableLiveData<String> raw = new MutableLiveData<>();

    @Nullable
    private String selectedProvider;

    public TerminalLocationViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * @param provider {@code google} / {@code baidu}；{@code null} 表示跟商店设置
     */
    public void setSelectedProvider(@Nullable String provider) {
        selectedProvider = provider;
    }

    public void getLocation() {
        showLoading(new LoadingOption("Retrieving terminal location..."));
        addSubscribe(Observable.just(selectedProvider == null ? "" : selectedProvider)
                .observeOn(Schedulers.io())
                .map(value -> {
                    String providerArg = value.isEmpty() ? null : value;
                    return StoreSdk.getInstance().terminalLocationAbility().getLocation(providerArg);
                })
                .subscribe(response -> {
                    dismissLoading();
                    bind(response);
                }, throwable -> {
                    dismissLoading();
                    showError(throwable);
                })
        );
    }

    private void bind(TerminalLocationResponse response) {
        if (response == null) {
            address.postValue("");
            lat.postValue("");
            lon.postValue("");
            provider.postValue("");
            time.postValue("");
            raw.postValue("Get terminal location failed");
            return;
        }
        address.postValue(emptyToDash(response.address));
        lat.postValue(emptyToDash(response.lat));
        lon.postValue(emptyToDash(response.lon));
        provider.postValue(emptyToDash(response.provider));
        time.postValue(formatTime(response.time));
        raw.postValue(BaseUtils.toJson(response));
    }

    private static String emptyToDash(String value) {
        return value == null || value.isEmpty() ? "-" : value;
    }

    /** 页面展示用。协议字段仍是毫秒，原始 JSON 里保持原值。 */
    private static String formatTime(Long time) {
        if (time == null || time <= 0) {
            return "-";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(time));
    }

    @Override
    public String getTitle() {
        return "Terminal Location";
    }
}
