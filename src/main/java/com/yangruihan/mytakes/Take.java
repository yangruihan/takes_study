package com.yangruihan.mytakes;

import java.io.IOException;

/**
 * Take.
 * 
 * <p>Take is a momentary snapshot of in-server reality, visible to the
 * end user via printable {@link com.yangruihan.mytakes.Response}.
 * For example, this is a simple web server
 * that returns "hello, world!" plain text web page:
 * 
 * <pre> new FtBasic(
 * 	new Take() {
 * 		public Response act(final Request req) {
 * 			return new RsText("hello, world!");
 * 		}
 * 	},
 * 	8080
 * ).start(Exit.NEVER);</pre>
 * 
 * <p>Take 是一个在现实服务器中，对最终用户通过打印{@link com.yangruihan.mytakes.Response}
 * 可见的瞬间快照。
 * 
 * <p>There are a few classes that implement this interface and you
 * can create your own. But the best way is to start with
 * {@link com.yangruihan.mytakes.facets.fork.TkFork}, for example:
 * 
 * <pre> new FtBasic(
 * 	new TkFork(new FkRegex("/", "hello, world!")), 8080
 * ).start(Exit.NEVER);</pre>
 * 
 * <p>这里有一些实现了这个接口的类，并且你也可以自己实现。但是最好的方式
 * 是以使用{@link com.yangruihan.mytakes.facets.fork.TkFork}
 * 
 * <p>This code will start an HTTP server on port 8080 and will forward
 * all HTTP requests to the instance of class
 * {@link com.yangruihan.mytakes.facets.fork.TkFork}.
 * That object will try to find the best suitable "fork" amongst all
 * encapsulated objects. There is only one in the example above &mdash;
 * an instance of {@link com.yangruihan.mytakes.facets.fork.FkRegex}.
 * 
 * <p>这里的代码在8080端口开启了一个HTTP服务器，并且转交所有的HTTP 请求（request）
 * 到这个{@link com.yangruihan.mytakes.facets.fork.TkFork}实例。
 * 这个对象试图在所有封装对象中寻找最合适的“分支”。在上面的例子中只有一个“ /”，
 * 它是一个{@link com.yangruihan.mytakes.facets.fork.FkRegex}的实例
 * 
 * <p>All implementations of this interface must be immutable and thread-safe.
 * 
 * <p>这个接口的所有实现必须是不变且线程安全的
 * 
 * @author Yrh
 *
 */
public interface Take {

	/**
	 * Convert request to response.
	 * 
	 * 将请求（request）转换成响应（response）
	 * 
	 * @param req 请求
	 * @return response 响应
	 * @throws IOException
	 */
	Response act(Request req) throws IOException;
}
