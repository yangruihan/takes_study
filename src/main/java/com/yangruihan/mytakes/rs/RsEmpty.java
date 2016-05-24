package com.yangruihan.mytakes.rs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import com.yangruihan.mytakes.Response;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Empty response.
 * 
 * 空响应
 * 
 * <p>The class is immutable and thread-class
 * 
 * <p>这个类是不可变的且线程安全的
 * 
 * @author Yrh
 *
 */
@ToString
@EqualsAndHashCode
public final class RsEmpty implements Response {

	@Override
	public Iterable<String> head() throws IOException {
		return Collections.singletonList("HTTP/1.1 200 OK");
	}

	@Override
	public InputStream body() throws IOException {
		return new ByteArrayInputStream(new byte[0]);
	}
}
