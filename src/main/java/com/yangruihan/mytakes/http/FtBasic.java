package com.yangruihan.mytakes.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import com.yangruihan.mytakes.Take;

import lombok.EqualsAndHashCode;

/**
 * Basic front 基本前端类
 * 
 * @author Yrh
 *
 */
@EqualsAndHashCode(of = { "back", "socket" })
public final class FtBasic implements Front {

	/**
	 * Back 后端接口
	 */
	private final transient Back back;

	/**
	 * Server socket 服务器Socket
	 */
	private final transient ServerSocket socket;

	/**
	 * Ctor.
	 * 
	 * @param tks
	 *            Take 实例
	 * @throws IOException
	 */
	public FtBasic(final Take tks) throws IOException {
		/**
		 * 将 Take 实例封装在一个实现 Back 接口的实现类 BkBasic 默认端口为80
		 */
		this(new BkBasic(tks), 80);
	}

	/**
	 * Ctor.
	 * 
	 * @param tks
	 *            Take 实例
	 * @param prt
	 *            端口号
	 * @throws IOException
	 */
	public FtBasic(final Take tks, final int prt) throws IOException {
		/**
		 * 将 Take 实例封装在一个实现 Back 接口的实现类 BkBasic
		 */
		this(new BkBasic(tks), prt);
	}

	/**
	 * Ctor.
	 * 
	 * @param bck
	 *            Back 实例
	 * @param port
	 *            端口号
	 * @throws IOException
	 */
	public FtBasic(final Back bck, final int port) throws IOException {
		/**
		 * 将端口号封装成一个 ServerSocket
		 */
		this(bck, new ServerSocket(port));
	}

	/**
	 * Ctor.
	 * 
	 * @param bck
	 *            Back 实例
	 * @param skt
	 *            ServerSocket 实例
	 */
	public FtBasic(final Back bck, final ServerSocket skt) {
		this.back = bck;
		this.socket = skt;
	}

	/**
	 * Start and dispatch all incoming sockets. 开始并且分发接收到的 Sockets
	 * 
	 * @param exit
	 * @throws IOException
	 */
	public void start(Exit exit) throws IOException {
		this.socket.setSoTimeout((int) TimeUnit.SECONDS.toMillis(1L));
		try {
			do {
				this.loop(this.socket);
			} while (!exit.ready());
		} finally {
			this.socket.close();
		}
	}

	/**
	 * Make a loop cycle. 启动一个循环
	 * 
	 * @param server
	 * @throws IOException
	 */
	private void loop(final ServerSocket server) throws IOException {
		try {
			this.back.accept(server.accept());
		} catch (final SocketTimeoutException ex) {
			assert ex != null;
		}
	}
}
