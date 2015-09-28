package com.dennisxu.lib.core.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.dennisxu.lib.core.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xuyang on 15/9/24.
 */
public class ImageHelper {

    protected ImageLoader imageLoader          = ImageLoader.getInstance();

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public DisplayImageOptions options;

    private static ImageHelper   sInstance            = null;
    private Context mContext;

    public static synchronized ImageHelper getInstance( Context context ) {
        if (sInstance == null) {
            sInstance = new ImageHelper(context);
        }
        return sInstance;
    }

    private ImageHelper(Context context) {
        mContext = context.getApplicationContext();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        //默认图标
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).build();
    }

    public void displayImage( String uri, ImageView imageAware ) {

        imageLoader.displayImage(uri, imageAware, options, animateFirstListener);
    }

    public void displayImageWithListener( String uri, ImageView imageAware ,ImageLoadingListener listener) {

        imageLoader.displayImage(uri, imageAware, options, animateFirstListener);
    }

    public void displayImage( String uri, ImageView imageview, DisplayImageOptions options ) {
        imageLoader.displayImage(uri, imageview, options);
    }

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete( String imageUri, View view, Bitmap loadedImage ) {

            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
