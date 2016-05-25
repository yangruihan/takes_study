package com.yangruihan.mytakes.rs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import com.yangruihan.mytakes.Response;
import com.yangruihan.mytakes.misc.Condition;
import com.yangruihan.mytakes.misc.Select;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Response decorator, without a header.
 * 
 * 响应装饰器，删除一个头数据
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
public final class RsWithoutHeader extends RsWrap {
	
	/**
	 * Ctor.
	 * @param res
	 * @param name
	 */
	public RsWithoutHeader(final Response res, final CharSequence name) {
		super(
			new Response() {
				
				@Override
				public Iterable<String> head() throws IOException {
					final String prefix = String.format("%s:", name.toString().toLowerCase(Locale.ENGLISH));
					return new Select<String>(
								res.head(),
								new Condition<String>() {
									public boolean fits(final String header) {
										return !header.toLowerCase(Locale.ENGLISH).startsWith(prefix);
									};
								}
							);
				}
				
				@Override
				public InputStream body() throws IOException {
					return res.body();
				}
			}
		);
	}
}
