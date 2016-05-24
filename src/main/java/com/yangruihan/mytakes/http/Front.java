package com.yangruihan.mytakes.http;

import java.io.IOException;

/**
 * HTTP front 
 * 
 * 前端接口
 * 
 * @author Yrh
 *
 */
public interface Front {

	/**
	 * Start and dispatch all incoming sockets.
	 * 
	 * 开始并且分发接收到的 Sockets
	 * 
	 * @param exit
	 * @throws IOException
	 */
	void start(Exit exit) throws IOException;
}
