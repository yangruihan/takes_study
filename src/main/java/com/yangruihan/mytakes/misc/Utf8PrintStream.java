package com.yangruihan.mytakes.misc;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * PrintStream that uses UTF-8 encoding for all operations.
 * 
 * PrintStream 对于所有操作使用 UTF-8 编码
 * 
 * @author Yrh
 *
 */
public class Utf8PrintStream extends PrintStream {

	/**
	 * Ctor.
	 * @param output 将打印到的输出流
	 * @param flush 是否自动flush
	 * @throws UnsupportedEncodingException
	 */
	public Utf8PrintStream(final OutputStream output, final boolean flush) throws UnsupportedEncodingException {
		super(output, flush, StandardCharsets.UTF_8.toString());
	}
}
