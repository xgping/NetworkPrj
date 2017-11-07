package com.xgp.lib.network;

import android.text.TextUtils;

/**
 * description: 网络工具的工厂类，用于之后网络框架切换的解耦
 * <p>
 * author: Created by xianggengping on 2017/10/24.
 * <p>
 * email: xianggengping@163.com
 */

public class NetWorkFactory {
    private static final String NET_WORK_TYPE = "volley";

    /**
     * 创建具体网络请求实现类
     *
     * @return
     */
    public static INetWorkInterface newNetworkImpl() {
        if (TextUtils.isEmpty(NET_WORK_TYPE)) {
            return null;
        }
        if (NET_WORK_TYPE.equals(NetWorkVolleyImpl.DESCRIPTOR)) {
            return new NetWorkVolleyImpl();
        }
        return null;
    }
}
