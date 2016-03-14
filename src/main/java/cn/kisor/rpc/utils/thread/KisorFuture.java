package cn.kisor.rpc.utils.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.kisor.rpc.exception.ClientTimeoutException;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年1月17日 下午2:56:30
 * @description
 */
public class KisorFuture<T> implements Future<T> {

	private volatile Object result;
	/** Future已发送时间 */
	private volatile long sentTime;
	/** Future生成时间 */
	private final long createTime = System.currentTimeMillis();
	private volatile boolean isCompleted = false;
	private short waiters;

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return isCompleted;
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		while (!this.isCompleted) {
			wait();
		}
		return getResult();
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		timeout = unit.toMillis(timeout); // 转为毫秒
		long remaintime = timeout - (sentTime - createTime); // 剩余时间
		if (remaintime <= 0) {
			if (this.isCompleted) {
				return getResult();
			}
		} else {
			if (await(remaintime, TimeUnit.MILLISECONDS)) {
				return getResult();
			}
		}
		throw new ClientTimeoutException();
	}

	public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
		return await0(unit.toNanos(timeout), true);
	}

	private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
		if (isDone()) {
			return true;
		}
		if (timeoutNanos <= 0) {
			return isDone();
		}
		if (interruptable && Thread.interrupted()) {
			throw new InterruptedException(toString());
		}
		long startTime = System.nanoTime();
		long waitTime = timeoutNanos;
		boolean interrupted = false;
		try {
			synchronized (this) {
				if (isDone()) {
					return true;
				}

				if (waitTime <= 0) {
					return isDone();
				}

				// checkDeadLock();
				incWaiters();
				try {
					for (;;) {
						try {
							wait(waitTime / 1000000, (int) (waitTime % 1000000));
						} catch (InterruptedException e) {
							if (interruptable) {
								throw e;
							} else {
								interrupted = true;
							}
						}

						if (isDone()) {
							return true;
						} else {
							waitTime = timeoutNanos - (System.nanoTime() - startTime);
							if (waitTime <= 0) {
								return isDone();
							}
						}
					}
				} finally {
					decWaiters();
				}
			}
		} finally {
			if (interrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private void decWaiters() {
		waiters--;
	}

	private void incWaiters() {
		if (waiters == Short.MAX_VALUE) {
			throw new IllegalStateException("too many waiters: " + this);
		}
		waiters++;
	}

	@SuppressWarnings("unchecked")
	private T getResult() {
		// if (result instanceof KisorResponse) {
		//
		// }
		return (T) result;
	}

	public KisorFuture<T> success(T result) throws InterruptedException {
		if (setSuccess0(result)) {
			// notifyListeners();
			return this;
		}
		throw new IllegalStateException("complete already: " + this);

	}

	private boolean setSuccess0(T result) {
		if (isDone()) {
			return false;
		}

		synchronized (this) {
			// Allow only once.
			if (isDone()) {
				return false;
			}
			if (this.result == null) {
				this.result = result;
				this.isCompleted = true;
			}
			if (hasWaiters()) {
				notifyAll();
			}
		}
		return true;
	}

	private boolean hasWaiters() {
		return waiters > 0;
	}

}
