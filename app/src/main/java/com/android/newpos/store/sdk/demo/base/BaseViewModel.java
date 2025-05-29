package com.android.newpos.store.sdk.demo.base;

import android.app.Application;
import android.os.Build;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.newpos.store.sdk.demo.R;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @ClassName : BaseViewModel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-10:44
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public abstract class BaseViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mDialog;
    public BaseViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue(getTitle());
        mDialog = new MutableLiveData<>();
    }

    public abstract String getTitle();

    public MutableLiveData<String> getText(){
        return mText;
    }

    public MutableLiveData<String> getDialog(){
        return mDialog;
    }

    public ExecutorService executorService;

    public ExecutorService getService(){
        if(null == executorService){
            executorService = Executors.newCachedThreadPool();
        }
        return executorService;
    }

    private CompositeDisposable mCompositeDisposable;

    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    protected void showError(Throwable throwable){
        String msg = throwable.getMessage();
        if(throwable instanceof BaseException){
            msg = ((BaseException) throwable).msg;
        }
        mDialog.postValue(msg);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        BaseLog.d("BaseViewModel>onCleared");
        if (executorService != null) {
            executorService.shutdownNow();
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    public static class DialogInfo{
        public boolean show = false;
        public String info = "";

        public DialogInfo(boolean show) {
            this.show = show;
        }

        public DialogInfo(boolean show, String info) {
            this.show = show;
            this.info = info;
        }
    }
}
