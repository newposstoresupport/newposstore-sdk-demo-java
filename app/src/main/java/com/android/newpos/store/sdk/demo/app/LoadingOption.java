package com.android.newpos.store.sdk.demo.app;

public class LoadingOption {
    public boolean loading = false;
    public String loadingText = "";

    public LoadingOption(boolean loading) {
        this.loading = loading;
    }

    public LoadingOption(String loadingText) {
        this.loading = true;
        this.loadingText = loadingText;
    }

    public LoadingOption(boolean loading, String loadingText) {
        this.loading = loading;
        this.loadingText = loadingText;
    }
}
