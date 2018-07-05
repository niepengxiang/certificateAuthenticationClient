package com.g4b.swing.client.utils;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;

/**
 * @ClassName: RSAUtils  
 * @Description: TODO RSA公钥/私钥/签名工具包
 * @author NiePengXiang
 * @date 2018年7月5日  
 *
 */
public class RSAUtils {

	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * @Title: genKeyPair  
	 * @Description: TODO 生成密钥对(公钥和私钥)
	 * @return Map<String,Object>   
	 * @throws Exception
	 */
	public static Map<String, Object> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	
	/**
	 * @Title: sign  
	 * @Description: TODO 用私钥对信息生成数字签名
	 * @param @param data 		已加密数据
	 * @param @param privateKey 私钥
	 * @return String   
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey.getBytes());
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return Hex.encodeHexString(signature.sign());
	}

	
	/**
	 * @Title: verify  
	 * @Description: TODO 校验数字签名
	 * @param @param data 已加密数据
	 * @param @param publicKey  公钥
	 * @param @param sign  数字签名
	 * @return boolean    
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getBytes());
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(sign.getBytes());
	}

	
	/**
	 * @Title: decryptByPrivateKey  
	 * @Description: TODO 私钥解密
	 * @param  encryptedData 	已加密数据
	 * @param  privateKey 		私钥
	 * @return byte[]   
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey.getBytes());
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = (PrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/**
	 * @Title: decryptByPublicKey  
	 * @Description: TODO 公钥解密
	 * @param  encryptedData 	已加密数据
	 * @param  publicKey		公钥
	 * @return byte[]     
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey.getBytes());
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	
	/**
	 * @Title: encryptByPublicKey  
	 * @Description: TODO  公钥加密
	 * @param  data 		源数据
	 * @param  publicKey 	公钥
	 * @return byte[]    	返回类型  
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey.getBytes());
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	
	/**
	 * @Title: encryptByPrivateKey  
	 * @Description: TODO 	私钥加密
	 * @param  data 		源数据
	 * @param  privateKey 	私钥
	 * @return byte[]   
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey.getBytes());
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/** *//**
     * <p>
     * 
     * </p>
     *
     * @param keyMap 
     * @return
     * @throws Exception
     */
	/**
	 * @Title: getPrivateKey  
	 * @Description: TODO 获取私钥
	 * @param @param keyMap 密钥对
	 * @return String   
	 * @throws Exception
	 */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return new String(key.getEncoded());
    }

	
    /**
     * @Title: getPublicKey  
     * @Description: TODO 获取公钥
     * @param @param keyMap 密钥对
     * @return String   
     * @throws Exception
     */
	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return new String(key.getEncoded());
	}
}
