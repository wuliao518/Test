package com.jiang.test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
/**
 * Created by wuliao on 15/12/24.
 */
public class HMACSHA1 {
    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * 生成签名数据
     *
     * @param data 待加密的数据
     * @param key  加密使用的key
     * @return 生成MD5编码的字符串
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getSignature(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        return mac.doFinal(data);
    }
}
