package com.age5k.jcps.framework.lifecycle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorUtil {

	private static Logger LOG = LoggerFactory.getLogger(ExecutorUtil.class);

	private static class MyTF implements ThreadFactory, Thread.UncaughtExceptionHandler {
		private static AtomicInteger nextNumber = new AtomicInteger();

		String group;

		public MyTF(String group) {
			this.group = group;
		}

		@Override
		public Thread newThread(Runnable r) {

			Thread rt = new Thread(r, "MyThread-" + group + "-" + nextNumber.incrementAndGet());
			rt.setUncaughtExceptionHandler(this);
			return rt;
		}

		@Override
		public void uncaughtException(Thread thread, Throwable throwable) {
			LOG.error("uncaughtException in thread:" + thread.getName(), throwable);
		}

	}

	private static Map<String, MyTF> TFMAP = new HashMap<>();

	private static MyTF getTF(String group) {
		MyTF rt = TFMAP.get(group);
		if (rt == null) {
			synchronized (TFMAP) {
				rt = TFMAP.get(group);
				if (rt == null) {
					rt = new MyTF(group);
					TFMAP.put(group, rt);
				}
			}
		}
		return rt;
	}

	public static ExecutorService newSingleThreadExecutor(String group) {

		return Executors.newSingleThreadExecutor(getTF(group));
	}

	public static ScheduledExecutorService newScheduledThreadPool(String group, int corePoolSize) {

		return Executors.newScheduledThreadPool(corePoolSize, getTF(group));
	}
}
