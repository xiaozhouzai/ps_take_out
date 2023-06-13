package com.lcy.reggie.utils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class RSAUtils {


        /**
         * 生成 RSA 密钥对
         *
         * @param keySize 密钥长度(单位：位)
         * @return 公钥和私钥
         */

        public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            keyPairGenerator.initialize(keySize);

            return keyPairGenerator.generateKeyPair();

        }


        /**
         * 使用公钥加密消息并返回密文
         *
         * @param publicKey 公钥
         * @param message   消息
         * @return 密文
         */

        public static byte[] encryptWithPublicKey(PublicKey publicKey, byte[] message) throws Exception {

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return cipher.doFinal(message);

        }


        /**
         * 使用私钥解密消息并返回明文
         *
         * @param privateKey 私钥
         * @param cipherText 密文
         * @return 明文
         */

        public static byte[] decryptWithPrivateKey(PrivateKey privateKey, byte[] cipherText) throws Exception {

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            return cipher.doFinal(cipherText);

        }


        /**
         * 对消息进行签名并返回数字签名(Base64 编码后的字符串)
         *
         * @param privateKey 私钥
         * @param message    消息
         * @return 数字签名(Base64 编码后的字符串)
         */

        public static String signWithPrivateKey(PrivateKey privateKey, byte[] message) throws Exception {

            Signature signature = Signature.getInstance("SHA256withRSA");

            signature.initSign(privateKey);

            signature.update(message);

            byte[] signedBytes = signature.sign();

            return Base64.getEncoder().encodeToString(signedBytes);

        }


        /**
         * 对数字签名进行验证并返回验证结果(true:验证成功；false:验证失败)
         *
         * @param publicKey          公钥
         * @param message            消息(未加密的原始数据)
         * @param signatureBase64Str 数字签名(Base64 编码后的字符串)
         * @return 验证结果(true : 验证成功 ； false : 验证失败)
         */

        public static boolean verifyWithPublicKey(PublicKey publicKey, byte[] message, String signatureBase64Str) throws Exception {

            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64Str); // 先将 Base64 编码的数字签名解码成字节数组形式
            Signature signature = Signature.getInstance("SHA256withRSA"); // 创建 SHA256withRSA 签名实例

            signature.initVerify(publicKey); // 初始化签名实例以进行验证

            signature.update(message); // 更新签名实例以使用新的消息数据

            return signature.verify(signatureBytes); // 验证数字签名是否有效
        }
}
