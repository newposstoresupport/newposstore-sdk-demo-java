package com.android.newpos.store.sdk.demo.base;

import static com.android.newpos.store.sdk.demo.base.DownloadWorker.KEY_MESSAGE_ID;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.android.newpos.store.sdk.demo.MainApplication;
import com.google.gson.JsonObject;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.dto.AttachFile;
import com.newpos.store.android.sdk.dto.Mode;
import com.newpos.store.android.sdk.util.ParamTemplateUtils;
import com.tencent.mmkv.MMKV;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @ClassName : AppUtils
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/5/26-16:48
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppUtils {
    public static final String XML_PREFIX = "<?xml";
    public static final String JSON_PREFIX = "{";
    public static final String JSON_SUFFIX = "}";
    private static final MMKV mmkv = MMKV.defaultMMKV();

    private static final String CLIENT_ID = "clientId";

    public static void putClientId(String clientId){
        mmkv.encode(CLIENT_ID, clientId);
    }

    public static String getClientId(){
        return mmkv.decodeString(CLIENT_ID);
    }

    public static void showToast(String message) {
        Disposable d= Observable.just( true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        b -> Toast.makeText(MainApplication.getContext(), message, Toast.LENGTH_SHORT).show(),
                        throwable -> Log.e("showToast", "Error while showing toast: " + throwable.getMessage(), throwable)
                );
    }
    public static void showToast(int msgId) {
        showToast(MainApplication.getContext().getString(msgId));
    }

    public static void startDownloadWorker(Context context, String msgId){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        Data inputData = new Data.Builder()
                .putString(KEY_MESSAGE_ID, msgId)
                .build();
        OneTimeWorkRequest request =
                new OneTimeWorkRequest.Builder(DownloadWorker.class)
                        .setConstraints(constraints)
                        .setInputData(inputData)
                        .build();

        WorkManager.getInstance(context).enqueue(request);
    }

    public static boolean unzipFiles(String filePath, String dir) {
        File source = new File(filePath);
        File zipDir = new File(dir);
        if(!zipDir.exists()){
            zipDir.mkdirs();
        }
        if (source.exists()) {
            ZipInputStream zis = null;
            FileInputStream fis = null;


            try {
                fis = new FileInputStream(source);
                zis = new ZipInputStream(fis);
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null && !entry.isDirectory()) {
                    writeFile(zipDir.getAbsolutePath(), zis, entry);
                }
                zis.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                closeIO(zis, fis);
            }
        }

        return true;
    }

    public static boolean replaceFiles(List<AttachFile> paramsFiles, JsonObject values, JsonObject wrapped){
        for (AttachFile attachFile : paramsFiles){
            BaseLog.d("replaceFiles attachFile:"+attachFile);
            File dir = new File(attachFile.fileDir);
            if(!dir.exists()){
                continue;
            }
            File[] files = dir.listFiles();
            if(files == null){
                continue;
            }

            //debug
//            {
//                String XML_TMP = "test_para.xml";
//                String XML_VAL = "sys.param.p";
//                String templateFile = attachFile.fileDir + "/" +XML_TMP;
//                String templateValue = attachFile.fileDir + "/" +XML_VAL;
//                BaseLog.d("templateFile:"+templateFile);
//                BaseLog.d("templateValue:"+templateValue);
//                return replaceXmlFile(templateFile, values, wrapped);
//            }

            for (File file: files){
                //TODO 后台需要关联文件名称，否则无法知道哪个是模板，哪个是参数
                String content = AppUtils.readFileFirstLine(file);
                if(TextUtils.isEmpty(content)){
                    continue;
                }

                if(content.startsWith(XML_PREFIX)){
                    boolean result = replaceXmlFile(file.getAbsolutePath(), values, wrapped);
                    BaseLog.d("replace "+file+" "+result);
                }else if(content.startsWith(JSON_PREFIX)){
                    boolean result = replaceJsonFile(file.getAbsolutePath(), values, wrapped);
                    BaseLog.d("replace "+file+" "+result);
                }
            }
        }

        return true;
    }

    public static boolean replaceXmlFile(String filePath, JsonObject values, JsonObject wrapped){
        BaseLog.w("replaceXmlFile:"+filePath);
        try {
            String fileContent = readFileContent(filePath);
            String replaceResult = fileContent;
            BaseLog.d(fileContent);
            JsonObject resolvedValues = new JsonObject();
            Set<String> keys = values.keySet();
            for (String key: keys){
                boolean needDecrypt = false;
                if(wrapped.has(key) && Objects.equals(wrapped.get(key).getAsString(), "1")){
                    BaseLog.w(key + " is encrypted, need to decrypt!");
                    needDecrypt = true;
                }
                String value = values.get(key).getAsString();
                if(needDecrypt){
                    String decrypt = decryptValue(value);
                    BaseLog.d("decrypt:"+decrypt);
                    if(!TextUtils.isEmpty(decrypt)){
                        value = decrypt;
                    }
                }
                BaseLog.d(key + " = " + value);
                resolvedValues.addProperty(key, value);
            }
            replaceResult = ParamTemplateUtils.replacePlaceholders(replaceResult, resolvedValues);
            BaseLog.d(replaceResult);

            if (!replaceResult.equals(fileContent)) {
                BaseLog.w(filePath + " is replaced!");
                writeFile(filePath, replaceResult);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean replaceJsonFile(String filePath, JsonObject values, JsonObject wrapped){
        return replaceXmlFile(filePath, values, wrapped);
    }

    public static void writeFile(String dir, ZipInputStream zis, ZipEntry entry) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File target = new File(dir, entry.getName());
            // write file
            fos = new FileOutputStream(target);
            bos = new BufferedOutputStream(fos);
            int read = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, read);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(fos, bos);
        }
    }

    public static String readFileFirstLine(File fin) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fin);
            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            if ((line = br.readLine()) != null) {
                return line;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static String readFileContent(String fileName) {
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            int length = fis.available();
            byte [] buffer = new byte[length];
            fis.read(buffer);
            String res = new String(buffer, StandardCharsets.UTF_8);
            fis.close();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static void writeFile(String path, String content){
        try {
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeIO(Closeable... closeables) {
        try {
            close(closeables);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Closeable... closeables) throws IOException {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        }
    }

    public static String escapeExprSpecialWord(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

    private static String escapeJson(String input) {
        if (!TextUtils.isEmpty(input)) {
            String[] fbsArr = {"\\", "\"", "\\/"};
            for (String key : fbsArr) {
                if (input.contains(key)) {
                    input = input.replace(key, "\\\\\\" + key);
                }
            }
        }
        return input;
    }

    private static String getEscape(char c) {
        switch (c) {
            case '&':
                return "&amp;";
            case '<':
                return "&lt;";
            case '>':
                return "&gt;";
            case '"':
                return "&quot;";
            case '\'':
                return "&apos;";
        }

        return String.valueOf(c);
    }

    public static String escapeXml(String src) {
        if (src == null || src.isEmpty()) {
            return src;
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            buf.append(getEscape(src.charAt(i)));
        }
        return Matcher.quoteReplacement(buf.toString());
    }

    public static String decryptValue(String value){
        try {
            byte[] doB64 = Base64.decode(value, Base64.DEFAULT);
            byte[] decrypt = BaseApi.getInstance().doEncryptionDecryption(doB64, Mode.DECRYPTION);
            BaseLog.d("decrypt:"+byteArrayToHexString(decrypt));
            if(decrypt == null){
                return null;
            }
            return new String(decrypt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String byteArrayToHexString(byte[] b) {
        if(b == null){
            return "";
        }
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
}
