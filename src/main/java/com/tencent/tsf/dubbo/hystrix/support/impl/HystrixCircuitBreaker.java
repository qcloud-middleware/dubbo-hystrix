package com.tencent.tsf.dubbo.hystrix.support.impl;

import org.apache.commons.lang.StringUtils;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.tencent.tsf.dubbo.hystrix.CircuitBreaker;
import com.tencent.tsf.dubbo.hystrix.constants.HystrixConfigProperites;
import com.tencent.tsf.dubbo.hystrix.utils.ConfigurationManager;
import com.tencent.tsf.dubbo.hystrix.utils.RpcContextUtil;

/**
 * Created by zhipeng on 2018/3/23. 
 */
public class HystrixCircuitBreaker extends HystrixCommand<Result> implements CircuitBreaker {
	private Invoker<?> invoker;
	private Invocation invocation;
	/**
	 * 对象从线程逃逸出来,使dubbo与hystrix线程都可见
	 */
	private RpcContext sharedRpcContext;
	
	private static ConfigurationManager configurationManager = new ConfigurationManager("hystrix-circuit-breaker.properties");
	
	public HystrixCircuitBreaker(Invoker<?> invoker, Invocation invocation) {
		super(buildSetter(invoker, invocation));
		this.invoker = invoker;
		this.invocation = invocation;
	}
	
	@Override
	protected Result getFallback() {
		Throwable throwable = new RpcException("Hystrix fallback", getExecutionException());
		return new RpcResult(throwable);
	}

	@Override
	public Result circuitBreak() {//处于dubbo thread
		setSharedRpcContext();
		return super.execute();
	}

	@Override
	protected Result run() throws Exception {//处于hystrix thread
		RpcContextUtil.setValueInThreadLocal(sharedRpcContext);
		return invoker.invoke(invocation);
	}

	private static String buildHystrixCommandKey(Invocation invocation) {
		return String.format("%s_%d", invocation.getMethodName(), invocation.getArguments() == null ? 0	: invocation.getArguments().length);
	}
	
	private static String buildHystrixCommandGroupKey(Invoker<?> invoker) {
		return invoker.getInterface().getName();
	}
	
	private static int getThreadPoolCoreSize(URL url) {
		return url != null ? url.getParameter("ThreadPoolCoreSize", HystrixConfigProperites.DEFAULT_THREADPOOL_CORE_SIZE) : HystrixConfigProperites.DEFAULT_THREADPOOL_CORE_SIZE;
	}

	private void setSharedRpcContext() {
		this.sharedRpcContext = RpcContext.getContext();
	}
	
	private static String getConfigValue(String defaultkey, String commandKey) {
		if(StringUtils.isNotBlank(configurationManager.getProperty(commandKey)))
			return configurationManager.getProperty(commandKey);
		return configurationManager.getProperty(defaultkey);
	}
	
	private static String formatCommandKey(String commandKey, String placeholder) {
		return String.format(commandKey, placeholder);
	}

	private static Setter buildSetter(Invoker<?> invoker, Invocation invocation) {
		String hystrixCommandKey = buildHystrixCommandKey(invocation);
		
		Setter commandSetter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(buildHystrixCommandGroupKey(invoker))).andCommandKey(HystrixCommandKey.Factory.asKey(hystrixCommandKey));
		
		HystrixCommandProperties.Setter commandPropertiesSetter = HystrixCommandProperties.Setter();
		HystrixThreadPoolProperties.Setter threadPoolPropertiesSetter = HystrixThreadPoolProperties.Setter().withCoreSize(getThreadPoolCoreSize(invoker.getUrl()));//设置核心线程池大小;默认值:10
		
		String value;
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, formatCommandKey(HystrixConfigProperites.COMMANDKEY_CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, hystrixCommandKey))))
			commandPropertiesSetter.withCircuitBreakerRequestVolumeThreshold(Integer.parseInt(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS, formatCommandKey(HystrixConfigProperites.COMMANDKEY_CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS, hystrixCommandKey))))
			commandPropertiesSetter.withCircuitBreakerSleepWindowInMilliseconds(Integer.parseInt(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, formatCommandKey(HystrixConfigProperites.COMMANDKEY_CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, hystrixCommandKey))))
			commandPropertiesSetter.withCircuitBreakerErrorThresholdPercentage(Integer.parseInt(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_EXECUTION_TIMEOUT_ENABLED, formatCommandKey(HystrixConfigProperites.COMMANDKEY_EXECUTION_TIMEOUT_ENABLED, hystrixCommandKey))))
			commandPropertiesSetter.withExecutionTimeoutEnabled(Boolean.parseBoolean(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_CIRCUIT_BREAKER_ENABLED, formatCommandKey(HystrixConfigProperites.COMMANDKEY_CIRCUIT_BREAKER_ENABLED, hystrixCommandKey))))
			commandPropertiesSetter.withCircuitBreakerEnabled(Boolean.parseBoolean(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_CIRCUIT_BREAKER_FORCE_CLOSED, formatCommandKey(HystrixConfigProperites.COMMANDKEY_CIRCUIT_BREAKER_FORCE_CLOSED, hystrixCommandKey))))
			commandPropertiesSetter.withCircuitBreakerForceClosed(Boolean.parseBoolean(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_CIRCUIT_BREAKER_FORCE_OPEN, formatCommandKey(HystrixConfigProperites.COMMANDKEY_CIRCUIT_BREAKER_FORCE_OPEN, hystrixCommandKey))))
			commandPropertiesSetter.withCircuitBreakerForceOpen(Boolean.parseBoolean(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS, formatCommandKey(HystrixConfigProperites.COMMANDKEY_EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS, hystrixCommandKey))))
			commandPropertiesSetter.withExecutionIsolationSemaphoreMaxConcurrentRequests(Integer.parseInt(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_EXECUTION_ISOLATION_STRATEGY, formatCommandKey(HystrixConfigProperites.COMMANDKEY_EXECUTION_ISOLATION_STRATEGY, hystrixCommandKey))))
			commandPropertiesSetter.withExecutionIsolationStrategy(HystrixConfigProperites.EXECUTION_ISOLATION_STRATEGY_SEMAPHORE.equalsIgnoreCase(value) ? HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE : HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_TIMEOUT, formatCommandKey(HystrixConfigProperites.COMMANDKEY_EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_TIMEOUT, hystrixCommandKey))))
			commandPropertiesSetter.withExecutionIsolationThreadInterruptOnTimeout(Boolean.parseBoolean(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_FUTURE_CANCEL, formatCommandKey(HystrixConfigProperites.COMMANDKEY_EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_FUTURE_CANCEL, hystrixCommandKey))))
			commandPropertiesSetter.withExecutionIsolationThreadInterruptOnFutureCancel(Boolean.parseBoolean(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_EXECUTION_TIMEOUT_IN_MILLISECONDS, formatCommandKey(HystrixConfigProperites.COMMANDKEY_EXECUTION_TIMEOUT_IN_MILLISECONDS, hystrixCommandKey))))
			commandPropertiesSetter.withExecutionTimeoutInMilliseconds(Integer.parseInt(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_FALLBACK_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS, formatCommandKey(HystrixConfigProperites.COMMANDKEY_FALLBACK_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS, hystrixCommandKey))))
			commandPropertiesSetter.withFallbackIsolationSemaphoreMaxConcurrentRequests(Integer.parseInt(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_FALLBACK_ENABLED, formatCommandKey(HystrixConfigProperites.COMMANDKEY_FALLBACK_ENABLED, hystrixCommandKey))))
			commandPropertiesSetter.withFallbackEnabled(Boolean.parseBoolean(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_KEEP_ALIVE_TIME_MINUTES, formatCommandKey(HystrixConfigProperites.COMMANDKEY_KEEP_ALIVE_TIME_MINUTES, hystrixCommandKey))))
			threadPoolPropertiesSetter.withKeepAliveTimeMinutes(Integer.parseInt(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_MAX_QUEUE_SIZE, formatCommandKey(HystrixConfigProperites.DEFAULT_MAX_QUEUE_SIZE, hystrixCommandKey))))
			threadPoolPropertiesSetter.withMaxQueueSize(Integer.parseInt(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_QUEUE_SIZE_REJECTION_THRESHOLD, formatCommandKey(HystrixConfigProperites.COMMANDKEY_QUEUE_SIZE_REJECTION_THRESHOLD, hystrixCommandKey))))
			threadPoolPropertiesSetter.withQueueSizeRejectionThreshold(Integer.parseInt(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_ALLOW_MAXIMUM_SIZE_TO_DIVERGE_FROM_CORE_SIZE, formatCommandKey(HystrixConfigProperites.COMMANDKEY_ALLOW_MAXIMUM_SIZE_TO_DIVERGE_FROM_CORE_SIZE, hystrixCommandKey))))
			threadPoolPropertiesSetter.withAllowMaximumSizeToDivergeFromCoreSize(Boolean.parseBoolean(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS, formatCommandKey(HystrixConfigProperites.COMMANDKEY_ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS, hystrixCommandKey))))
			threadPoolPropertiesSetter.withMetricsRollingStatisticalWindowInMilliseconds(Integer.parseInt(value));
		if(StringUtils.isNotBlank(value = getConfigValue(HystrixConfigProperites.DEFAULT_ROLLING_STATISTICAL_WINDOW_BUCKETS, formatCommandKey(HystrixConfigProperites.DEFAULT_ROLLING_STATISTICAL_WINDOW_BUCKETS, hystrixCommandKey))))
			threadPoolPropertiesSetter.withMetricsRollingStatisticalWindowBuckets(Integer.parseInt(value));
		
		commandSetter.andCommandPropertiesDefaults(commandPropertiesSetter);
		commandSetter.andThreadPoolPropertiesDefaults(threadPoolPropertiesSetter);
		
		
		return commandSetter;
	}
}