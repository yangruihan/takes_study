package com.yangruihan.mytakes.rs;

import java.io.InputStream;
import java.net.URL;

import com.yangruihan.mytakes.Response;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Plain text response decorator
 * 
 * 纯文本响应装饰器
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
public final class RsText extends RsWrap {

	/**
	 * Ctor.
	 */
	public RsText() {
		this("");
	}
	
	/**
	 * Ctor.
	 * @param body 纯文本身体（body）
	 */
	public RsText(final CharSequence body) {
		this(new RsEmpty(), body);
	}
	
	/**
	 * Ctor.
	 * @param body 纯文本身体（body）
	 */
	public RsText(final byte[] body) {
		this(new RsEmpty(), body);
	}
	
	/**
	 * Ctor.
	 * @param body 纯文本身体（body）
	 */
	public RsText(final InputStream body) {
		this(new RsEmpty(), body);
	}
	
	/**
	 * Ctor.
	 * @param body 纯文本身体（body）
	 */
	public RsText(final URL url) {
		this(new RsEmpty(), url);
	}
	
	/**
	 * Ctor.
	 * @param res 原始响应
	 * @param body HTML身体（body）
	 */
	public RsText(final Response res, final CharSequence body) {
		this(new RsWithBody(res, body));
	}
	
	/**
	 * Ctor.
	 * @param res 原始响应
	 * @param body HTML身体（body）
	 */
	public RsText(final Response res, final byte[] body) {
		this(new RsWithBody(res, body));
	}
	
	/**
	 * Ctor.
	 * @param res 原始响应
	 * @param body HTML身体（body）
	 */
	public RsText(final Response res, final InputStream body) {
		this(new RsWithBody(res, new Body.TempFile(new Body.Stream(body))));
	}
	
	/**
	 * Ctor.
	 * @param res 原始响应
	 * @param body HTML身体（body）
	 */
	public RsText(final Response res, final URL url) {
		this(new RsWithBody(res, url));
	}
	
	/**
	 * Ctor.
	 * @param res 原始响应
	 */
	public RsText(final Response res) {
		super(new RsWithType(res, "text/plain"));
	}
}
