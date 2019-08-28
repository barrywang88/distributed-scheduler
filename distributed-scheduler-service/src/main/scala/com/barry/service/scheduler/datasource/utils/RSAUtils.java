package com.barry.service.scheduler.datasource.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

/**
 * RSA算法加密/解密工具类
 *
 * @author huyj
 * @Created 2018-09-29 09:42
 */
public class RSAUtils {

    public static String publicKey;  //公钥
    public static String privateKey; //私钥

    /**
     * 生成公钥和私钥
     */
    public static void generateKey() {
        //1.初始化秘钥
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom sr = new SecureRandom();  //随机数生成器
            keyPairGenerator.initialize(512, sr);   //设置512位长的秘钥
            KeyPair keyPair = keyPairGenerator.generateKeyPair(); //开始创建
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            //进行转码
            //publicKey = Base64.encodeBase64String(rsaPublicKey.getEncoded());
            publicKey = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
            //进行转码
            // privateKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
            privateKey = Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 随机 生成公钥和私钥
     */
    public static void generateKeyRandom() {
        Map<String, Object> keyMap;
        try {
            keyMap = CreateSecrteKey.initKey();

            publicKey = Base64.getEncoder().encodeToString(CreateSecrteKey.getPublicKey(keyMap).getEncoded());
            //进行转码
            // privateKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
            privateKey = Base64.getEncoder().encodeToString(CreateSecrteKey.getPrivateKey(keyMap).getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 私钥匙加密或解密
     *
     * @param content
     * @param privateKeyStr
     * @return
     */
    public static String encryptByprivateKey(String content,
                                             String privateKeyStr, int opmode) {
        //私钥要用PKCS8进行处理
        //PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr));
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
        KeyFactory keyFactory;
        PrivateKey privateKey;
        Cipher cipher;
        byte[] result;
        String text = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            //还原Key对象
            privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(opmode, privateKey);
            if (opmode == Cipher.ENCRYPT_MODE) {  //加密
                result = cipher.doFinal(content.getBytes());
                //text = Base64.encodeBase64String(result);
                text = Base64.getEncoder().encodeToString(result);
            } else if (opmode == Cipher.DECRYPT_MODE) {  //解密
                //result = cipher.doFinal(Base64.decodeBase64(content));
                result = cipher.doFinal(Base64.getDecoder().decode(content));
                text = new String(result, "UTF-8");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 公钥匙加密或解密
     *
     * @param content
     * @param publicKeyStr
     * @return
     */
    public static String encryptByPublicKey(String content,
                                            String publicKeyStr, int opmode) {
        //公钥要用X509进行处理
        //X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
        KeyFactory keyFactory;
        PublicKey publicKey;
        Cipher cipher;
        byte[] result;
        String text = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            //还原Key对象
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(opmode, publicKey);
            if (opmode == Cipher.ENCRYPT_MODE) {  //加密
                result = cipher.doFinal(content.getBytes());
                //text = Base64.encodeBase64String(result);
                text = Base64.getEncoder().encodeToString(result);
            } else if (opmode == Cipher.DECRYPT_MODE) {  //解密
                //result = cipher.doFinal(Base64.decodeBase64(content));
                result = cipher.doFinal(Base64.getDecoder().decode(content));
                text = new String(result, "UTF-8");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }


    public static void buildDataBaseInfo(String username, String password) {
        RSAUtils.generateKey();
        //RSAUtils.generateKeyRandom();
        String publicKey = RSAUtils.publicKey;
        String privateKey = RSAUtils.privateKey;
        StringBuffer buffer = new StringBuffer();


        buffer.append("公钥请发给开发人员:").append("\n");
        buffer.append("publicKey:[").append(publicKey).append("]").append("\n");
        buffer.append("私钥请DBA妥善保管:").append("\n");
        buffer.append("privateKey:[").append(privateKey).append("]");
        buffer.append("\n\n");

        buffer.append("加密信息:").append("\n");
        buffer.append("用户名:[").append(username).append("]").append("\n");
        String user_encryp = RSAUtils.encryptByprivateKey(username, privateKey, Cipher.ENCRYPT_MODE);
        buffer.append("用户名私钥加密:[").append(user_encryp).append("]").append("\n");
        //buffer.append("用户名公钥解密:[").append(RSAUtils.encryptByPublicKey(user_encryp, publicKey, Cipher.DECRYPT_MODE)).append("]").append("\n");


        buffer.append("\n");
        buffer.append("密码:[").append(password).append("]").append("\n");
        String pass_encryp = RSAUtils.encryptByprivateKey(password, privateKey, Cipher.ENCRYPT_MODE);
        buffer.append("密码私钥加密:[").append(pass_encryp).append("]").append("\n");
        buffer.append("密码公钥解密:[").append(RSAUtils.encryptByPublicKey(pass_encryp, publicKey, Cipher.DECRYPT_MODE)).append("]").append("\n");


        System.out.println(buffer.toString());
    }


    public static void RsaDecryptTest(String ciphertext, String publicKey) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("加密信息:").append("\n");
        buffer.append("密文:[").append(ciphertext).append("]").append("\n");
        buffer.append("公钥解密:[").append(RSAUtils.encryptByPublicKey(ciphertext, publicKey, Cipher.DECRYPT_MODE)).append("]").append("\n");
        System.out.println(buffer.toString());
    }


    /**
     * 测试方法
     *
     * @param args
     */
    public static void main(String[] args) {

        buildDataBaseInfo("root", "today-36524");

        /*解密测试*//*
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIvJScGG6XLs7y1L9Fh9C7ShJx0eOHx+GnLoZUUFf1YrQf8+T29QapzZMtDK4SoJii98STXjBjCnS7LSIcRpdU8CAwEAAQ==";
        String userCiphertext = "hvaatZezNMgyrDEGl9D1nRhqgq1VpdAoG3W2l/UdakVHwwKgouu0o4/W7Wt13z3MZ+i+z4yCm97FR0m5UnD9jQ==";
        //String passCiphertext = "Olpx4XR3ftN5D4uUykmVOx+hWy3qOIzNEeBn3krRZ/HhdahXHZ38pqNUg3bAku8X0Mq49lS+BJmvuj0ptG1jPw==";
        try {
            //RsaDecryptTest(Base64.getEncoder().encodeToString(userCiphertext.getBytes("UTF-8")), publicKey);
            RsaDecryptTest(Base64.getEncoder().encodeToString(userCiphertext.getBytes("UTF-8")), publicKey);
            //RsaDecryptTest(new String(Base64.getDecoder().decode(passCiphertext), "UTF-8"), publicKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


        /**
         * 注意：
         *   私钥加密必须公钥解密
         *   公钥加密必须私钥解密
         */
       /* System.out.println("-------------生成两对秘钥，分别发送方和接收方保管-------------");
        RSAUtils.generateKey();
        //RSAUtils.generateKeyRandom();
        System.out.println("公钥匙给接收方:" + RSAUtils.publicKey);
        System.out.println("私钥给发送方:" + RSAUtils.privateKey);

        System.out.println("-------------第一个栗子，私钥加密公钥解密-------------");
        String textsr = "早啊，你吃早饭了吗？O(∩_∩)O~";
        //私钥加密
        String cipherText = RSAUtils.encryptByprivateKey(textsr, RSAUtils.privateKey, Cipher.ENCRYPT_MODE);
        System.out.println("发送方用私钥加密后：" + cipherText);
        //公钥解密
        String text = RSAUtils.encryptByPublicKey(cipherText,
                RSAUtils.publicKey, Cipher.DECRYPT_MODE);
        System.out.println("接收方用公钥解密后：" + text);

        System.out.println("-------------第二个栗子，公钥加密私钥解密-------------");
        //公钥加密
        textsr = "吃过啦！你吃了吗？O(∩_∩)O~";
        cipherText = RSAUtils.encryptByPublicKey(textsr,
                RSAUtils.publicKey, Cipher.ENCRYPT_MODE);
        System.out.println("接收方用公钥加密后：" + cipherText);
        //公钥解密
        text = RSAUtils.encryptByprivateKey(cipherText,
                RSAUtils.privateKey, Cipher.DECRYPT_MODE);
        System.out.print("发送方用私钥解密后：" + text);*/

    }
}

