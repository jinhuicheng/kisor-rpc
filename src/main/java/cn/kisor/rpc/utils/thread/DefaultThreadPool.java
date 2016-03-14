package cn.kisor.rpc.utils.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadPool {
	// private static final Logger LOGGER = LoggerFactory.getLogger(TaskExcutor.class);
	private static final Map<ThreadPoolName, ThreadPoolExecutor> threadPoolExecutors = new HashMap<ThreadPoolName, ThreadPoolExecutor>();

	public static enum ThreadPoolName {
		bizThreadPool;
		static Map<String, ThreadPoolName> data = new HashMap<String, DefaultThreadPool.ThreadPoolName>();
		static {
			for (ThreadPoolName poolName : values()) {
				data.put(poolName.name(), poolName);
			}
		}

		public static ThreadPoolName getThreadPoolName(String key) {
			return data.get(key);
		}
	}

	protected static class KisorThreadFactory implements ThreadFactory {
		static final AtomicInteger poolNumber = new AtomicInteger(1);
		final ThreadGroup group;
		final AtomicInteger threadNumber = new AtomicInteger(1);
		final String namePrefix;

		KisorThreadFactory(String name) {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = name + "-thread-" + poolNumber.getAndIncrement();
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
	static {
		threadPoolExecutors.put(ThreadPoolName.bizThreadPool, new ThreadPoolExecutor(50, 100, 5l, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new KisorThreadFactory(
				ThreadPoolName.bizThreadPool.name()), new ThreadPoolExecutor.CallerRunsPolicy()));
		// Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
		// @Override
		// public void run() {
		// for (Map.Entry<ThreadPoolName, ThreadPoolExecutor> entry : threadPoolExecutors.entrySet()) {
		// ThreadPoolExecutor executor = entry.getValue();
		// LOGGER.info(String.format(
		// "ThreadPool-%s activeCount:%s,completedTaskCount:%s,corePoolSize:%s,aliveTime:%s,largestPoolSize:%s,maximumPoolSize:%s,poolSize:%s,taskCount:%s,queue.size:%s",
		// entry.getKey(), executor.getActiveCount(), executor.getCompletedTaskCount(), executor.getCorePoolSize(), executor.getKeepAliveTime(TimeUnit.SECONDS),
		// executor.getLargestPoolSize(), executor.getMaximumPoolSize(), executor.getPoolSize(), executor.getTaskCount(), executor.getQueue().size()));
		// }
		// }
		// }, 1, 60, TimeUnit.SECONDS);

	}

	public static ThreadPoolExecutor getThreadPoolExecutor(ThreadPoolName threadPoolName) {
		return threadPoolExecutors.get(threadPoolName);
	}

	public static void execute(ThreadPoolName threadPoolName, Runnable r) {
		if (!threadPoolExecutors.containsKey(threadPoolName)) {
			throw new IllegalArgumentException("未配置线程池" + threadPoolName);
		}
		threadPoolExecutors.get(threadPoolName).execute(r);
	}
}
