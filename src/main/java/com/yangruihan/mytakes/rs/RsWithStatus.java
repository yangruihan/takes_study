package com.yangruihan.mytakes.rs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.yangruihan.mytakes.Response;
import com.yangruihan.mytakes.misc.Concat;
import com.yangruihan.mytakes.misc.Condition;
import com.yangruihan.mytakes.misc.Select;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Response decorator, with status code.
 * 
 * 使用状态值装饰响应（response）
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
public final class RsWithStatus extends RsWrap {

	/**
	 * Statuses and their reasons.
	 * 
	 * 状态值和他们的原因
	 */
	private static final Map<Integer, String> REASONS = 
			Collections.unmodifiableMap(RsWithStatus.make());
	
	/**
	 * Ctor.
	 * @param code 状态值
	 */
	public RsWithStatus(final int code) {
		this(new RsEmpty(), code);
	}
	
	/**
	 * Ctor.
	 * @param res 原始响应
	 * @param code 状态值
	 */
	public RsWithStatus(final Response res, final int code) {
		this(res, code, RsWithStatus.best(code));
	}
	
	/**
	 * Ctor.
	 * @param res 原始响应
	 * @param code 状态值
	 * @param rsn 原因
	 */
	public RsWithStatus(final Response res, final int code, final CharSequence rsn) {
		super(new Response() {
			
			@Override
			public Iterable<String> head() throws IOException {
				return RsWithStatus.head(res, code, rsn);
			}
			
			@Override
			public InputStream body() throws IOException {
				return res.body();
			}
		});
	}
	
	/**
	 * Make head.
	 * 
	 * 生成头部
	 * 
	 * @param origin 原始响应
	 * @param status 状态值
	 * @param reason 原因
	 * @return
	 * @throws IOException
	 */
	public static Iterable<String> head(final Response origin,
			final int status, final CharSequence reason) throws IOException {
		if (status < 100 || status > 999) {
			throw new IllegalArgumentException(
						String.format("according to RFC 7230 HTTP status code must have three digits: %d",
								status)
					);
		}
		return new Concat<String>(
					Collections.singletonList(String.format("HTTP/1.1 %d %s", status, reason)),
					new Select<String>(
								origin.head(),
								new Condition<String>() {
									
									public boolean fits(final String item) {
										return !item.startsWith("HTTP/");
									}
								}
							)
				);
	}
	
	/**
	 * Find the best reason for this status code.
	 * 
	 * 找到最适合这个状态值的原因
	 * 
	 * @param code
	 * @return
	 */
	public static String best(final int code) {
		String reason = RsWithStatus.REASONS.get(code);
		if (reason == null) {
			reason = "Unknown";
		}
		return reason;
	}
	
	/**
	 * Make all reasons.
	 * 
	 * 生成所有原因
	 * 
	 * @return
	 */
	private static Map<Integer, String> make() {
		final Map<Integer, String> map = new HashMap<>(0);
		map.put(HttpURLConnection.HTTP_OK, "OK");
        map.put(HttpURLConnection.HTTP_NO_CONTENT, "No Content");
        map.put(HttpURLConnection.HTTP_CREATED, "Created");
        map.put(HttpURLConnection.HTTP_ACCEPTED, "Accepted");
        map.put(HttpURLConnection.HTTP_MOVED_PERM, "Moved Permanently");
        map.put(HttpURLConnection.HTTP_MOVED_TEMP, "Moved Temporarily");
        map.put(HttpURLConnection.HTTP_SEE_OTHER, "See Other");
        map.put(HttpURLConnection.HTTP_NOT_MODIFIED, "Not Modified");
        map.put(HttpURLConnection.HTTP_USE_PROXY, "Use Proxy");
        map.put(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request");
        map.put(HttpURLConnection.HTTP_UNAUTHORIZED, "Unauthorized");
        map.put(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "Payment Required");
        map.put(HttpURLConnection.HTTP_FORBIDDEN, "Forbidden");
        map.put(HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
        map.put(HttpURLConnection.HTTP_BAD_METHOD, "Bad Method");
        map.put(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "Not Acceptable");
        map.put(HttpURLConnection.HTTP_CLIENT_TIMEOUT, "Client Timeout");
        map.put(HttpURLConnection.HTTP_GONE, "Gone");
        map.put(HttpURLConnection.HTTP_LENGTH_REQUIRED, "Length Required");
        map.put(HttpURLConnection.HTTP_PRECON_FAILED, "Precon Failed");
        map.put(HttpURLConnection.HTTP_ENTITY_TOO_LARGE, "Entity Too Large");
        map.put(HttpURLConnection.HTTP_REQ_TOO_LONG, "Request Too Long");
        map.put(HttpURLConnection.HTTP_UNSUPPORTED_TYPE, "Unsupported Type");
        map.put(HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Error");
        map.put(HttpURLConnection.HTTP_BAD_GATEWAY, "Bad Gateway");
        map.put(HttpURLConnection.HTTP_NOT_IMPLEMENTED, "Not Implemented");
        return map;
	}
}
