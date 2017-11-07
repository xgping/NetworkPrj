package com.xgp.lib.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyWapper;
import com.android.volley.toolbox.StringRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * description:
 * <p>
 * author: Created by xianggengping on 2017/10/24.
 * <p>
 * email: xianggengping@163.com
 */

public class NetWorkVolleyImpl implements INetWorkInterface<Request> {

    private static final String TAG = "NetWorkVolleyImpl";

    public static String DESCRIPTOR = "volley";

    static {
        VolleyWapper.getRequestQueue().start();
    }

    private Map<MgtvAbstractRequest, Request> mRequestCache = Collections.synchronizedMap(new HashMap<MgtvAbstractRequest, Request>());

    @Override
    public void execute(MgtvAbstractRequest request) {
        if (request == null) {
            return;
        }
        Request volleyRequest = transformRequest(request);
        if (volleyRequest == null) {
            return;
        }
        VolleyWapper.getRequestQueue().add(volleyRequest);
        mRequestCache.put(request, volleyRequest);
    }

    @Override
    public <V> Request transformRequest(final MgtvAbstractRequest<V> request) {
        if (request == null) {
            return null;
        }
        Request volleyRequest = null;
        if (request.getRequestMethod() == Request.Method.GET) {
            volleyRequest = buildGetRequest(request);
        } else if (request.getRequestMethod() == Request.Method.POST) {
            volleyRequest = buildPostRequest(request);
        }

        volleyRequest.setShouldCache(request.isCache());
        // NOTE(xianggengping):设置重试逻辑
        MgtvRetryPolicy retryPolicy = new MgtvRetryPolicy(volleyRequest);
        retryPolicy.setRetryDomains(request.getRetryDomains());
        volleyRequest.setRetryPolicy(retryPolicy);
        volleyRequest.setCachePeriod(request.getCachePeriod());
        volleyRequest.setCompleteCacheTime(request.getCompleteCacheTime());
        return volleyRequest;
    }

    /**
     * 构建get请求
     *
     * @param request
     * @param <V>
     * @return
     */
    private <V> Request buildGetRequest(final MgtvAbstractRequest<V> request) {
        final TaskCallback callback = request.getTaskCallback();
        String requestUrl = request.getRequestUrl();
        Log.i(TAG, "requestID:" + request.hashCode() + ",request method: GET ,requestUrl:" + requestUrl);
        StringRequest getRequest = new StringRequest(request.getRequestMethod(), requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "requestID:" + request.hashCode() + "result:" + response);
                if (callback != null) {
                    V result = request.parseData(response);
                    callback.onSuccess(result);
                }
                mRequestCache.remove(request);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "requestID:" + request.hashCode() + "error:" + error.getMessage());
                if (callback != null) {
                    // TODO 后续对错误返回结果进行处理
                    callback.onFailure(0, "", "");
                }
                mRequestCache.remove(request);
            }
        });
        return getRequest;
    }

    /**
     * 构建post请求
     *
     * @param request
     * @param <V>
     * @return
     */
    private <V> Request buildPostRequest(final MgtvAbstractRequest<V> request) {
        final TaskCallback callback = request.getTaskCallback();
        String requestUrl = request.getRequestUrl();
        String params = null;
        if (request.getParameter() != null) {
            params = request.getParameter().buildParameter();
        }
        Log.i(TAG, "requestID:" + request.hashCode() + ",request method: POST ,requestUrl:" + requestUrl + " , params:" + params);
        StringRequest getRequest = new StringRequest(request.getRequestMethod(), requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "requestID:" + request.hashCode() + "result:" + response);
                if (callback != null) {
                    V result = request.parseData(response);
                    callback.onSuccess(result);
                }
                mRequestCache.remove(request);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "requestID:" + request.hashCode() + "error:" + error.getMessage());
                if (callback != null) {
                    // TODO 后续对错误返回结果进行处理
                    callback.onFailure(0, "", "");
                }
                mRequestCache.remove(request);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return request.getParameter();
            }
        };
        return getRequest;
    }

    @Override
    public void stop(MgtvAbstractRequest request) {
        mRequestCache.remove(request);
    }
}
