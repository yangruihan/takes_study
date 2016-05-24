package com.yangruihan.mytakes.rq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.LinkedList;

import com.yangruihan.mytakes.HttpException;
import com.yangruihan.mytakes.Request;
import com.yangruihan.mytakes.misc.Opt;
import com.yangruihan.mytakes.misc.Utf8String;

import lombok.EqualsAndHashCode;

/**
 * Live request.
 * 
 * 有生命力的请求（request）
 * 
 * <p>
 * The class is immutable and thread-class
 * 
 * <p>
 * 这个类是不可变的且线程安全的
 * 
 * @author Yrh
 *
 */
@EqualsAndHashCode(callSuper = true)
public final class RqLive extends RqWrap {

	/**
	 * Ctor.
	 * @param input 输入流
	 * @throws IOException
	 */
	public RqLive(final InputStream input) throws IOException {
		super(RqLive.parse(input));
	}

	/**
	 * Parse input stream
	 * 
	 * 解析输入流
	 * 
	 * @param input 输入流
	 * @return request 解析生成的请求对象（request）
	 * @throws IOException
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	private static Request parse(final InputStream input) throws IOException {
		boolean eof = true;
		final Collection<String> head = new LinkedList<>(); // 声明一个 LinkedList 用来存储头部数据
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Opt<Integer> data = new Opt.Empty<>();
		data = RqLive.data(input, data, false);
		while (data.get() > 0) {
			eof = false;
			if (data.get() == '\r') {
				RqLive.checkLineFeed(input, baos, head.size() + 1);
				if (baos.size() == 0) {
					break;
				}
				data = new Opt.Single<>(input.read());
				final Opt<String> header = RqLive.newHeader(data, baos);
				if (header.has()) {
					head.add(header.get());
				}
				data = RqLive.data(input, data, false);
				continue;
			}
			baos.write(RqLive.legalCharacter(data, baos, head.size() + 1));
			data = RqLive.data(input, new Opt.Empty<>(), true);
		}
		if (eof) {
			throw new IOException("empty request");
		}
		return new Request() {

			@Override
			public Iterable<String> head() throws IOException {
				return head;
			}

			@Override
			public InputStream body() throws IOException {
				return input;
			}
		};
	}
	
	/**
	 * Builds current read header.
	 * 
	 * 生成当前读的头（header）
	 * 
	 * @param data 当前读到的字节
	 * @param baos 当前读到的头（header）
	 * @return header
	 */
	private static Opt<String> newHeader(final Opt<Integer> data,
			final ByteArrayOutputStream baos) {
		Opt<String> header = new Opt.Empty<>();
		if (data.get() != ' ' && data.get() != '\t') {
			header = new Opt.Single<>(
						new Utf8String(baos.toByteArray()).string()
					);
			baos.reset();
		}
		return header;
	}
	
	/**
	 * Checks whether or not the next byte to read is a Line Feed.
	 * 
	 * 检查要读的下一个字节是否是换行符
	 * 
	 * <p><i>Please note that this method assumes that the previous byte read
	 * was a Carriage Return.</i>
	 * 
	 * <p>请注意，此方法假定之前一个字节是回车
	 * 
	 * @param input 要读的输入流
	 * @param baos 当前读取的头（header）
	 * @param position 当前头（header）的行数
	 * @throws IOException 
	 */
	private static void checkLineFeed(final InputStream input,
			final ByteArrayOutputStream baos, final Integer position) throws IOException {
		if (input.read() != '\n') {
			throw new HttpException(
						HttpURLConnection.HTTP_BAD_REQUEST,
						String.format("there is no LF after CR in header, line #%d: \"%s\"",
								position,
								new Utf8String(baos.toByteArray()).string()
						)
					);
		}
	}
	
	/**
	 * Returns a legal character based n the read character.
	 * 
	 * 返回一个基于位置n读到的字符的合法字符
	 * 
	 * @param data 读到的字符
	 * @param baos 包含读取到的头（header）的输出字节流
	 * @param position 当前头（header）的行数
	 * @return
	 * @throws IOException
	 */
	private static Integer legalCharacter(final Opt<Integer> data,
			final ByteArrayOutputStream baos, final Integer position) throws IOException {
		if ((data.get() > 0x7f || data.get() < 0x20) && data.get() != '\t') {
			throw new HttpException(
						HttpURLConnection.HTTP_BAD_REQUEST,
						String.format("illegal character 0x%02X in HTTP header line #%d: \"%s\"",
								data.get(),
								position,
								new Utf8String(baos.toByteArray()).string()
						)
					);
		}
		return data.get();
	}
	
	/**
	 * Obtains new byte if hasn't
	 * 
	 * 是否填充新的数据
	 * 
	 * @param input 输入流
	 * @param data 数据（空或者是当前数据）
	 * @param available 表明是否先检查这里是否有有可用的字节数据
	 * @return 下一个或者当前数据
	 * @throws IOException
	 */
	private static Opt<Integer> data(final InputStream input,
			final Opt<Integer> data, final boolean available) throws IOException {
		final Opt<Integer> ret;
		if (data.has()) {
			ret = data;
		} else if (available && input.available() <= 0) {
			ret = new Opt.Single<>(-1);
		} else {
			ret = new Opt.Single<>(input.read());
		}
		return ret;
	}
}
