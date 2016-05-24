package com.yangruihan.mytakes.http;

/**
 * HTTP front exit. 前端退出接口
 * 
 * @author Yrh
 *
 */
public interface Exit {

	/**
	 * Never exit. 从不退出
	 */
	Exit NEVER = new Exit() {

		public boolean ready() {
			return false;
		}

	};

	/**
	 * Ready to exit? 是否准备退出
	 * 
	 * @return
	 */
	boolean ready();

	/**
	 * OR. 模拟逻辑或运算
	 * 
	 * @author Yrh
	 *
	 */
	final class Or implements Exit {

		/**
		 * Left. 左边
		 */
		private final transient Exit left;

		/**
		 * Right. 右边
		 */
		private final transient Exit right;

		/**
		 * Ctor.
		 * 
		 * @param lft
		 *            左边
		 * @param rht
		 *            右边
		 */
		public Or(final Exit lft, final Exit rht) {
			this.left = lft;
			this.right = rht;
		}

		public boolean ready() {
			return this.left.ready() || this.right.ready();
		}
	}
	
	/**
	 * AND. 模拟逻辑与运算
	 * @author Yrh
	 *
	 */
	final class And implements Exit {
		
		/**
		 * Left. 左边
		 */
		private final transient Exit left;

		/**
		 * Right. 右边
		 */
		private final transient Exit right;

		/**
		 * Ctor.
		 * 
		 * @param lft
		 *            左边
		 * @param rht
		 *            右边
		 */
		public And(final Exit lft, final Exit rht) {
			this.left = lft;
			this.right = rht;
		}

		public boolean ready() {
			return this.left.ready() && this.right.ready();
		}
	}
	
	/**
	 * NOT. 逻辑非运算
	 * @author Yrh
	 *
	 */
	final class Not implements Exit {
		
		/**
		 * Origin 原始数据
		 */
		private final transient Exit origin;
		
		/**
		 * Ctor.
		 * @param exit Origin 原始数据
		 */
		public Not(final Exit exit) {
			this.origin = exit;
		}
		
		public boolean ready() {
			return !this.origin.ready();
		}
	}
}
