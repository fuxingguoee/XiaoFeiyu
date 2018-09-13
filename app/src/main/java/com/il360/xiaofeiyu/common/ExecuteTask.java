package com.il360.xiaofeiyu.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecuteTask {
	public static final int count = Runtime.getRuntime().availableProcessors() * 3 + 2;
	public static ExecutorService executorService = Executors.newFixedThreadPool(count);
	
	public static void execute(Runnable task) {
		executorService.submit(task);
	}

}
