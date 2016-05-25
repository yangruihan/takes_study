package com.yangruihan.mytakes.rs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import com.yangruihan.mytakes.Response;
import com.yangruihan.mytakes.misc.Utf8String;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Response decorator, with body.
 * 
 * 带响应体（body）的响应装饰器
 * 
 * <p>This implementation of {@link Response} interface requires that
 * the {@link Response#head()} method has to be invoked before reading
 * from the {@code InputStream} obtained from {@link Response#body()}
 * method.
 * 
 * <p>这个{@link Response}接口的实现要求{@link Response#head()}方法必须在
 * 从{@link Response#body()}包含的{@code InputStream}中读取数据之前调用
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
public final class RsWithBody extends RsWrap {

	/**
	 * Constructs a {@code RsWithBody} with the specified body that will be
	 * encoded into UTF-8 by default
	 * 
	 * 构造一个{@code RsWithBody}带有的特殊响应体（body）会被默认编码为 UTF-8
	 * 
	 * @param body
	 */
	public RsWithBody(final CharSequence body) {
		this(new RsEmpty(), body);
	}
	
	/**
	 * Constructs a {@code RsWithBody} with the specified body
	 * 
	 * 构造一个{@code RsWithBody}带有的特殊响应体（body）的响应
	 * 
	 * @param body 响应体
	 */
	public RsWithBody(final byte[] body) {
		this(new RsEmpty(), body);
	}
	
	/**
	 * Constructs a {@code RsWithBody} with the specified body
	 * 
	 * 构造一个{@code RsWithBody}带有的特殊响应体（body）的响应
	 * 
	 * @param body 响应体
	 */
	public RsWithBody(final InputStream body) {
		this(new RsEmpty(), body);
	}
	
	/**
	 * Constructs a {@code RsWithBody} with the content located at the specified
	 * url as body
	 * 
	 * 构造一个带有位于特殊url里内容的响应
	 * 
	 * @param url
	 */
	public RsWithBody(final URL url) {
		this(new RsEmpty(), url);
	}
	
	/**
	 * Constructs a {@code RsWithBody} with the specified response and body. The
	 * body will be encoded into UTF-8 by default.
	 * 
	 * 构造一个带有特殊响应和响应体的响应，响应体默认使用编码 UTF-8
	 * 
	 * @param res 原始响应
	 * @param body 响应体
	 */
	public RsWithBody(final Response res, final CharSequence body) {
		this(res, new Utf8String(body.toString()).bytes());
	}
	
	/**
	 * Constructs a {@code RsWithBody} with the specified response and body. The
	 * body will be encoded using the specified character set.
	 * 
	 * 构造一个带有特殊响应和响应体的响应，响应体使用特殊的指定编码集
	 * 
	 * @param res 原始响应
	 * @param body 响应体
	 * @param charset 编码集
	 */
	public RsWithBody(final Response res, final CharSequence body, final Charset charset) {
		this(res, body.toString().getBytes(charset));
	}
	
	/**
	 * Ctor.
	 * @param res 原始响应
	 * @param url Url
	 */
	public RsWithBody(final Response res, final URL url) {
		this(res, new Body.URL(url));
	}
	
	/**
	 * Ctor.
	 * @param res 原始响应
	 * @param body 响应体
	 */
	public RsWithBody(final Response res, final byte[] body) {
		this(res, new Body.ByteArray(body));
	}
	
	/**
	 * Ctor.
	 * @param res 原始响应
	 * @param body 响应体
	 */
	public RsWithBody(final Response res, final InputStream body) {
		this(res, new Body.Stream(body));
	}
	
	/**
	 * Constructs a {@code RsWithBody} with the specified response and body
	 * content.
	 * 
	 * 构造一个{@code RsWithBody}带有的特殊响应及响应体（body）内容的响应
	 * 
	 * @param res
	 * @param body
	 */
	public RsWithBody(final Response res, final Body body) {
		super(
			new Response() {
				
				@Override
				public Iterable<String> head() throws IOException {
					return RsWithBody.append(res, body.length());
				}
				
				@Override
				public InputStream body() throws IOException {
					return body.input();
				}
			}
		);
	}
	
	/**
	 * Appends content length to header from response.
	 * 
	 * 添加内容长度到响应中的响应头
	 * 
	 * @param res 响应
	 * @param length 长度
	 * @return
	 * @throws IOException
	 */
	private static Iterable<String> append(final Response res, final int length) throws IOException {
		final String header = "Content-Length";
		return new RsWithHeader(
					new RsWithoutHeader(res, header),
					header,
					Integer.toString(length)
				).head();
	}
	
}
