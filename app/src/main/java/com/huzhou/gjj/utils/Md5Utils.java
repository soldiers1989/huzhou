package com.huzhou.gjj.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 加密工具类<br>
 * MD5加密出来的长度是32位<br>
 * SHA加密出来的长度是40位<br>
 * AES加密<br>
 * Tip：支持JDK1.6，在JAVA_HOME/jre/lib/security目录下更新两个包 local_policy.jar
 * US_export_policy.jar 用于避开美国加密算法出口软件限制 AES 256位加密
 */
public class Md5Utils {

    /**
     * 二次加密，MD5 And SHA
     *
     * @param inputText
     * @return
     */

    public static String md5(String inputText) {
        return encrypt(inputText, "md5");
    }

    /**
     * SHA加密
     *
     * @param inputText
     * @return
     */


    private static String encrypt(String inputText, String algorithmName) {
        if (inputText == null || "".equals(inputText.trim())) {
            throw new IllegalArgumentException("请确认密码为空，且非由空格组成！");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = "md5";
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes("UTF8"));
            byte s[] = m.digest();
            // m.digest(inputText.getBytes("UTF8"));
            return hex(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptText;
    }

    /**
     * 返回十六进制字符串
     *
     * @param arr
     * @return
     */
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

}
