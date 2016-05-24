package com.yangruihan.mytakes.http;

import java.io.IOException;
import java.net.Socket;

/**
 * HTTP back. 后端接口
 * 
 * <p>All implementations of this interface must be immutable and thread-safe
 * 
 * <p>这个接口的所有实现必须是不变且线程安全的
 * 
 * @author Yrh
 *
 */
public interface Back {

	/**
	 * Accept and dispatch this socket.
	 * 
	 * 接收并且分发这个 Socket.
	 * 
	 * @param socket
	 * @throws IOException
	 */
	void accept(Socket socket) throws IOException;
}
