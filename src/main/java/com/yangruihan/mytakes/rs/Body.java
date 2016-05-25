package com.yangruihan.mytakes.rs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The body of a response used by {@link RsWithBody}.
 * 
 * 响应体
 * 
 * @author Yrh
 *
 */
@SuppressWarnings("PMD.TooManyMethods")
public interface Body {

	/**
	 * Gives an {@code InputStream} corresponding to the content of
	 * the body.
	 * 
	 * 给出对应响应体内容的输入流
	 * 
	 * @return
	 * @throws IOException
	 */
	InputStream input() throws IOException;
	
	/**
	 * Gives the length of the stream.
	 * 
	 * 给出这个流的长度
	 * 
	 * @return
	 * @throws IOException
	 */
	int length() throws IOException;
	
	/**
	 * Content of a body based on an {@link java.net.URL}.
	 * 
	 * 基于{@link java.net.URL}的响应体内容
	 * 
	 * @author Yrh
	 *
	 */
	final class URL implements Body {
		
		/**
		 * The {@link java.net.URL} of the content.
		 * 
		 * 内容的{@link java.net.URL}
		 */
		private final transient java.net.URL url;
		
		/**
		 * Constructs an {@code URL} with the specified {@link java.net.URL}.
		 * 
		 * 构造一个带有指定{@link java.net.URL}的{@code URL}
		 * 
		 * @param content
		 */
		public URL(final java.net.URL content) {
			this.url = content;
		}

		@Override
		public InputStream input() throws IOException {
			return this.url.openStream();
		}

		@Override
		public int length() throws IOException {
			try (final InputStream input = this.url.openStream()) {
				return input.available();
			}
		}
	}
	
	/**
	 * Content of a body based on a byte array.
	 * 
	 * 基于字节数组的响应体内容
	 * 
	 * @author Yrh
	 *
	 */
	final class ByteArray implements Body {
		
		/**
		 * The content of the body in a byte array.
		 * 
		 * 响应体中的字节数组内容
		 */
		private final transient byte[] bytes;
		
		/**
		 * Constructs an {@code ByteArray} with the specified byte array.
		 * 
		 * 构造一个带有指定字节数组的{@code ByteArray}
		 * 
		 * @param content
		 */
		public ByteArray(final byte[] content) {
			this.bytes = content;
		}

		@Override
		public InputStream input() throws IOException {
			return new ByteArrayInputStream(this.bytes);
		}

		@Override
		public int length() throws IOException {
			return this.bytes.length;
		}
	}
	
	/**
	 * The content of the body based on an {@link InputStream}.
	 * 
	 * 基于{@link InputStream}的响应体内容
	 * 
	 * @author Yrh
	 *
	 */
	final class Stream implements Body {
		
		/**
		 * The content of the body in an InputStream.
		 * 
		 * 响应体中的输入流
		 */
		private final transient InputStream stream;
		
		/**
		 * The length of the stream.
		 * 
		 * 流的长度
		 */
		private final transient AtomicInteger length;
		
		/**
		 * Constructs an {@code Stream} with the specified {@link InputStream}.
		 * 
		 * 构造一个带有指定{@link InputStream}的{@code Stream}
		 * 
		 * @param input
		 */
		Stream(final InputStream input) {
			this.stream = input;
			this.length = new AtomicInteger(-1);
		}

		@Override
		public InputStream input() throws IOException {
			this.estimate();
			return this.stream;
		}

		@Override
		public int length() throws IOException {
			this.estimate();
			return this.length.get();
		}
		
		/**
		 * Estimates the length of the {@code InputStream}.
		 * 
		 * 估计{@code InputStream}的长度
		 * 
		 * @throws IOException
		 */
		private void estimate() throws IOException {
			if (this.length.get() == -1) {
				this.length.compareAndSet(-1, this.stream.available());
			}
		}
	}
	
	/**
	 * Decorator that will store the content of the underlying Body into a
	 * temporary File.
	 * 
	 * 装饰器将会将基础身体（body）的内容存入一个临时文件
	 * 
	 * <p><b>The content of the Body will be stored into a temporary
	 * file to be able to read it as many times as we want so use it only
	 * for large content, for small content use {@link Body.ByteArray}
	 * instead.</b>
	 * 
	 * <p><b>存入临时文件的身体（body）内容可以随心所欲的被我们读取，不过仅限于
	 * 大型内容，对于一个小型内容应使用{@link Body.ByteArray}替代
	 * 
	 * @author Yrh
	 *
	 */
	final class TempFile implements Body {
		
		/**
		 * The temporary file that contains the content of the body.
		 * 
		 * 存储身体（body）内容的临时文件
		 */
		private final transient File file;
		
		/**
		 * The underlying body.
		 * 
		 * 基础身体（body）
		 */
		private final transient Body body;
		
		/**
		 * Constructs a {@code TempFile} with the specified {@link Body}.
		 * 
		 * 构造一个带有指定{@link Body}的{@code TempFile}
		 * 
		 * @param body
		 */
		public TempFile(final Body body) {
			this.body = body;
			this.file = new File(
							System.getProperty("java.io.tmpdir"),
							String.format("%s-%s.tmp", 
									Body.TempFile.class.getName(),
									UUID.randomUUID().toString()
							)
						);
		}

		@Override
		public InputStream input() throws IOException {
			return new FileInputStream(this.file());
		}

		@Override
		public int length() throws IOException {
			return (int)this.file().length();
		}
		
		/**
		 * Needed to remove the file once the Stream object is no more used.
		 * 
		 * 一旦该流对象不再使用则应该清除临时文件
		 */
		@Override
		protected void finalize() throws Throwable {
			try {
				Files.delete(Paths.get(this.file.getAbsolutePath()));
			} finally {
				super.finalize();
			}
		}
		
		/**
		 * Gives the {@code File} that contains the content of the underlying
		 * {@code  Body}.
		 * 
		 * 给出包含基础{@code  Body}内容的{@code File}
		 * 
		 * @return
		 * @throws IOException
		 */
		private File file() throws IOException {
			synchronized (this.file) {
				if (!this.file.exists()) {
					this.file.deleteOnExit();
					try (final InputStream content = this.body.input()) {
						Files.copy(
								content, Paths.get(this.file.getAbsolutePath()),
								StandardCopyOption.REPLACE_EXISTING
						);
					}
				}
				return this.file;
			}
		}
	}
}
