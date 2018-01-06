package com.age5k.jcps.framework.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ExecutorBasedServer implements Server {

	protected ExecutorService executor;

	@Override
	public void start() {
		this.executor = this.newExecutor();
	}

	protected abstract ExecutorService newExecutor();

	@Override
	public void shutdown() {
		this.executor.shutdown();
		try {
			this.executor.awaitTermination(100, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		}
	}

}
