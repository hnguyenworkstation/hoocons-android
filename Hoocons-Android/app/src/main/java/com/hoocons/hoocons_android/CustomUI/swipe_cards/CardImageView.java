package com.hoocons.hoocons_android.CustomUI.swipe_cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by hNguyen on 6/19/2017.
 */
public class CardImageView extends RoundedImageView {
    private CardEntity mUser;
    private boolean isLoadImgSucc = false;

    public CardImageView(Context context) {
        super(context);
    }

    public CardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        Drawable tempDrawable = drawable;
        if (drawable instanceof GlideBitmapDrawable) {
            isLoadImgSucc = true;
            /*if (mUser != null) {
                mUser.setEndLoadTimeAnchor();
            }*/
            tempDrawable = new RoundedDrawable(((GlideBitmapDrawable) drawable).getBitmap());
        }
        super.setImageDrawable(tempDrawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        /*if (GraphicsUtils.isValidBitmap(bm)) {
            isLoadImgSucc = true;
            if (mUser != null) {
                mUser.setEndLoadTimeAnchor();
            }
        }*/
    }

    public void setUser(CardEntity user) {
        this.mUser = user;
    }

    public void reset() {
        isLoadImgSucc = false;
    }

    public boolean isLoadImgSucc() {
        return isLoadImgSucc;
    }
}

