package com.android.newpos.store.sdk.demo;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * 仪器冒烟：验证目标包名与当前 flavor 的 {@link BuildConfig#APPLICATION_ID} 一致。
 * <p>用例资产：SSDK-UI-001（见 {@code StoreSdk/docs/qa/test-cases/catalog.md}）。</p>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals(BuildConfig.APPLICATION_ID, appContext.getPackageName());
    }
}