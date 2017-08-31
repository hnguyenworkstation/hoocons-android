package com.hoocons.hoocons_android.ViewHolders;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 8/30/17.
 */

public class EventCardViewHolder extends ViewHolder {
    @Nullable
    @BindView(R.id.single_image)
    ImageView mSingleImage;

    @Nullable
    @BindView(R.id.text_content)
    TextView mTextContent;


    public EventCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initView(final int position) {
        assert mTextContent != null;

        if (position % 2 == 0) {
            mTextContent.setText("Your work is going to fill a large part of your life," +
                    " and the only way to be truly satisfied is to do what you believe is great work. " +
                    "And the only way to do great work is to love what you do. " +
                    "If you haven't found it yet, keep looking. Don't settle." +
                    " As with all matters of the heart, you'll know when you find it.");
            loadSingleImage("https://fossbytes.com/wp-content/uploads/2017/02/steve.png");
        } else {
            mTextContent.setText("Great things in business are never done by one person. They're done by a team of people.\n" +
                    "Read more at: https://www.brainyquote.com/quotes/quotes/s/stevejobs737723.html");
            loadSingleImage("http://graphicsheat.com/wp-content/uploads/2015/04/Steve-Jobs-Quotes-10.jpg");
        }
    }

    private void loadSingleImage(String url) {
        assert mSingleImage != null;
        BaseApplication.getInstance().getGlide()
                .load(url)
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .into(mSingleImage);
    }

    public void onViewRecycled() {
        BaseApplication.getInstance().getGlide().clear(mSingleImage);
    }
}
