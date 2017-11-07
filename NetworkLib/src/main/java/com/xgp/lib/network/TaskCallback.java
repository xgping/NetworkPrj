package com.xgp.lib.network;

/**
 * description:请求结果返回的回调接口
 * <p>
 * author: Created by xianggengping on 2017/10/24.
 * <p>
 * email: gengping@mgtv.com
 */

public interface TaskCallback<V> {

    /**
     * 请求成功回调
     * @param result
     *            请求返回的数据
     */
    void onSuccess(V result);

    /**
     * 请求失败回调，当请求发生超时等问题时的回调
     * @param code
     *            错误码
     * @param msg
     *            错误信息
     * @param errorCode
     *            错误码
     */
    void onFailure(int code, String msg, String errorCode);
}
