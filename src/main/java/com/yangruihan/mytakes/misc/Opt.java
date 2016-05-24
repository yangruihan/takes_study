package com.yangruihan.mytakes.misc;

import lombok.EqualsAndHashCode;

/**
 * Replacement a nullable T reference with a non-null value.
 * 
 * 用一个非空的值代替一个可以为空的泛型引用
 * 
 * <p>All implementations of this interface must be immutable and thread-safe
 * 
 * <p>这个接口的所有实现必须是不变且线程安全的
 * 
 * @author Yrh
 *
 */
public interface Opt<T> {

	/**
	 * Returns the contained instance.
	 * 
	 * 返回包含的实例
	 * 
	 * @return instance 实例
	 */
	T get();
	
	/**
	 * Return true if contains instance
	 * 
	 * 如果包含实例返回真
	 * 
	 * @return
	 */
	boolean has();
	
	/**
	 * Holder for a single element only.
	 * 
	 * 只持有一个元素
	 * 
	 * @author Yrh
	 *
	 * @param <T>
	 */
	@EqualsAndHashCode(of = "origin")
	final class Single<T> implements Opt<T> {
		
		/**
		 * Origin.
		 * 
		 * 原始实例
		 * 
		 */
		private final transient T origin;
		
		/**
		 * Ctor.
		 * @param orgn
		 */
		public Single(final T orgn) {
			this.origin = orgn;
		}

		@Override
		public T get() {
			return this.origin;
		}

		@Override
		public boolean has() {
			return true;
		}
	}
	
	/**
	 * Empty instance.
	 * 
	 * 空实例
	 * 
	 * <p>The class is immutable and thread-safe.
	 * 
	 * <p>这个类是不可变的且线程安全的
	 * 
	 * @author Yrh
	 *
	 * @param <T>
	 */
	final class Empty<T> implements Opt<T> {

		@Override
		public T get() {
			throw new UnsupportedOperationException(
						"there is nothing here, use has() first, to check"
					);
		}

		@Override
		public boolean has() {
			return false;
		}
	}
}
