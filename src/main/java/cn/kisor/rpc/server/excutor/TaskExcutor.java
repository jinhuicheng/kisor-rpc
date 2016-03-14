package cn.kisor.rpc.server.excutor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kisor.rpc.component.DefaultKisorContext.RequestRegister;
import cn.kisor.rpc.component.request.MessageRequest;
import cn.kisor.rpc.component.request.impl.KisorMessageRequest;
import cn.kisor.rpc.component.response.MessageResponse;
import cn.kisor.rpc.component.response.impl.KisorMessageResponse;
import cn.kisor.rpc.filter.FilterChain;
import cn.kisor.rpc.utils.thread.DefaultThreadPool;
import cn.kisor.rpc.utils.thread.DefaultThreadPool.ThreadPoolName;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年1月15日 上午3:39:42
 * @description
 */
public class TaskExcutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskExcutor.class);

	// private final static CompletionService<Object> completionService = new ExecutorCompletionService<Object>(DefaultThreadPool.getThreadPoolExecutor(ThreadPoolName.bizThreadPool));

	public static MessageResponse excute(final MessageRequest messageRequest, final MessageResponse messageResponse, final FilterChain taskChain, final long timeout, final TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		Future<MessageResponse> future = DefaultThreadPool.getThreadPoolExecutor(ThreadPoolName.bizThreadPool).submit(new MessageRequestCallable(messageResponse, taskChain, messageRequest));
		return getResult(future, timeout, unit);
	}

	public static MessageResponse excute(final KisorMessageRequest kisorRequest, final KisorMessageResponse kisorResponse, final FilterChain taskChain) throws InterruptedException,
			ExecutionException,
			TimeoutException {
		Future<MessageResponse> future = DefaultThreadPool.getThreadPoolExecutor(ThreadPoolName.bizThreadPool).submit(new MessageRequestCallable(kisorResponse, taskChain, kisorRequest));
		return getResult(future);
	}

	public static <T> T getResult(Future<T> future, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		if (future == null) {
			return null;
		}
		T object = null;
		try {
			object = future.get(timeout, unit);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			Thread.currentThread().interrupt();
			future.cancel(true);
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
		return object;
	}

	public static <T> T getResult(Future<T> future) throws InterruptedException, ExecutionException {
		if (future == null) {
			return null;
		}
		T object = null;
		try {
			object = future.get();
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			future.cancel(true);
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
		return object;
	}

	private static final class MessageRequestCallable implements Callable<MessageResponse> {
		private final MessageResponse messageResponse;
		private final FilterChain taskChain;
		private final MessageRequest messageRequest;

		protected MessageRequestCallable(MessageResponse messageResponse, FilterChain taskChain, MessageRequest messageRequest) {
			this.messageResponse = messageResponse;
			this.taskChain = taskChain;
			this.messageRequest = messageRequest;
		}

		@Override
		public MessageResponse call() throws Exception {
			if (messageRequest != null) {
				RequestRegister.setMessageRequest(messageRequest);
			}
			try {
				taskChain.doFilter(messageRequest, messageResponse, taskChain);
				return messageResponse;
			} catch (Exception e) {
				LOGGER.error("多线程执行错误", e);
				throw e;
			} finally {
				if (messageRequest != null) {
					RequestRegister.remove();
				}
			}
		}
	}
}
