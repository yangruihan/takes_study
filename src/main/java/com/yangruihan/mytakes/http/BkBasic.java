package com.yangruihan.mytakes.http;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.Socket;

import com.yangruihan.mytakes.HttpException;
import com.yangruihan.mytakes.Request;
import com.yangruihan.mytakes.Response;
import com.yangruihan.mytakes.Take;
import com.yangruihan.mytakes.misc.Utf8PrintStream;
import com.yangruihan.mytakes.rq.RqLive;
import com.yangruihan.mytakes.rq.RqWithHeaders;
import com.yangruihan.mytakes.rs.RsPrint;
import com.yangruihan.mytakes.rs.RsText;
import com.yangruihan.mytakes.rs.RsWithStatus;

import lombok.EqualsAndHashCode;

/**
 * Basic back-end
 * 
 * 基础后端
 * 
 * <p>
 * The class is immutable and thread-safe.
 * 
 * <p>
 * 这个类是不可变的且线程安全的
 * 
 * @author Yrh
 *
 */
@EqualsAndHashCode(of = "take")
public final class BkBasic implements Back {

	/**
	 * Take.
	 */
	private final transient Take take;

	/**
	 * Ctor.
	 * 
	 * @param tks
	 */
	public BkBasic(final Take tks) {
		this.take = tks;
	}

	/**
	 * Accept and dispatch this socket.
	 * 
	 * 接收并且分发这个 Socket.
	 * 
	 * @param socket
	 * @throws IOException
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public void accept(Socket socket) throws IOException {
		try (
				final InputStream input = socket.getInputStream();
				final BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
			) {
			while (true) {
				this.print(
					BkBasic.addSocketHeaders(
						new RqLive(input), 
						socket
					),
					output
				);
				
				if (input.available() <= 0) {
					break;
				}
			}
		}
	}
	
	/**
	 * Print response to output stream, safely.
	 * 
	 * 安全地打印响应（response）到输出流中
	 * 
	 * @param req
	 * @param output
	 */
	@SuppressWarnings("PMD.AvoidCatchingThrowable")
	private void print(final Request req, final OutputStream output) throws IOException {
		try {
			new RsPrint(this.take.act(req)).print(output);
		} catch (final HttpException ex) {
			new RsPrint(BkBasic.failure(ex, ex.code())).print(output);
		} catch (final Throwable ex) {
			new RsPrint(
					BkBasic.failure(
						ex,
						HttpURLConnection.HTTP_INTERNAL_ERROR
					)
				).print(output);
		}
	}
	
	/**
	 * Make a failure response.
	 *
	 * 生成一个错误响应
	 *
	 * @param err 错误
	 * @param code HTTP 错误代码
	 * @return response 响应
	 * @throws IOException
	 */
	private static Response failure(final Throwable err, final int code) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream stream = new Utf8PrintStream(baos, false);
		
		try {
			err.printStackTrace(stream);
		} finally {
			stream.close();
		}
		return new RsWithStatus(
					new RsText(new ByteArrayInputStream(baos.toByteArray())),
					code
				);
	}
	
	/**
	 * Adds custom headers with information about socket.
	 * @param req 请求
	 * @param socket Socket
	 * @return request 带有自定义头的请求
	 */
	private static Request addSocketHeaders(final Request req, final Socket socket) {
		return new RqWithHeaders(
					req,
					String.format("X-Takes-LocalAddress: %s",
								socket.getLocalAddress().getHostAddress()),
					String.format("X-Takes-LocalPort: %d",
								socket.getLocalPort()),
					String.format("X-Takes-RemoteAddress: %s",
								socket.getInetAddress().getHostAddress()),
					String.format("X-Takes-RemotePort: %d",
								socket.getPort())
				);
	}
}
