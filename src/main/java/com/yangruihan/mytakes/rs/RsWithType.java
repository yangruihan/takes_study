package com.yangruihan.mytakes.rs;

import java.nio.charset.Charset;

import com.yangruihan.mytakes.Response;
import com.yangruihan.mytakes.misc.Opt;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Response decorator, with content type.
 * 
 * 响应装饰器，带有一个内容类型
 * 
 * <p>The class is immutable and thread-class
 * 
 * <p>这个类是不可变的且线程安全的
 * 
 * @author Yrh
 *
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class RsWithType extends RsWrap {

	/**
	 * Type header.
	 * 
	 * 类型头
	 */
	private static final String HEADER = "Content-Type";
	
	/**
	 * The name of the parameter allowing to define the character set.
	 * 
	 * 允许定义字符集参数名
	 */
	private static final String CHARSET = "charset";
	
	/**
	 * Constructs a {@code RsWithType} that will add the content type header to
     * the response using the specified type as media type.
     * 
     * 构造一个{@code RsWithType}会将一个指定类型的内容类型头添加到响应当中作为媒体类型
     * 
     * <p>The resulting header is of type {@code Content-Type: media-type}.
     * 
     * <p>结果头的类型为{@code Content-Type: media-type}
     * 
	 * @param res
	 * @param type
	 */
	public RsWithType(final Response res, final CharSequence type) {
		this(res, type, new Opt.Empty<Charset>());
	}
	
	/**
	 * Constructs a {@code RsWithType} that will add the content type header to
     * the response using the specified type as media type and the specified
     * charset as charset parameter value.
     * 
     * 构造一个{@code RsWithType}会将一个指定类型的内容类型头添加到响应当中作为媒体类型
     * 并且其中的字符集为字符集参数的值
     * 
     * <p>The resulting header
     * is of type {@code Content-Type: media-type; charset=charset-value}.
     * 
     * <p>结果头的类型为{@code Content-Type: media-type; charset=charset-value}
     * 
	 * @param res
	 * @param type
	 * @param charset
	 */
	public RsWithType(final Response res, final CharSequence type, final Charset charset) {
		this(res, type, new Opt.Single<Charset>(charset));
	}
	
	/**
	 * Constructs a {@code RsWithType} that will add the content type header to
     * the response using the specified type as media type and the specified
     * charset as charset parameter value if present.
     * 
     * 构造一个{@code RsWithType}会将一个指定类型的内容类型头添加到响应当中作为媒体类型
     * 并且其中的字符集为字符集参数的值如果存在的话
     * 
	 * @param res
	 * @param type
	 * @param charset
	 */
	private RsWithType(final Response res, final CharSequence type, final Opt<Charset> charset) {
		super(RsWithType.make(res, type, charset));
	}
	
	/**
	 * Factory allowing to create {@code Response} with the corresponding
     * content type and character set.
     * 
     * 允许创建带有相应内容和字符集的{@code Response}的工厂
     * 
	 * @param res
	 * @param type
	 * @param charset
	 * @return
	 */
	private static Response make(final Response res, final CharSequence type, final Opt<Charset> charset) {
		final Response response;
		if (charset.has()) {
			response = new RsWithHeader(
						new RsWithoutHeader(res, RsWithType.HEADER),
						RsWithType.HEADER,
						String.format("%s; %s=%s", type, RsWithType.CHARSET, charset.get().name())
					);
		} else {
			response = new RsWithHeader(
						new RsWithoutHeader(res, RsWithType.HEADER),
						RsWithType.HEADER,
						type
					);
		}
		return response;
	}
	
	/**
	 * Response decorator, with content type text/html.
	 * 
	 * 响应装饰器，带有 text/html 内容类型
     *
     * <p>The class is immutable and thread-safe.
     * 
     * <p>这个类是不可变的且线程安全的
     * 
	 * @author Yrh
	 *
	 */
	public static final class HTML extends RsWrap {
		
		/**
		 * Constructs a {@code HTML} that will add text/html as the content type
         * header to the response.
         * 
         * 构造一个带有text/html作为内容类型的响应
         * 
		 * @param res
		 */
		public HTML(final Response res) {
			this(res, new Opt.Empty<Charset>());
		}
		
		/**
		 * Constructs a {@code HTML} that will add text/html as the content type
         * header to the response using the specified charset as charset
         * parameter value.
         * 
         * 构造一个带有text/html作为内容类型的响应，并且使用指定的字符集
         * 
		 * @param res
		 * @param charset
		 */
		public HTML(final Response res, final Charset charset) {
			this(res, new Opt.Single<>(charset));
		}
		
		/**
		 * Constructs a {@code HTML} that will add text/html as the content type
         * header to the response using the specified charset as charset
         * parameter value if present.
         * 
         * 构造一个带有text/html作为内容类型的响应，并且使用指定的字符集如果存在的话
         * 
		 * @param res
		 * @param charset
		 */
		private HTML(final Response res, final Opt<Charset> charset) {
			super(RsWithType.make(res, "text/html", charset));
		}
	}
	
	/**
	 * Response decorator, with content type application/json.
     *
     * 响应装饰器，带有 application/json 内容类型
     *
     * <p>The class is immutable and thread-safe.
     * 
     * <p>这个类是不可变的且线程安全的
     * 
	 * @author Yrh
	 *
	 */
	public static final class JSON extends RsWrap {
		
		/**
		 * Constructs a {@code JSON} that will add application/json as the
         * content type header to the response.
         * 
         * 构造一个带有application/json作为内容类型的响应
         * 
		 * @param res
		 */
		public JSON(final Response res) {
			this(res, new Opt.Empty<Charset>());
		}
		
		/**
		 * Constructs a {@code JSON} that will add application/json as the
         * content type header to the response using the specified charset as
         * charset parameter value.
         * 
         * 构造一个带有application/json作为内容类型的响应，并且使用指定的字符集
         * 
		 * @param res
		 * @param charset
		 */
		public JSON(final Response res, final Charset charset) {
			this(res, new Opt.Single<>(charset));
		}
		
		/**
		 * Constructs a {@code JSON} that will add application/json as the
         * content type header to the response using the specified charset as
         * charset parameter value if present.
         * 
         * 构造一个带有application/json作为内容类型的响应，并且使用指定的字符集如果存在的话
         * 
		 * @param res
		 * @param charset
		 */
		private JSON(final Response res, final Opt<Charset> charset) {
			super(RsWithType.make(res, "application/json", charset));
		}
	}
	
	/**
	 * Response decorator, with content type text/xml.
     *
     * 响应装饰器，带有 text/xml 内容类型
     *
     * <p>The class is immutable and thread-safe.
     * 
     * <p>这个类是不可变的且线程安全的
     * 
	 * @author Yrh
	 *
	 */
	public static final class XML extends RsWrap {
		
		/**
		 * Constructs a {@code XML} that will add text/xml as the content type
         * header to the response.
         * 
         * 构造一个带有text/xml作为内容类型的响应
         * 
		 * @param res
		 */
		public XML(final Response res) {
            this(res, new Opt.Empty<Charset>());
        }
		
		/**
		 * Constructs a {@code XML} that will add text/xml as the content type
         * header to the response using the specified charset as charset
         * parameter value.
         * 
         * 构造一个带有text/xml作为内容类型的响应，并且使用指定的字符集
         * 
		 * @param res
		 * @param charset
		 */
		public XML(final Response res, final Charset charset) {
            this(res, new Opt.Single<Charset>(charset));
        }
		
		/**
		 * Constructs a {@code XML} that will add text/xml as the content type
         * header to the response using the specified charset as charset
         * parameter value if present.
         * 
         * 构造一个带有text/xml作为内容类型的响应，并且使用指定的字符集如果存在的话
         * 
		 * @param res
		 * @param charset
		 */
		private XML(final Response res, final Opt<Charset> charset) {
            super(RsWithType.make(res, "text/xml", charset));
        }
	}
	
	/**
	 * Response decorator, with content type text/plain.
     *
     * 响应装饰器，带有 text/plain 内容类型
     *
     * <p>The class is immutable and thread-safe.
     * 
     * <p>这个类是不可变的且线程安全的
     * 
	 * @author Yrh
	 *
	 */
	public static final class Text extends RsWrap {
		
		/**
		 * Constructs a {@code Text} that will add text/plain as the content
         * type header to the response.
         * 
         * 构造一个带有text/plain作为内容类型的响应
         * 
		 * @param res
		 */
		public Text(final Response res) {
            this(res, new Opt.Empty<Charset>());
        }
		
		/**
		 * Constructs a {@code Text} that will add text/plain as the content
         * type header to the response using the specified charset as charset
         * parameter value.
         * 
         * 构造一个带有text/plain作为内容类型的响应，并且使用指定的字符集
         * 
		 * @param res
		 * @param charset
		 */
		public Text(final Response res, final Charset charset) {
            this(res, new Opt.Single<Charset>(charset));
        }
		
		/**
		 * Constructs a {@code Text} that will add text/plain as the content
         * type header to the response using the specified charset as charset
         * parameter value if present.
         * 
         * 构造一个带有text/plain作为内容类型的响应，并且使用指定的字符集如果存在的话
         * 
		 * @param res
		 * @param charset
		 */
		private Text(final Response res, final Opt<Charset> charset) {
            super(RsWithType.make(res, "text/plain", charset));
        }
	}
}
