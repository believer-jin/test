package cn.entersoft.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * 加密解密辅助类
 * 使用commons-codec.jar加密解密（可以使用MD5，SHA1，BASE64加密解密）
 * @author dingsj
 *2013-12-10
 *Base64Util
 */
public class EncryptionUtils {
	
	/**
	 * MD5加密
	 * @param data
	 * @return String
	 * @author dingsj
	 * @since 2013-12-10 下午02:47:52
	 */
	public static String encryptionMD5(String data){
		return DigestUtils.md5Hex(data);
	}
	
	/**
	 * SHA1加密
	 * @param data
	 * @return String
	 * @author dingsj
	 * @since 2013-12-10 下午02:49:16
	 */
	public static String encryptiionSHA1(String data){
		return DigestUtils.shaHex(data);
	}
	
	/**
	 * BASE64加密
	 * @param data
	 * @return String
	 * @author dingsj
	 * @since 2013-12-10 下午02:51:22
	 */
	public static String encryptionBASE64(String data){
		return new String(Base64.encodeBase64(data.getBytes(),true));
	}
	
	/**
	 * BASE64解密
	 * @param data
	 * @return String
	 * @author dingsj
	 * @since 2013-12-10 下午02:58:21
	 */
	public static String DecryptionBASE64(String data){
		return  new String(Base64.decodeBase64(data.getBytes()));
	}
	
}
