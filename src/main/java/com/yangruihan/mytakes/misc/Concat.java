package com.yangruihan.mytakes.misc;

import java.util.Iterator;

/**
 * Concatenating iterables. It produces an iterable collection combining A and
 * B, with order of the elements in A first.
 * 
 * 串联可迭代的变量。它结合 A 和 B 产生一个可迭代的集合，A 的元素在前面
 *  
 * @author Yrh
 *
 */
public final class Concat<T> implements Iterable<T> {

	/**
	 * Internal reference to hold the first elements from constructor.
	 * 
	 * 保存从构造函数获取的第一个元素的内部引用
	 */
	private final transient Iterable<T> left;
	
	/**
	 * Internal reference to hold the second elements from constructor.
	 * 
	 * 保存从构造函数获取的第二个元素的内部引用
	 */
	private final transient Iterable<T> right;
	
	/**
	 * Ctor.
	 * @param aitb 第一个可以迭代的元素
	 * @param bitb 第二个可以迭代的元素
	 */
	public Concat(final Iterable<T> aitb, final Iterable<T> bitb) {
		this.left = aitb;
		this.right = bitb;
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s", this.left, this.right);
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Concat.ConcatIterator<T>(this.left.iterator(), this.right.iterator());
	}
	
	/**
	 * The concat iterator to traverse the input iterables as if they are
	 * from one list.
	 * 
	 * Concat 迭代器遍历输入可迭代的变量，好像它们是从一个列表中输入的
	 * 
	 * <p>This class is NOT thread-safe.
	 * 
	 * <p>这个类不是线程安全的
	 * 
	 * @author Yrh
	 *
	 * @param <T>
	 */
	private static final class ConcatIterator<E> implements Iterator<E> {
		
		/**
		 * Internal reference for holding the first iterator from constructor.
		 * 
		 * 保存从构造函数获取的第一个迭代器的内部引用
		 */
		private final transient Iterator<E> left;
		
		/**
		 * Internal reference for holding the first iterator from constructor.
		 * 
		 * 保存从构造函数获取的第二个迭代器的内部引用
		 */
		private final transient Iterator<E> right;
		
		ConcatIterator(final Iterator<E> aitr, final Iterator<E> bitr) {
			this.left = aitr;
			this.right = bitr;
		}
		
		@Override
		public boolean hasNext() {
			return this.left.hasNext() || this.right.hasNext();
		}
		
		@Override
		public E next() {
			final E object;
			if (this.left.hasNext()) {
				object = this.left.next();
			} else {
				object = this.right.next();
			}
			return object;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException("This iterable is immutable and cannot remove anything");
		}
	}
}
