package com.yangruihan.mytakes;

import java.io.IOException;
import java.io.InputStream;

/**
 * HTTP request.
 * 
 * <p>An object implementing this interface can be "parsed" using one
 * of the decorators available in {@link com.yangruihan.mytakes.rq} package. For example,
 * in order to fetch a query parameter you can use
 * {@link com.yangruihan.mytakes.rq.RqHref}:
 * 
 * <pre> final Iterable&lt;String@gt; params =
 * 	new RqHref(request).href().param("name");</pre>
 * 
 * <p>一个实现了这个接口的对象可以通过使用任意一个在{@link com.yangruihan.mytakes.rq}包里的
 * 装饰器“解析”
 * 
 * <p>All implementations of this interface must be immutable and thread-safe.
 * 
 * <p>这个接口的所有实现必须是不变且线程安全的
 * 
 * @author Yrh
 *
 */
public interface Request {

	/**
	 * All lines above the body.
	 * 
	 * 头部，所有在身体（body）之上的行
	 * 
	 * @return
	 * @throws IOException
	 */
	Iterable<String> head() throws IOException;
	
	/**
	 * HTTP request body.
	 * 
	 * HTTP 请求的身体（body）
	 * 
	 * @return
	 * @throws IOException
	 */
	InputStream body() throws IOException;
}
