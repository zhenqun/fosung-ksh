package com.fosung.ksh.common.util;

import com.fosung.framework.common.exception.AppException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author toquery
 * @version 1
 */
public class UtilDT {

    /**
     * 获取真正的字符串值,将字符串null置为真正的null
     *
     * @param username
     * @param id
     * @return
     */
    public static String getUserNameIDHash(String key) {
        return sha1IdAndName(key);
    }
    /**
     * 获取真正的字符串值,将字符串null置为真正的null
     *
     * @param username
     * @param id
     * @return
     */
    public static String getUserNameIDHash(String username, String id) {
        StringBuffer userInfo = new StringBuffer();
        userInfo.append(id).append(username);
        return sha1IdAndName(userInfo.toString());
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     */
    public static String sha1IdAndName(String input) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            byte[] result = digest.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new AppException("编码 user hash 出错！");
        }
    }

    public static void main(String[] args) {
        String s = UtilDT.getUserNameIDHash("玄国晓","370725199010032592");
        System.out.println(s);
    }
}
