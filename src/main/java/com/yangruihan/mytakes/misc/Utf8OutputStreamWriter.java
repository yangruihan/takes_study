package com.yangruihan.mytakes.misc;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * OutputStreamWriter that uses UTF-8 encoding for all operations.
 * 
 * OutputStreamWriter 对于所有操作使用 UTF-8 编码
 * 
 * @author Yrh
 *
 */
public class Utf8OutputStreamWriter extends OutputStreamWriter {

	/**
	 * Ctor.
	 * @param output 输出流值
	 */
	public Utf8OutputStreamWriter(final OutputStream output) {
		super(output, StandardCharsets.UTF_8);
	}
}
