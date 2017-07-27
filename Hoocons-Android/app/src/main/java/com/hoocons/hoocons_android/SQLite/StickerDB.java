package com.hoocons.hoocons_android.SQLite;

import android.content.ContentValues;

import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.Sticker;

import org.aisen.android.common.context.GlobalContext;
import org.aisen.android.common.utils.FileUtils;
import org.aisen.android.common.utils.Logger;
import org.aisen.android.network.task.TaskException;
import org.aisen.android.network.task.WorkTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by hungnguyen on 7/27/17.
 */

public class StickerDB {
    private static List<Sticker> monkeyStickerSet;

    public static void initMonkeyStickerHashMap() {
        if (monkeyStickerSet != null) {
            monkeyStickerSet.clear();
        } else {
            monkeyStickerSet = new ArrayList<>();
        }

        new WorkTask<Void, Void, Void>() {
            @Override
            public Void workInBackground(Void... params) throws TaskException {
                InputStream in;
                try {
                    in = BaseApplication.getInstance().getAssets().open("emotions.properties");
                    Properties properties = new Properties();
                    properties.load(new InputStreamReader(in, "utf-8"));
                    Set<Object> keySet = properties.keySet();

                    for (Object key : keySet) {
                        String value = properties.getProperty(key.toString());

                        Sticker sticker = new Sticker(key.toString(), value);
                        monkeyStickerSet.add(sticker);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public static List<Sticker> getMonkeyStickerSet() {
        return monkeyStickerSet;
    }

    public static void setMonkeyStickerSet(List<Sticker> monkeyStickerSet) {
        StickerDB.monkeyStickerSet = monkeyStickerSet;
    }
}
