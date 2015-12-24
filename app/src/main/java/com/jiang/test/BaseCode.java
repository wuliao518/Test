package com.jiang.test;

import com.tencent.upload.utils.Base64;

/**
 * Created by wuliao on 15/12/24.
 */
public class BaseCode {

    public static String encode(byte[] data) {
        return new String(Base64.encode(data, Base64.DEFAULT));
    }
}
