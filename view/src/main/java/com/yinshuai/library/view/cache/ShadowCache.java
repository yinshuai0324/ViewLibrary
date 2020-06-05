package com.yinshuai.library.view.cache;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: 尹帅
 * @CreateDate: 2020-06-05 16:14
 * @UpdateDate: 2020-06-05 16:14
 */
public class ShadowCache {
    public static ConcurrentHashMap<String, SoftReference<Bitmap>> cacheMap = new ConcurrentHashMap<>();
}
