package com.yangruihan.mytakes;

import java.io.IOException;

/**
 * HTTP-aware exception
 * 
 * 意识到的HTTP异常
 * 
 * @author Yrh
 *
 */
@SuppressWarnings({ "PMD.OnlyOneConstructorShouldDoInitialization", "PMD.BeanMembersShouldSerialize" })
public class HttpException extends IOException {

	/**
	 * Serialization marker.
	 */
	private static final long serialVersionUID = 5551295094654158589L;
	
	/**
	 * Status code.
	 * 
	 * 状态值
	 */
	private final int status;

	/**
	 * Ctor.
	 * 
	 * @param code
	 */
	public HttpException(final int code) {
		super(Integer.toString(code));
		this.status = code;
	}

	/**
	 * Ctor.
	 * 
	 * @param code
	 *            HTTP 状态值
	 * @param cause
	 *            造成的原因
	 */
	public HttpException(final int code, final String cause) {
		super(String.format("[%03d] %s", code, cause));
		this.status = code;
	}

	/**
	 * Ctor.
	 * 
	 * @param code
	 *            HTTP 状态值
	 * @param cause
	 *            造成的原因
	 */
	public HttpException(final int code, final Throwable cause) {
		super(cause);
		this.status = code;
	}

	/**
	 * Ctor.
	 * 
	 * @param code
	 *            HTTP 状态值
	 * @param msg
	 *            异常信息
	 * @param cause
	 *            造成的原因
	 */
	public HttpException(final int code, final String msg, final Throwable cause) {
		super(String.format("[%03d] %s", code, msg), cause);
		this.status = code;
	}

	/**
	 * HTTP status code.
	 * 
	 * @return Code
	 */
	public final int code() {
		return this.status;
	}
}
