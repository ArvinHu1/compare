package com.statestr.arvin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	protected static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	protected static MessageDigest messageDigest = null;

	static {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			System.out.println(MD5Util.class.getName()+"初始化失败");
			nsaex.printStackTrace();
		}
	}

	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}
	
	public static String getMD5String(byte[] bytes) {
		messageDigest.update(bytes);
		return bufferToHex(messageDigest.digest());
	}
	
	public static String bufferToHex(byte[] bytes) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte[] bytes, int i, int length) {
		StringBuffer sb = new StringBuffer(length * 2);
		int k = i + length;
		for(int l = i; l < k; l++) {
			appendHexPair(bytes[l], sb);
		}
		return sb.toString();
	}

	private static void appendHexPair(byte bytes, StringBuffer sb) {
		char c0 = hexDigits[(bytes & 0xf0) >> 4];
		char c1 = hexDigits[bytes & 0xf];
		sb.append(c0);
		sb.append(c1);
	}
	
	public static String getFileMD5String(File file) throws IOException {
		InputStream fis;
		fis = new FileInputStream(file);
		byte[] bt = new byte[1024];
		int readNum;
		while ((readNum = fis.read(bt)) > 0) {
			messageDigest.update(bt, 0, readNum);
		}
		fis.close();
		return bufferToHex(messageDigest.digest());
	}


	public static void main(String[] args) throws IOException {
		System.out.println(MD5Util.getMD5String("123456"));
		
//		System.out.println(MD5Util.getFileMD5String(new File("C:\\MIP\\branches\\develop version\\MIP-GUI\\WEB-INF\\cfg\\settings.xml")));
	}
}
