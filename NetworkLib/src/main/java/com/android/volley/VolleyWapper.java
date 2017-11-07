package com.android.volley;

import com.android.volley.toolbox.Volley;
import com.mgtv.tv.lib.core.ContextProvider;

/**
 * description: Volley的包装类，该类是一个单例设计模式
 * <p>
 * author: Created by xianggengping on 2017/10/24.
 * <p>
 * email: gengping@mgtv.com
 */

public final class VolleyWapper {
    private static RequestQueue mRequestQueue;

    /**
     * 获取RequestQueue实例
     * @return
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            synchronized (VolleyWapper.class) {
                if (mRequestQueue == null) {
                    mRequestQueue = Volley.newRequestQueue(
                            ContextProvider.getApplicationContext());
                }
            }
        }
        return mRequestQueue;
    }
}
