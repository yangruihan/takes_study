package com.yangruihan.mytakes.rs;

import java.io.IOException;
import java.io.InputStream;

import com.yangruihan.mytakes.Response;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Response decorator.
 * 
 * 响应（response）装饰器类
 * 
 * <p>The class is immutable and thread-class
 * 
 * <p>这个类是不可变的且线程安全的
 * 
 * @author Yrh
 *
 */
@ToString(of = "origin")
@EqualsAndHashCode(of = "origin")
public class RsWrap implements Response {

	/**
	 * Original response.
	 * 
	 * 原始响应（response） 
	 */
	private final transient Response origin;
	
	/**
	 * Ctor.
	 * @param res 原始响应（response） 
	 */
	public RsWrap(final Response res) {
		this.origin = res;
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
