package com.npx.admin.utils;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;

public class PkiUtils {
	public static String keyStoreName;
	public static String keyStoreType;
	public static char[] keyStorePassword;
	public static String keystoreAlias;
	public static char[] keyPass;
	public static String signAlg;

	static{
		load();
	}
	public PkiUtils() {

	}
	private static synchronized void load()
	{
		String path = PkiUtils.class.getClassLoader().getResource("//").getPath();
		File file = new File(path).getParentFile().getParentFile();
		String ketSoreNamePath = file.getPath() + "\\pfx\\clientkeystorename.pfx";
		
		keyStoreName = ketSoreNamePath;//"e://test.pfx";//PropertiesUtil.getProperties("jchrpt.properties").getProperty("keyStoreName");
		keyStoreType = "PKCS12";
		keyStorePassword = "123456".toCharArray();
		keystoreAlias = "";
		keyPass ="123456".toCharArray();	
		signAlg = "SHA256withRSA";
	}
	
	public static String sign(String plaintext) throws Exception {	
		KeyStore ks = KeyStore.getInstance(keyStoreType);
		FileInputStream fis = new FileInputStream(keyStoreName);
		ks.load(fis, keyStorePassword);
		fis.close();
		PrivateKey priv = (PrivateKey) ks.getKey(getKeyAlias(ks), keyPass);
		Signature rsa = Signature.getInstance(signAlg);
		rsa.initSign(priv);
		rsa.update(plaintext.getBytes("UTF-8"));
		byte[] sig = rsa.sign();
		System.out.println("签名方法："+signAlg);
		return new String(Base64.encodeBase64(sig)).replaceAll(" ", "");
	}
	
	public static String getKeyAlias(KeyStore keyStore) {
		  String keyAlias = "";
		  try {
		   Enumeration<String> enums = keyStore.aliases();
		   while (enums.hasMoreElements()) {
		    keyAlias = (String) enums.nextElement();
		   }
		  } catch (KeyStoreException e) {
		   e.printStackTrace();
		  }
		  System.out.println(keyAlias);
		  return keyAlias;
		 }
	
	public static Certificate getKeyStroeCert() throws Exception {		
		KeyStore ks = KeyStore.getInstance(keyStoreType);
		FileInputStream fis = new FileInputStream(keyStoreName);
		ks.load(fis, keyStorePassword);
		fis.close();
		return ks.getCertificate(getKeyAlias(ks));		
	}
	
	public static boolean checkSign(String data,String sign) throws Exception {
		Certificate cert = PkiUtils.getKeyStroeCert();
		byte[] publicKey = cert.getPublicKey().getEncoded();
		return doCheckSHA256(data, sign, publicKey);
	}
	
	public static void checkSign1(String data,String sign) throws Exception {
		Certificate cert = PkiUtils.getKeyStroeCert();
		byte[] publicKey = cert.getPublicKey().getEncoded();
		System.out.println("SHA1withRSA 验签结果"+doCheckSHA1(data, sign, publicKey));
	}
	
	public static boolean doCheckSHA1(String content, String sign, byte[] publicKey){  
        try{  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));  
            java.security.Signature signature = java.security.Signature.getInstance("SHA1withRSA");  
            signature.initVerify(pubKey);  
            signature.update( content.getBytes("UTF-8") );  
            boolean bverify = signature.verify(org.bouncycastle.util.encoders.Base64.decode(sign));  
            return bverify;  
        }catch (Exception e){  
            //e.printStackTrace();  
        }  
        return false;  
    }  
	
	public static boolean doCheckSHA256(String content, String sign, byte[] publicKey){  
        try{  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));  
            java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA");  
            signature.initVerify(pubKey);  
            signature.update( content.getBytes("UTF-8") );  
            boolean bverify = signature.verify(org.bouncycastle.util.encoders.Base64.decode(sign));  
            return bverify;  
        }catch (Exception e){  
            e.printStackTrace();  
        }  
        return false;  
    }  
	
	public static void doTest() throws Exception{
		String p = "123456";
		String sign = PkiUtils.sign(p);
		System.out.println(PkiUtils.sign(p));
		System.out.println("签名值："+sign);
		boolean checkSign = PkiUtils.checkSign(p,sign);
		//PkiUtils.checkSign1(p, sign);
		System.out.println(checkSign);
	}
	
	public static void main(String[] args) throws Exception {
		doTest();
	}
	
	
}
