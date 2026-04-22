package com.liuheng.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.util.Base64;

public class WeChatUtil {

    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 调用微信code2Session接口获取openid和session_key
     * @param appId 小程序appId
     * @param appSecret 小程序appSecret
     * @param jsCode 微信登录code
     * @return JSON字符串包含openid和session_key
     */
    public static String code2Session(String appId, String appSecret, String jsCode) {
        try {
            String url = WX_LOGIN_URL + "?appid=" + URLEncoder.encode(appId, StandardCharsets.UTF_8)
                    + "&secret=" + URLEncoder.encode(appSecret, StandardCharsets.UTF_8)
                    + "&js_code=" + URLEncoder.encode(jsCode, StandardCharsets.UTF_8)
                    + "&grant_type=authorization_code";

            URL uri = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } finally {
                conn.disconnect();
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException("code2Session失败: " + e.getMessage());
        }
    }

    /**
     * 解密微信手机号
     * @param sessionKey session_key
     * @param encryptedData 加密数据
     * @param iv 初始向量
     * @return 解密后的JSON字符串
     */
    public static String decryptPhoneNumber(String sessionKey, String encryptedData, String iv) {
        try {
            byte[] keyBytes = sessionKey.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
            byte[] ivBytes = Base64.getDecoder().decode(iv);

            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(ivBytes));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), params);

            byte[] decrypted = cipher.doFinal(encryptedDataBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (java.security.NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | java.security.spec.InvalidParameterSpecException | java.security.InvalidKeyException | java.security.InvalidAlgorithmParameterException e) {
            throw new RuntimeException("手机号解密失败: " + e.getMessage());
        }
    }
}
