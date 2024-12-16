package com.zcc.highmyopia.hospital.utils;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author zcc
 * @Date 2024/11/18
 * @Description
 */
public class Sign {

    private static Map<String, String> initialBasicHeader(String method, String path,
                                                          Map<String, String> headers,
                                                          Map<String, String> querys,
                                                          Map<String, String> bodys,
                                                          List<String> signHeaderPrefixList,
                                                          String appKey, String appSecret, String hostId)
            throws MalformedURLException {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        //headers.put(SystemHeader.X_CA_TIMESTAMP, String.valueOf(System.currentTimeMillis()-5*1000*60));
        headers.put(SystemHeader.X_CA_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        headers.put(SystemHeader.X_CA_NONCE, UUID.randomUUID().toString());
        headers.put(SystemHeader.X_CA_KEY, appKey);
        headers.put(SystemHeader.X_CA_VERSION,"001");
        headers.put(SystemHeader.X_CA_HOSTID, hostId);
        headers.put(SystemHeader.X_CA_SIGNATURE,
                SignUtil.sign(appSecret, method, path, headers, querys, bodys, signHeaderPrefixList));

        return headers;
    }
}
