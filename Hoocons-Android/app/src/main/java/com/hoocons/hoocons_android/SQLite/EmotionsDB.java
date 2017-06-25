package com.hoocons.hoocons_android.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.aisen.android.common.context.GlobalContext;
import org.aisen.android.common.utils.FileUtils;
import org.aisen.android.common.utils.Logger;
import org.aisen.android.network.task.TaskException;
import org.aisen.android.network.task.WorkTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.Emotion;
import com.hoocons.hoocons_android.Models.Emotions;

public class EmotionsDB {
    private static final String TAG = EmotionsDB.class.getSimpleName();
    private static SQLiteDatabase emotionsDb;

    static {
        emotionsDb = new SqliteDbHelper(BaseApplication.getInstance(), "hoocons_emotions.db", 1).getWritableDatabase();
    }

    static class SqliteDbHelper extends SQLiteOpenHelper {

        SqliteDbHelper(Context context, String dbName, int dbVersion) {
            super(context, dbName, null, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            dropDb(db);
            onCreate(db);
        }
    }

    static void dropDb(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                db.execSQL("DROP TABLE " + cursor.getString(0));
            }
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    public static void checkEmotions() {
        Cursor cursor = null;

        boolean tableExist = false;
        try {
            String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='" + EmotionTable.table + "' ";

            cursor = emotionsDb.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    tableExist = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            cursor = null;
        }

        if (!tableExist) {
            Logger.v(TAG, "create emotions table");

            String sql = String.format("create table %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT , %s TEXT, %s BOLB)", EmotionTable.table,
                    EmotionTable.id, EmotionTable.key, EmotionTable.file, EmotionTable.value);
            emotionsDb.execSQL(sql);
        } else {
            Logger.v(TAG, "emotions table exist");
        }

        boolean insertEmotions = true;
        try {
            cursor = emotionsDb.rawQuery(" select count(*) as c from " + EmotionTable.table, null);
            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                if (count == 159)
                    insertEmotions = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (insertEmotions) {
            Logger.v(TAG, "insert emotions");
            new WorkTask<Void, Void, Void>() {
                @Override
                public Void workInBackground(Void... params) throws TaskException {
                    InputStream in;
                    try {
                        in = GlobalContext.getInstance().getAssets().open("emotions.properties");
                        Properties properties = new Properties();
                        properties.load(new InputStreamReader(in, "utf-8"));
                        Set<Object> keySet = properties.keySet();

                        emotionsDb.beginTransaction();
                        emotionsDb.execSQL(String.format("delete from %s", EmotionTable.table));

                        for (Object key : keySet) {
                            String value = properties.getProperty(key.toString());
                            Logger.w(TAG, String.format("emotion's key(%s), value(%s)", key, value));

                            ContentValues values = new ContentValues();
                            values.put(EmotionTable.key, key.toString());
                            byte[] emotion = FileUtils.readStreamToBytes(GlobalContext.getInstance().getAssets().open(value));
                            values.put(EmotionTable.value, emotion);
                            values.put(EmotionTable.file, value);

                            emotionsDb.insert(EmotionTable.table, EmotionTable.id, values);
                        }

                        emotionsDb.setTransactionSuccessful();
                        emotionsDb.endTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } else {
            Logger.v(TAG, "emotions exist");
        }
    }

    public static byte[] getEmotion(String key) {
        Cursor cursor = emotionsDb.rawQuery(" SELECT " + EmotionTable.value + " FROM " + EmotionTable.table + " WHERE " + EmotionTable.key + " = ? ",
                new String[] { key });
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(cursor.getColumnIndex(EmotionTable.value));
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return null;
    }

    public static Emotions getEmotions(String type) {
        Emotions emotions = new Emotions();
        emotions.setEmotions(new ArrayList<Emotion>());

        String query = type.indexOf("lxh_") == -1 ? "unlike" : "like";
        query = "like";
        Cursor cursor = emotionsDb.rawQuery(" SELECT * FROM " + EmotionTable.table + " WHERE " + EmotionTable.file + " " + query + " '" + type
                + "%' order by org_aisen_weibo_sina_file", null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    byte[] data = cursor.getBlob(cursor.getColumnIndex(EmotionTable.value));
                    String key = cursor.getString(cursor.getColumnIndex(EmotionTable.key));

                    Emotion emotion = new Emotion();
                    emotion.setData(data);
                    emotion.setKey(key);

                    emotions.getEmotions().add(emotion);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return emotions;
    }

    static class EmotionTable {
        static final String table = "org_aisen_weibo_sina_emotions";
        static final String id = "org_aisen_weibo_sina_id";
        static final String key = "org_aisen_weibo_sina_key";
        static final String file = "org_aisen_weibo_sina_file";
        static final String value = "org_aisen_weibo_sina_value";
    }

}
