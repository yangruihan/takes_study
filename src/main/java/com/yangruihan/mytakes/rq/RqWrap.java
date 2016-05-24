package com.yangruihan.mytakes.rq;

import java.io.IOException;
import java.io.InputStream;

import com.yangruihan.mytakes.Request;

import lombok.EqualsAndHashCode;

/**
 * Request wrap.
 * 
 * 请求（request）包装类
 * 
 * <p>The class is immutable and thread-class
 * 
 * <p>这个类是不可变的且线程安全的
 * 
 * @author Yrh
 *
 */
@EqualsAndHashCode(of = "origin")
public class RqWrap implements Request {
	
	/**
	 * Original request.
	 * 原始请求（request）
	 */
	private final transient Request origin;
	
	/**
	 * Ctor.
	 * @param req
	 */
	public RqWrap(final Request req) {
		this.origin = req;
	}

	@Override
	public Iterable<String> head() throws IOException {
		return this.origin.head();
	}

	@Override
	public InputStream body() throws IOException {
		return this.origin.body();
	}

}
