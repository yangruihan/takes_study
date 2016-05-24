package com.yangruihan.mytakes.misc;

import java.nio.charset.Charset;

/**
 * String that uses UTF-8 encoding for all byte operations.
 * 
 * 对所有字节操作使用 UTF-8 编码的字符串
 * 
 * @author Yrh
 *
 */
public final class Utf8String {

	/**
	 * UTF-8 encoding.
	 */
	private static final String ENCODING = "UTF-8";
	
	/**
	 * String value.
	 * 
	 * 字符串值
	 */
	private final transient String value;
	
	/**
	 * Ctor.
	 * @param string
	 */
	public Utf8String(final String string) {
		this.value = string;
	}
	
	/**
	 * Ctor.
	 * @param bytes
	 */
	public Utf8String(final byte... bytes) {
		this(new String(bytes, Charset.forName(Utf8String.ENCODING)));
	}
	
	/**
	 * Encodes string value into a sequence of bytes using UTF-8 charset.
	 * 
	 * 将字符串值编码为使用 UTF-8 的字节序列
	 * 
	 * @return
	 */
	public byte[] bytes() {
		return this.value.getBytes(Charset.forName(Utf8String.ENCODING));
	}
	
	/**
	 * Returns string value.
	 * 
	 * 返回字符串值
	 * 
	 * @return
	 */
	public String string() {
		return this.value;
	}
}
