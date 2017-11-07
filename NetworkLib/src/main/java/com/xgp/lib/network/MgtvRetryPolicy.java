package com.xgp.lib.network;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

/**
 * description:该类实现了网络请求重试策略
 * <p>
 * author: Created by xianggengping on 2017/10/24.
 * <p>
 * email: xianggengping@163.com
 */

public class MgtvRetryPolicy implements RetryPolicy {
    /**
     * 当重试的域名中没有带schema时，使用这个值
     */
    private static final String DEFAULT_SCHEMA = "http://";
    /**
     * 请求域名前缀
     */
    private static final String PREFIX = "http";
    /**
     * 链接超时时间
     */
    private static final int MGTV_CONNECT_TIMEOUT = 2500;
    /**
     * 读取超时时间
     */
    private static final int MGTV_READ_TIMEOUT = 2500;

    private int mRetryCount;
    private int mTotalRetryCount;
    private List<String> mRetryDomains;
    private Request mRequest;

    public MgtvRetryPolicy(Request request) {
        this.mRequest = request;
    }

    @Override
    public int getCurrentTimeout() {
        return 0;
    }

    @Override
    public int getConnectTimeoutMs() {
        return MGTV_CONNECT_TIMEOUT;
    }

    @Override
    public int getReadTimeoutMs() {
        return MGTV_READ_TIMEOUT;
    }

    @Override
    public int getCurrentRetryCount() {
        return mRetryCount;
    }

    @Override
    public void retry(VolleyError error) throws VolleyError {
        mRetryCount++;
        if (mRetryCount > mTotalRetryCount) {
            throw error;
        } else {
            if (mRequest != null) {
                String retryDomain = mRetryDomains.get(mRetryCount - 1);
                mRequest.setRequestDomain(retryDomain);
            }
        }
    }

    /**
     * 设置重试域名列表
     */
    public void setRetryDomains(List<String> retryDomainList) {
        List<String> list = checkRetryDomain(retryDomainList);
        this.mRetryDomains = list;
        if (mRetryDomains != null) {
            mTotalRetryCount = mRetryDomains.size();
        }
    }

    private List<String> checkRetryDomain(List<String> retryDomains) {
        if (retryDomains == null) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < retryDomains.size(); i++) {
            String retryDomain = retryDomains.get(i);
            if (!TextUtils.isEmpty(retryDomain)) {
                if (!retryDomain.startsWith(PREFIX)) {
                    list.add(DEFAULT_SCHEMA + retryDomain);
                } else {
                    list.add(retryDomain);
                }
            }
        }
        return list;
    }
}
