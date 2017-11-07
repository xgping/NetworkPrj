package com.xgp.lib.network;

/**
 * description:网络请求的接口类，子类进行具体网络的实现逻辑
 * <p>
 * author: Created by xianggengping on 2017/10/24.
 * <p>
 * email: xianggengping@163.com
 */

public interface INetWorkInterface<T> {
    /**
     * 将MgAbstractRequest转化成为具体框架所需要的request
     * @param request
     *            MgAbstractRequest对象
     * @param <V>
     *            请求完成之后，数据应该解析的类型
     * @return 框架对应的request对象
     */
    <V> T transformRequest(MgtvAbstractRequest<V> request);

    /**
     * 开始发出网络请求
     * @param request
     */
    void execute(MgtvAbstractRequest request);

    /**
     * 停止网络请求
     * @param request
     */
    void stop(MgtvAbstractRequest request);
}
