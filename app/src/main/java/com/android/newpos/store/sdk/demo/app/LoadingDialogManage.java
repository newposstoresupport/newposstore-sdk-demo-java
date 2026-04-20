package com.android.newpos.store.sdk.demo.app;
import android.app.Activity;
import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import com.android.newpos.store.sdk.demo.R;

public class LoadingDialogManage {
    private static volatile LoadingDialogManage instance;
    private LoadingDialog dialog;

    private void LoadingDialogManager() {}

    public static LoadingDialogManage getInstance() {
        if (instance == null) {
            synchronized (LoadingDialogManage.class) {
                if (instance == null) {
                    instance = new LoadingDialogManage();
                }
            }
        }
        return instance;
    }

    /** 显示加载框 */
    public synchronized void show(Context context, String message) {
        dismiss();

        if (!(context instanceof Activity) || ((Activity) context).isFinishing() || ((Activity) context).isDestroyed()) {
            return;
        }

        dialog = new LoadingDialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        AppCompatTextView label = dialog.findViewById(R.id.tv);
        if (label != null) {
            label.setText(message);
        }

        dialog.show();
    }

    /** 隐藏加载框 */
    public synchronized void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }
}
