package com.panda.volleydemo;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;



/**
 * Created by PC on 2017/9/11.
 */

public class BitmapCache implements ImageLoader.ImageCache {

    private LruCache<String,Bitmap> mCache;

    public BitmapCache()
    {
        int max=10*1024*1024;
        mCache=new LruCache<String,Bitmap>(max)
        {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url,bitmap);
    }
}
