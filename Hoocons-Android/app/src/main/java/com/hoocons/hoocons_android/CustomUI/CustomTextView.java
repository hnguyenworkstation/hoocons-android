package com.hoocons.hoocons_android.CustomUI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.SQLite.EmotionsDB;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import org.aisen.android.common.context.GlobalContext;
import org.aisen.android.common.utils.BitmapUtil;
import org.aisen.android.common.utils.KeyGenerator;
import org.aisen.android.common.utils.Logger;
import org.aisen.android.component.bitmaploader.BitmapLoader;
import org.aisen.android.component.bitmaploader.core.LruMemoryCache;
import org.aisen.android.component.bitmaploader.core.MyBitmap;
import org.aisen.android.network.task.TaskException;
import org.aisen.android.network.task.WorkTask;
import org.aisen.android.support.textspan.ClickableTextViewMentionLinkOnTouchListener;
import org.aisen.android.support.textspan.MyURLSpan;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hungnguyen on 7/22/17.
 */
public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
    static final String TAG = "CustomTextView";

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(@NonNull Runnable r) {
            int count = mCount.getAndIncrement();
            Logger.v(TAG, "new Thread " + "CustomTextView #" + count);
            return new Thread(r, "CustomTextView #" + count);
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(10);

    private static final Executor THREAD_POOL_EXECUTOR =
            new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory);

    public static LruMemoryCache<String, SpannableString> stringMemoryCache;

    private EmotionTask emotionTask;
    private String content;

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

	public CustomTextView(Context context) {
        super(context);
    }

    public void setContent(String text) {
        if (stringMemoryCache == null) {
            stringMemoryCache = new LruMemoryCache<String, SpannableString>(200) {
            };
        }

        boolean replace = false;

        if (TextUtils.isEmpty(text)) {
            super.setText(text);
            return;
        }

        if (!TextUtils.isEmpty(content) && content.equals(text))
            return;

        content = text;

        if (emotionTask != null)
            emotionTask.cancel(true);

        String key = KeyGenerator.generateMD5(text);
        SpannableString spannableString = stringMemoryCache.get(key);
        if (spannableString != null) {
            Logger.v(TAG, "Spannable is not null");

            super.setText(spannableString);
        } else {
            Logger.v(TAG, "Spannable is null");

            super.setText(text);
            emotionTask = new EmotionTask(this);
            emotionTask.executeOnExecutor(THREAD_POOL_EXECUTOR);
        }

        setClickable(false);
        setOnTouchListener(onTouchListener);
    }

    private static class EmotionTask extends WorkTask<Void, SpannableString, Boolean> {
        WeakReference<TextView> textViewRef;

        EmotionTask(TextView textView) {
            textViewRef = new WeakReference<TextView>(textView);
        }

        @Override
        public Boolean workInBackground(Void... params) throws TaskException {
            TextView textView = textViewRef.get();
            if (textView == null)
                return false;

            if (TextUtils.isEmpty(textView.getText()))
                return false;

            String text = textView.getText() + "";
            SpannableString spannableString = SpannableString.valueOf(text);
            Matcher localMatcher = Pattern.compile("\\[(\\S+?)\\]").matcher(spannableString);
            while (localMatcher.find()) {
                if (isCancelled())
                    break;

                String key = localMatcher.group(0);

                int k = localMatcher.start();
                int m = localMatcher.end();

                byte[] data = EmotionsDB.getEmotion(key);
                if (data == null)
                    continue;

                MyBitmap mb = BitmapLoader.getInstance()
                        .getImageCache().getBitmapFromMemCache(key, null);
                Bitmap b = null;
                if (mb != null) {
                    b = mb.getBitmap();
                }
                else {
                    b = BitmapFactory.decodeByteArray(data, 0, data.length);
                    int size = BaseActivity.getRunningActivity().getResources()
                            .getDimensionPixelSize(R.dimen.emotion_size);
                    b = BitmapUtil.zoomBitmap(b, size);
                    BitmapLoader.getInstance().getImageCache().addBitmapToMemCache(key, null, new MyBitmap(b, key));
                }

                ImageSpan l = new ImageSpan(GlobalContext.getInstance(), b, ImageSpan.ALIGN_BASELINE);
                spannableString.setSpan(l, k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            URLSpan[] urlSpans = spannableString.getSpans(0, spannableString.length(), URLSpan.class);
            MyURLSpan weiboSpan = null;
            for (URLSpan urlSpan : urlSpans) {
                weiboSpan = new MyURLSpan(urlSpan.getURL());
                int start = spannableString.getSpanStart(urlSpan);
                int end = spannableString.getSpanEnd(urlSpan);
                try {
                    spannableString.removeSpan(urlSpan);
                } catch (Exception e) {

                }
                spannableString.setSpan(weiboSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            publishProgress(spannableString);

            String key = KeyGenerator.generateMD5(spannableString.toString());
            stringMemoryCache.put(key, spannableString);
            Logger.v(TAG, String.format("Spannable TextView", stringMemoryCache.size()));
            return null;
        }

        @Override
        protected void onProgressUpdate(SpannableString... values) {
            super.onProgressUpdate(values);

            TextView textView = textViewRef.get();
            if (textView == null)
                return;

            try {
                if (values != null && values.length > 0)
                    textView.setText(values[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private OnTouchListener onTouchListener = new OnTouchListener() {
        ClickableTextViewMentionLinkOnTouchListener listener = new ClickableTextViewMentionLinkOnTouchListener();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return listener.onTouch(v, event);
        }
    };

}