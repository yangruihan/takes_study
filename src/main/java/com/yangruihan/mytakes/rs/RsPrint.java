package com.yangruihan.mytakes.rs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.regex.Pattern;

import com.yangruihan.mytakes.Response;
import com.yangruihan.mytakes.misc.Utf8OutputStreamWriter;
import com.yangruihan.mytakes.misc.Utf8String;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Response decorator that can print an entire response in HTTP format.
 * 
 * 可以以 HTTP 格式打印整个响应的响应装饰器
 *
 * <p>The class is immutable and thread-class
 * 
 * <p>这个类是不可变的且线程安全的
 * 
 * @author Yrh
 *
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class RsPrint extends RsWrap {
	
	/**
	 * Pattern for first line.
	 * 
	 * 第一行的模式
	 */
	private static final Pattern FIRST = Pattern.compile("HTTP/1\\.1 \\d{3} [a-zA-Z ]+");
	
	/**
	 * Pattern for all other lines in the head.
	 * 
	 * 头部（header）中其他行的模式
	 */
	private static final Pattern OTHERS = Pattern.compile("[a-zA-Z0-9\\-]+:\\p{Print}+");

	/**
	 * Ctor.
	 * @param res 原始响应（response）
	 */
	public RsPrint(final Response res) {
		super(res);
	}
	
	/**
	 * Print it into string.
	 * 
	 * 打印成一个字符串
	 * 
	 * @return
	 * @throws IOException
	 */
	public String print() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		this.print(baos);
		return new Utf8String(baos.toByteArray()).string();
	}
	
	/**
	 * Print body into string.
	 * 
	 * 打印身体（body）成一个字符串
	 * 
	 * @return
	 * @throws IOException
	 */
	public String printBody() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		this.printBody(baos);
		return new Utf8String(baos.toByteArray()).string();
	}
	
	/**
	 * Print head into string.
	 * 
	 * 打印头部（header）成一个字符串
	 * 
	 * @return
	 * @throws IOException
	 */
	public String printHead() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.printHead(baos);
        return new Utf8String(baos.toByteArray()).string();
	}
	
	/**
	 * Print it into output stream
	 * 
	 * 打印到输出流
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void print(final OutputStream output) throws IOException {
		this.printHead(output);
		this.printBody(output);
	}
	
	/**
	 * Print it into output stream.
	 * 
	 * 打印到输出流
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void printHead(final OutputStream output) throws IOException {
		final String eol = "\r\n"; // end of line
		final Writer writer = new Utf8OutputStreamWriter(output);
		int pos = 0;
		for (final String line : this.head()) {
			if (pos == 0 && !RsPrint.FIRST.matcher(line).matches()) {
				throw new IllegalArgumentException(
						String.format(
							"first line of HTTP response \"%s\" doesn't match \"%s\" regular expression, but it should, according to RFC 7230",
							line,
							RsPrint.FIRST
						)
				);
			}
			if (pos > 0 && !RsPrint.OTHERS.matcher(line).matches()) {
				throw new IllegalArgumentException(
						String.format(
							"header line #%d of HTTP response \"%s\" doesn't match \"%s\" regular expression, but it should, according to RFC 7230",
							pos + 1,
							line,
							RsPrint.OTHERS
						)
				);
			}
			writer.append(line);
			writer.append(eol);
			++pos;
		}
		writer.append(eol);
        writer.flush();
	}
	
	/**
	 * Print it into output stream.
	 * 
	 * 打印到输出流
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void printBody(final OutputStream output) throws IOException {
		final InputStream body = this.body();
		final byte[] buf = new byte[4096];
		while (true) {
			final int bytes = body.read(buf);
			if (bytes < 0) {
				break;
			}
			output.write(buf, 0, bytes);
		}
		output.flush();
	}
	
}
