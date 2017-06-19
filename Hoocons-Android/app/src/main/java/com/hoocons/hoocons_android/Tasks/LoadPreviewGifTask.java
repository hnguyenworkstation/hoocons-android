package com.hoocons.hoocons_android.Tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.LoadedGifUriRequest;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hungnguyen on 6/18/17.
 */

public class LoadPreviewGifTask extends AsyncTask<Void, Void, Uri> {
    private String mp4Link;
    private Context context;

    public LoadPreviewGifTask(Context context, String mp4Link) {
        this.mp4Link = mp4Link;
        this.context = context;
    }

    @Override
    protected void onPostExecute(Uri uri) {
        if (uri != null) {
            EventBus.getDefault().post(new LoadedGifUriRequest(uri));
        } else {
            EventBus.getDefault().post(new BadRequest());
        }
    }

    @Override
    protected Uri doInBackground(Void... params) {
        try {
            return loadGiffy(context, mp4Link);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Uri loadGiffy(Context context, String videoUrl) throws Exception {
        final File file = new File(context.getFilesDir().getPath(),
                "hoocons_giphy" + System.currentTimeMillis() + ".gif");

        if (!file.createNewFile()) {
            // file already exists
        }

        URL url = new URL(videoUrl);
        URLConnection connection = url.openConnection();
        connection.setReadTimeout(5000);
        connection.setConnectTimeout(30000);

        InputStream is = connection.getInputStream();
        BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
        FileOutputStream outStream = new FileOutputStream(file);

        byte[] buffer = new byte[1024 * 5];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        outStream.flush();
        outStream.close();
        inStream.close();
        is.close();

        return Uri.fromFile(file);
    }
}
