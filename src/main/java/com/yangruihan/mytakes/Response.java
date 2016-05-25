package com.yangruihan.mytakes;

import java.io.IOException;
import java.io.InputStream;

/**
 * HTTP response.
 * 
 * HTTP 响应
 * 
 * <p>{@link com.yangruihan.mytakes.Response} interface is an abstraction of a HTTP
 * response, that consists of a few headers and a body. To construct
 * a response, use one of the composable decorators from
 * {@link com.yangruihan.mytakes.rs} package. For example, this code will create
 * a response with HTML inside:
 * 
 * <pre> final Response response = new RsWithHeader(
 * 	new RsWithBody(
 * 		new RsWithStatus(200),
 * 		"hello, world!"
 * 	),
 * 	"Content-Type", "text/html"
 * );</pre>
 * 
 * <p>{@link com.yangruihan.mytakes.Response} 接口是一个HTTP响应（HTTP response）的抽象，
 * 它由一些头部（header）和一个身体（body）组成。为了构造一个响应（response），可以使用
 * {@link com.yangruihan.mytakes.rs}包里组合的装饰器。
 * 
 * <p> The implementations of this interface may require that
 * {@link Response#head()} method has to be invoked before reading from the
 * {@code InputStream} obtained from the {@link Response#body()} method,
 * but they must NOT require that the {@link InputStream} has to be read
 * from before the {@link Response#head()} method invocation.
 * 
 * <p>这个接口的实现可能需要{@link Response#head()}这个方法在从{@link Response#body()}
 * 包含的{@code InputStream}中读取数据之前被调用，但是读取{@link InputStream}不一定要
 * 在{@link Response#head()}方法执行之前
 * 
 * <p>All implementations of this interface must be immutable and thread-safe.
 * 
 * <p>这个接口的所有实现必须是不变且线程安全的
 * 
 * @author Yrh
 *
 */
public interface Response {

	/**
	 * HTTP response head.
	 * 
	 * HTTP 响应头
	 * 
	 * @return
	 * @throws IOException
	 */
	Iterable<String> head() throws IOException;
	
	/**
	 * HTTP response body.
	 * 
	 * HTTP 响应身体
	 * 
	 * @return
	 * @throws IOException
	 */
	InputStream body() throws IOException;
}
