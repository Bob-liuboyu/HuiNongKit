package com.project.arch_repo.utils.security;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Fanjb
 * @name AES加解密工具类(不用SecurityRandom)使用Base64编码与解码
 * @date 2014-2-11
 */
public class AESUtilsWithEncode {
    /**
     * 算法提供商
     */
    private static final String PROVIDER = "BC";

    /**
     * 算法名称
     */
    private static final String ALGORITHM = "AES";

    /**
     * 算法名称/填充模式/填充方式
     */
    private static final String TRANSFORMATION = "AES";

    /**
     * 编码方式
     */
    private static final String CHARSET = "utf-8";

    /**
     * 生成密钥
     *
     * @return 128bit/8 = 16byte --> 16进制数共32个字符
     */
    public static String generAESKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM, PROVIDER);
            kg.init(128);
            SecretKey sk = kg.generateKey();
            return new String(sk.getEncoded(), CHARSET);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String encrypt(String key, String data) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CHARSET), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec/*, new IvParameterSpec("0123456789abcdef".getBytes())*/);
            byte[] encrypted = cipher.doFinal(data.getBytes(CHARSET));
            return Base64.encode(encrypted);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } /*catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }*/
        return null;
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String key, String data) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CHARSET), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec/*, new IvParameterSpec("0123456789abcdef".getBytes())
            */);
            byte[] original = cipher.doFinal(Base64.decode(data));
            return new String(original, CHARSET);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } /*catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }*/
        return null;
    }


    public static void main(String[] args) {

        String password = "C59C64DCEB78709463E7157A8A022005";
        password = "0123456789abcdef";
        //password = generAESKey();
        String content = "你好abc我 d1成了23好4efgh—_i776556jkl你好mno@()*$#pqrst08-uvwxyz";
        //content = "abc1232@";
        content = "ABC";
        String encryptResult = encrypt(password, content);
        //encryptResult = "HSWCHD4xHuotTdhjOiXBtQ==";
        System.out.println("密钥：" + password);
        System.out.println("明文：" + content);
        System.out.println("密文：" + encryptResult);

        String decryptResult = decrypt(password, encryptResult);
        System.out.println("解密：" + decryptResult);
    }
}
