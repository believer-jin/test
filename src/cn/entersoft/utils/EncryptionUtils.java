package cn.entersoft.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * ���ܽ��ܸ�����
 * ʹ��commons-codec.jar���ܽ��ܣ�����ʹ��MD5��SHA1��BASE64���ܽ��ܣ�
 * @author dingsj
 *2013-12-10
 *Base64Util
 */
public class EncryptionUtils {
	
	/**
	 * MD5����
	 * @param data
	 * @return String
	 * @author dingsj
	 * @since 2013-12-10 ����02:47:52
	 */
	public static String encryptionMD5(String data){
		return DigestUtils.md5Hex(data);
	}
	
	/**
	 * SHA1����
	 * @param data
	 * @return String
	 * @author dingsj
	 * @since 2013-12-10 ����02:49:16
	 */
	public static String encryptiionSHA1(String data){
		return DigestUtils.shaHex(data);
	}
	
	/**
	 * BASE64����
	 * @param data
	 * @return String
	 * @author dingsj
	 * @since 2013-12-10 ����02:51:22
	 */
	public static String encryptionBASE64(String data){
		return new String(Base64.encodeBase64(data.getBytes(),true));
	}
	
	/**
	 * BASE64����
	 * @param data
	 * @return String
	 * @author dingsj
	 * @since 2013-12-10 ����02:58:21
	 */
	public static String DecryptionBASE64(String data){
		return  new String(Base64.decodeBase64(data.getBytes()));
	}
	
}
