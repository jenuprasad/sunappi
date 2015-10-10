package androidlibs.jenuine.com.androidlibs;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadService extends IntentService {

    private static final String TAG = "App";
    public DownloadService() {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */

            String results = downloadData(url);

        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private String downloadData(String url) {
        try {

            String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
//            String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));

            URL u = new URL(url);
            InputStream is = u.openStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            File folder = new File(Environment.getExternalStorageDirectory() + "/sunappi");
            if (!folder.exists())
                folder.mkdir();
            String path = folder.getAbsolutePath() + "/" + fileName;
            Log.v(TAG, path);
            FileOutputStream fos = new FileOutputStream(new File(path));
            while ((length = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }
        return "";
    }

}