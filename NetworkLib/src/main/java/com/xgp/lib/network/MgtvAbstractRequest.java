package com.xgp.lib.network;

import android.text.TextUtils;

import com.android.volley.Request;

import java.util.List;

/**
 * description:
 * <p>
 * author: Created by xianggengping on 2017/10/23.
 * <p>
 * email: xianggengping@163.com
 */

public abstract class MgtvAbstractRequest<V> {
    private static final long DEFAULT_CACHE_PERIOD = 30 * 60 * 1000;
    private static final long DEFAULT_COMPLETE_CACHE_TIME = 30 * 60 * 1000;

    public enum RequestMethod {
        GET, POST
    }

    /**
     * 请求方式，get或post
     */
    public int mRequestMethod = Request.Method.GET;
    /**
     * 请求回调
     */
    private TaskCallback<V> mCallback;
    /**
     * 请求参数
     */
    private MgtvBaseParameter mBaseParameter;
    /**
     * 是否需要缓存
     */
    private boolean mIsCache;

    /**
     * 构造http请求
     *
     * @param callback  回调接口
     * @param parameter 请求参数
     */
    public MgtvAbstractRequest(TaskCallback<V> callback, MgtvBaseParameter parameter) {
        this.mCallback = callback;
        this.mBaseParameter = parameter.combineParams();
    }

    /**
     * 开始解析数据，该方法由子类进行实现
     *
     * @param response
     * @return
     */
    public abstract V parseData(String response);

    public abstract List<String> getRetryDomains();

    /**
     * 获取请求路径
     *
     * @return
     */
    public abstract String getRequestPath();

    /**
     * 设置缓存时间，可继承子类进行自定义
     *
     * @return
     */
    public long getCachePeriod() {
        return DEFAULT_CACHE_PERIOD;
    }

    /**
     * 设置完全缓存的时间，可继承子类进行自定义
     *
     * @return
     */
    public long getCompleteCacheTime() {
        return DEFAULT_COMPLETE_CACHE_TIME;
    }

    /**
     * 获取请求回调
     *
     * @return
     */
    TaskCallback<V> getTaskCallback() {
        return mCallback;
    }

    /**
     * 获取请求地址（域名+路径+参数）
     *
     * @return
     */
    protected String getRequestUrl() {
        StringBuilder builder = new StringBuilder();
        String requestPath = getRequestPath();
        if (mRequestMethod == Request.Method.GET) {
            if (mBaseParameter == null) {
                return builder.append(requestPath).toString();
            }
            if (!TextUtils.isEmpty(requestPath) && requestPath.contains("?")) {
                builder.append(requestPath).append("&").append(mBaseParameter.buildParameter());
            } else {
                builder.append(requestPath).append("?").append(mBaseParameter.buildParameter());
            }
        } else {
            builder.append(getRequestPath());
        }
        return builder.toString();
    }

    int getRequestMethod() {
        return mRequestMethod;
    }

    MgtvBaseParameter getParameter() {
        return this.mBaseParameter;
    }

    /**
     * 开始http请求
     */
    public void execute() {
        this.execute(false);
    }

    /**
     * 开始http请求
     *
     * @param isCache 当前请求是否需要缓存
     */
    public void execute(boolean isCache) {
        this.execute(RequestMethod.GET, isCache);
    }

    public void execute(RequestMethod requestMethod, boolean isCache) {
        if (requestMethod == RequestMethod.GET) {
            mRequestMethod = Request.Method.GET;
        } else if (requestMethod == RequestMethod.POST) {
            mRequestMethod = Request.Method.POST;
        }
        this.mIsCache = isCache;
        INetWorkInterface workInterface = NetWorkFactory.newNetworkImpl();
        workInterface.execute(this);
    }

    /**
     * 当前请求是否需要缓存
     *
     * @return
     */
    public boolean isCache() {
        return mIsCache;
    }

    /**
     * 停止当前的网络请求
     */
    public void stop() {
        INetWorkInterface workInterface = NetWorkFactory.newNetworkImpl();
        workInterface.stop(this);
    }
}
