package com.yangruihan.mytakes.misc;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Select elements into a new iterable with given condition.
 * 
 * 根据给定的条件选择元素进入一个新的可迭代对象中
 * 
 * @author Yrh
 *
 */
public final class Select<T> implements Iterable<T> {

	/**
	 * Internal storage to hold the elements from iterables.
	 * 
	 * 内部存储区用来存放从可迭代对象中获取的元素
	 */
	private final transient Iterable<T> list;
	
	/**
	 * The condition to filter the element in the iterator.
	 * 
	 * 迭代器中过滤元素的条件
	 */
	private final transient Condition<T> condition;
	
	/**
	 * To produce an iterable collection, determined by condition, combining a
	 * and b, with order of the elements in a first.
	 * 
	 * 产生一个可迭代的结合，元素的选取取决于条件，并且结合 a 和 b，
	 * a 的元素在前
	 * 
	 * @param itb 要选择的可迭代对象
	 * @param cond 条件
	 */
	public Select(final Iterable<T> itb, final Condition<T> cond) {
		this.list = itb;
		this.condition = cond;
	}
	
	@Override
	public String toString() {
		return String.format("%s [if %s]", this.list, this.condition);
	}
	
	@Override
	public Iterator<T> iterator() {
		return new SelectIterator<T>(this.list.iterator(), this.condition);
	}
	
	/**
	 * The select iterator to traverse the input iterables and return the
	 * elements from the list with given condition.
	 * 
	 * 选择迭代器遍历输入的可迭代对象并返回一个列表，其中元素符合给定条件
	 * 
	 * <p>This class is NOT thread-safe.
	 * 
	 * <p>这个类是线程不安全的
	 * 
	 * @author Yrh
	 *
	 * @param <E>
	 */
	private static final class SelectIterator<E> implements Iterator<E> {
		
		/**
		 * The iterator to reflect the traverse state.
		 * 
		 * 反应遍历状态的迭代器
		 */
		private final transient Iterator<E> iterator;
		
		/**
		 * The condition to filter the elements in the iterator.
		 * 
		 * 过滤迭代器中元素的条件
		 */
		private final transient Condition<E> condition;
		
		/**
		 * The buffer storing the objects of the iterator.
		 * 
		 * 存储迭代器对象的缓冲
		 */
		private final transient Queue<E> buffer;
		
		/**
		 * Ctor.
		 * @param itr 原始数据的迭代器
		 * @param cond 过滤条件
		 */
		public SelectIterator(final Iterator<E> itr, final Condition<E> cond) {
			this.iterator = itr;
			this.condition = cond;
			this.buffer = new LinkedList<>();
		}
		
		@Override
		public boolean hasNext() {
			if (this.buffer.isEmpty()) {
				while (this.iterator.hasNext()) {
					final E object = this.iterator.next();
					if (this.condition.fits(object)) {
						this.buffer.add(object);
						break;
					}
				}
			}
			return !this.buffer.isEmpty();
		}
		
		@Override
		public E next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException("No more element with fits the select condition.");
			}
			return this.buffer.poll();
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException("This iterable is immutable and cannot remove anything");
		}
	}
}
