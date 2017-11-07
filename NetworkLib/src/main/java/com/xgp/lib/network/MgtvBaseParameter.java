package com.xgp.lib.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * description: 所有的请求参数parameter类都应该以这个类为基类
 * <p>
 * author: Created by xianggengping on 2017/10/24.
 * <p>
 * email: xianggengping@163.com
 */

public class MgtvBaseParameter extends HashMap<String, String> {

    public MgtvBaseParameter combineParams() {
        // TODO 添加通用参数
        return this;
    }

    public String put(String key, Object obj) {
        if (obj != null) {
            return put(key, obj.toString());
        }
        return "";
    }

    String buildParameter() {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, String>> iter = entrySet().iterator();
        while (iter.hasNext()) {
            try {
                Map.Entry<String, String> entry = iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val == null) {
                    val = "";
                }
                sb.append(key.toString());
                sb.append("=");
                if (val != null) {
                    sb.append(URLEncoder.encode(val.toString(), "UTF-8"));
                }
                if (iter.hasNext()) {
                    sb.append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
