package com.sunstar.gyyp.base;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunstar.gyyp.R;
import com.sunstar.gyyp.Url;

import jp.wasabeef.glide.transformations.BitmapTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ImageViewAttrAdapter {

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(Url.INSTANCE.getBaseUrl()+url)
                .error(R.mipmap.load_error)
                .into(imageView);
    }

    @BindingAdapter({"app:avater"})
    public static void loadAvaterImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load((url==null || url.isEmpty()) ? "" :url.contains("storage") ? url : Url.INSTANCE.getBaseUrl() + url)
                .apply(bitmapTransform(new CropCircleTransformation()))
                .error(R.mipmap.head_image)
                .into(imageView);
    }

}