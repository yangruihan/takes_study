package com.yangruihan.mytakes.rq;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.yangruihan.mytakes.Request;

import lombok.EqualsAndHashCode;

/**
 * Request with extra header.
 * 
 * 为请求（request）添加额外信息头（header）
 * 
 * @author Yrh
 *
 */
@EqualsAndHashCode(callSuper = true)
public final class RqWithHeaders extends RqWrap {

	/**
	 * Ctor.
	 * @param req 原始请求
	 * @param headers 需要添加的头信息
	 */
	public RqWithHeaders(final Request req, final CharSequence... headers) {
		this(req, Arrays.asList(headers));
	}
	
	/**
	 * Ctor.
	 * @param req 原始请求
	 * @param headers 需要添加的头信息
	 */
	public RqWithHeaders(final Request req, final Iterable<? extends CharSequence> headers) {
		super(new Request() {
			
			@Override
			public List<String> head() throws IOException {
				final List<String> head = new LinkedList<>();
				for (final String hdr : req.head()) {
					head.add(hdr);
				}
				for (final CharSequence header : headers) {
					head.add(header.toString().trim());
				}
				return head;
			}
			
			@Override
			public InputStream body() throws IOException {
				return req.body();
			}
		});
	}
}
