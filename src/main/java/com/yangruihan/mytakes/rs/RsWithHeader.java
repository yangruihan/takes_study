package com.yangruihan.mytakes.rs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.regex.Pattern;

import com.yangruihan.mytakes.Response;
import com.yangruihan.mytakes.misc.Concat;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Response decorator, with an additional header.
 * 
 * 响应装饰器，带有一个附加的头（header）
 * 
 * <p>Remember, if a header is already present in the response, this
 * decorator will add another one, with the same name. It doesn't check
 * for duplicates. If you want to avoid duplicate headers, use this
 * decorator in combination with {@link com.yangruihan.mytakes.rs.RsWithoutHeader},
 * for example:
 *
 * <pre> new RsWithHeader(
 *   new RsWithoutHeader(res, "Host"),
 *   "Host", "www.example.com"
 * )</pre>
 * 
 * <p>记住，如果一个头已经在一个响应中了，这个装饰器会同时添加带有相同名字的另一个。
 * 它不会检查冗余。如果你想避免冗余的头部（header），组合使用这个装饰器
 * {@link com.yangruihan.mytakes.rs.RsWithoutHeader}。
 * 
 *
 * <p>In this example, {@link com.yangruihan.mytakes.rs.RsWithoutHeader} will remove the
 * {@code Host} header first and {@link com.yangruihan.mytakes.rs.RsWithHeader} will
 * add a new one.
 * 
 * <p>在样例中，{@link com.yangruihan.mytakes.rs.RsWithoutHeader}会首先移除{@code Host}头部，
 * 然后{@link com.yangruihan.mytakes.rs.RsWithHeader}会增加一个新的
 *
 * <p>The class is immutable and thread-safe.
 * 
 * <p>这个类是不可变的且线程安全的
 * 
 * @author Yrh
 *
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class RsWithHeader extends RsWrap {

	/**
	 * Pattern for all other lines in the head.
	 * 
	 * 在头部中所有其他行的模式
	 */
	private static final Pattern HEADER = Pattern.compile("[a-zA-Z0-9\\-]+:\\p{Print}+");
	
	/**
	 * Ctor.
	 * @param hdr
	 */
	public RsWithHeader(final CharSequence hdr) {
		this(new RsEmpty(), hdr);
	}
	
	/**
	 * Ctor.
	 * @param name
	 * @param value
	 */
	public RsWithHeader(final CharSequence name, final CharSequence value) {
		this(new RsEmpty(), name, value);
	}
	
	/**
	 * Ctor.
	 * @param res
	 * @param name
	 * @param value
	 */
	public RsWithHeader(final Response res, final CharSequence name, final CharSequence value) {
		this(res, String.format("%s: %s", name, value));
	}
	
	/**
	 * Ctor.
	 * @param res
	 * @param header
	 */
	public RsWithHeader(final Response res, final CharSequence header) {
		super(
			new Response() {
				
				@Override
				public Iterable<String> head() throws IOException {
					return RsWithHeader.extend(res.head(), header.toString());
				}
				
				@Override
				public InputStream body() throws IOException {
					return res.body();
				}
			}
		);
	}
	
	/**
	 * Add to head additional header.
	 * 
	 * 向头部中添加附加头数据
	 * 
	 * @param head
	 * @param header
	 * @return
	 */
	private static Iterable<String> extend(final Iterable<String> head, final String header) {
		if (!RsWithHeader.HEADER.matcher(header).matches()) {
			throw new IllegalArgumentException(
						String.format(
								"header line of HTTP response \"%s\" doesn't match \"%s\" regular expression, but it should, according to RFC 7230",
								header,
								RsWithHeader.HEADER
						)
					);
		}
		return new Concat<String>(head, Collections.singleton(header));
	}
}
