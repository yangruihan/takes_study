package com.yangruihan.mytakes.misc;

/**
 * Condition to determine how {@link Select} behave when filtering an iterable.
 * 
 * 条件用以决定当过滤一个可迭代对象时哪一种{@link Select}行为
 * 
 * @author Yrh
 *
 * @param <T>
 */
public interface Condition<T> {

	/**
	 * Determine if an element should be added.
	 * 
	 * 决定一个元素是否应该被添加
	 * 
	 * @param element 元素
	 * @return
	 */
	boolean fits(T element);
	
	/**
	 * Negating condition of any condition.
	 * 
	 * 否定任何条件
	 * 
	 * @author Yrh
	 *
	 * @param <T>
	 */
	final class Not<T> implements Condition<T> {
		
		/**
		 * 需要否定的条件
		 */
		private final transient Condition<T> condition;
		
		/**
		 * Ctor.
		 * @param cond 需要否定的条件
		 */
		public Not(final Condition<T> cond) {
			this.condition = cond;
		}
		
		@Override
		public boolean fits(T element) {
			return !this.condition.fits(element);
		}
	}
}
