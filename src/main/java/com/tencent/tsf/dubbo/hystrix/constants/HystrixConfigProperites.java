package com.tencent.tsf.dubbo.hystrix.constants;

/**
 * Created by zhipeng on 2018/3/23. 
 */
public interface HystrixConfigProperites {
	/**
	 * 设置在一个滚动窗口中,打开断路器的最少请求数;默认值:20
	 */
	public static final String DEFAULT_CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD = "hystrix.command.default.circuitBreaker.requestVolumeThreshold";//默认属性
	public static final String COMMANDKEY_CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD = "hystrix.command.%s.circuitBreaker.requestVolumeThreshold";//实例属性

	/**
	 * 设置在回路被打开,拒绝请求到再次尝试请求并决定回路是否继续打开的时间;默认值:5000(毫秒)
	 */
	public static final String DEFAULT_CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS = "hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds";
	public static final String COMMANDKEY_CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS = "hystrix.command.%s.circuitBreaker.sleepWindowInMilliseconds";

	/**
	 * 设置打开回路并启动回退逻辑的错误比率;默认值:50
	 */
	public static final String DEFAULT_CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE = "hystrix.command.default.circuitBreaker.errorThresholdPercentage";
	public static final String COMMANDKEY_CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE = "hystrix.command.%s.circuitBreaker.errorThresholdPercentage";

	/**
	 * 是否开启超时时间中断抛出异常的功能 ;默认值:true
	 */
	public static final String DEFAULT_EXECUTION_TIMEOUT_ENABLED = "hystrix.command.default.execution.timeout.enabled";
	public static final String COMMANDKEY_EXECUTION_TIMEOUT_ENABLED = "hystrix.command.%s.execution.timeout.enabled";
	
	/**
	 * 设置断路器是否起作用;默认值:true
	 */
	public static final String DEFAULT_CIRCUIT_BREAKER_ENABLED = "hystrix.command.default.circuitBreaker.enabled";
	public static final String COMMANDKEY_CIRCUIT_BREAKER_ENABLED = "hystrix.command.%s.circuitBreaker.enabled";
	
	
	/**
	 * 如果该属性设置为true,强制断路器进入关闭状态,将会允许所有的请求,无视错误率;默认值:false
	 */
	public static final String DEFAULT_CIRCUIT_BREAKER_FORCE_CLOSED = "hystrix.command.default.circuitBreaker.forceClosed";
	public static final String COMMANDKEY_CIRCUIT_BREAKER_FORCE_CLOSED = "hystrix.command.%s.circuitBreaker.forceClosed";
	
	
	/**
	 * 如果该属性设置为true,强制断路器进入打开状态,将会拒绝所有的请求;该属性优先级比circuitBreaker.forceClosed高;默认值:false
	 */
	public static final String DEFAULT_CIRCUIT_BREAKER_FORCE_OPEN = "hystrix.command.default.circuitBreaker.forceOpen";
	public static final String COMMANDKEY_CIRCUIT_BREAKER_FORCE_OPEN = "hystrix.command.%s.circuitBreaker.forceOpen";
	
	
	/**
	 *设置当使用ExecutionIsolationStrategy.SEMAPHORE(信号隔离)时,HystrixCommand.run()方法允许的最大请求数.
	 *如果达到最大并发数时,后续请求会被拒绝.信号量应该是容器(比如Tomcat)线程池一小部分,不能等于或者略小于容器线程池大小,否则起不到保护作用.默认值:10
	 */
	public static final String DEFAULT_EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS = "hystrix.command.default.execution.isolation.semaphore.maxConcurrentRequests";
	public static final String COMMANDKEY_EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS = "hystrix.command.%s.execution.isolation.semaphore.maxConcurrentRequests";
	
	
	/**
	 * 隔离策略:线程 || 信号量;默认值:线程
	 */
	public static final String DEFAULT_EXECUTION_ISOLATION_STRATEGY = "hystrix.command.default.execution.isolation.strategy";
	public static final String COMMANDKEY_EXECUTION_ISOLATION_STRATEGY = "hystrix.command.%s.execution.isolation.strategy";
	
	
	/**
	 * 设置HystrixCommand.run()的执行是否在超时发生时被中断;默认值:true
	 */
	public static final String DEFAULT_EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_TIMEOUT = "hystrix.command.default.execution.isolation.thread.interruptOnTimeout";
	public static final String COMMANDKEY_EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_TIMEOUT = "hystrix.command.%s.execution.isolation.thread.interruptOnTimeout";
	
	
	/**
	 * 当隔离策略为THREAD时,当执行线程执行超时时,是否进行中断处理,即Future#cancel(true)处理,默认为false.
	 */
	public static final String DEFAULT_EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_FUTURE_CANCEL = "hystrix.command.default.execution.isolation.thread.interruptOnCancel";
	public static final String COMMANDKEY_EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_FUTURE_CANCEL = "hystrix.command.%s.execution.isolation.thread.interruptOnCancel";
	
	
	/**
	 * 设置调用者等待命令执行的超时限制,超过此时间,HystrixCommand被标记为TIMEOUT,并执行回退逻辑;默认值:1000(毫秒)
	 */
	public static final String DEFAULT_EXECUTION_TIMEOUT_IN_MILLISECONDS = "hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds";
	public static final String COMMANDKEY_EXECUTION_TIMEOUT_IN_MILLISECONDS = "hystrix.command.%s.execution.isolation.thread.timeoutInMilliseconds";
	
	
	
	/**
	 * 设置调用线程产生的HystrixCommand.getFallback()方法的允许最大请求数目.如果达到最大并发数目,后续请求将会被拒绝,如果没有实现回退,则抛出异常.默认值:10;
	 * 对THREAD和SEMAPHORE两种隔离策略都生效
	 */
	public static final String DEFAULT_FALLBACK_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS = "hystrix.command.default.fallback.isolation.semaphore.maxConcurrentRequests";
	public static final String COMMANDKEY_FALLBACK_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS = "hystrix.command.%s.fallback.isolation.semaphore.maxConcurrentRequests";
	
	
	
	/**
	 * 该属性决定当故障或者拒绝发生时,一个调用将会去尝试HystrixCommand.getFallback().默认值:true
	 */
	public static final String DEFAULT_FALLBACK_ENABLED = "hystrix.command.default.fallback.enabled";
	public static final String COMMANDKEY_FALLBACK_ENABLED = "hystrix.command.%s.fallback.enabled";
	
	
	/**
	 * 设置存活时间,单位分钟.如果coreSize小于maximumSize,那么该属性控制一个线程从实用完成到被释放的时间;默认值:1
	 */
	public static final String DEFAULT_KEEP_ALIVE_TIME_MINUTES = "hystrix.threadpool.default.keepAliveTimeMinutes";
	public static final String COMMANDKEY_KEEP_ALIVE_TIME_MINUTES = "hystrix.threadpool.%s.keepAliveTimeMinutes";
	
	
	/**
	 * 设置BlockingQueue最大的队列值.	如果设置为-1,那么使用SynchronousQueue,否则正数将会使用LinkedBlockingQueue.
	 * 如果需要去除这些限制,允许队列动态变化,可以参考queueSizeRejectionThreshold属性.修改SynchronousQueue和LinkedBlockingQueue需要重启.
	 * 默认值:-1
	 */
	public static final String DEFAULT_MAX_QUEUE_SIZE = "hystrix.threadpool.default.maxQueueSize";
	public static final String COMMANDKEY_MAX_QUEUE_SIZE = "hystrix.threadpool.%s.maxQueueSize";
	
	
	
	/**
	 * 设置队列拒绝的阈值—-一个人为设置的拒绝访问的最大队列值,即使当前队列元素还没达到maxQueueSize. 当将一个线程放入队列等待执行时,HystrixCommand使用该属性.
	 * 注意:如果maxQueueSize设置为-1,该属性不可用.
	 * 默认值:5
	 */
	public static final String DEFAULT_QUEUE_SIZE_REJECTION_THRESHOLD = "hystrix.threadpool.default.queueSizeRejectionThreshold";
	public static final String COMMANDKEY_QUEUE_SIZE_REJECTION_THRESHOLD = "hystrix.threadpool.%s.queueSizeRejectionThreshold";
	
	
	/**
	 * 该属性允许maximumSize起作用.属性值可以等于或者大于coreSize值,设置coreSize小于maximumSize的线程池能够支持maximumSize的并发数,但是会将不活跃的线程返回到系统中去.
	 *  默认值:false
	 */
	public static final String DEFAULT_ALLOW_MAXIMUM_SIZE_TO_DIVERGE_FROM_CORE_SIZE = "hystrix.threadpool.default.allowMaximumSizeToDivergeFromCoreSize";
	public static final String COMMANDKEY_ALLOW_MAXIMUM_SIZE_TO_DIVERGE_FROM_CORE_SIZE = "hystrix.threadpool.%s.allowMaximumSizeToDivergeFromCoreSize";
	
	
	/**
	 * 设置统计的滚动窗口的时间段大小.该属性是线程池保持指标时间长短;默认值:10000(毫秒)
	 */
	public static final String DEFAULT_ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS = "hystrix.threadpool.default.metrics.rollingStats.timeInMilliseconds";
	public static final String COMMANDKEY_ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS = "hystrix.threadpool.%s.metrics.rollingStats.timeInMilliseconds";
	
	
	/**
	 * 可统计的滚动窗口内的buckets数量,用于熔断器和指标发布.设置滚动的统计窗口被分成的桶(bucket)的数目.
	 * 注意: "metrics.rollingStats.timeInMilliseconds % metrics.rollingStats.numBuckets == 0"必须为true,否则会抛出异常.
	 * 默认值: 10
	 */
	public static final String DEFAULT_ROLLING_STATISTICAL_WINDOW_BUCKETS = "hystrix.threadpool.default.metrics.rollingStats.numBuckets";
	public static final String COMMANDKEY_ROLLING_STATISTICAL_WINDOW_BUCKETS = "hystrix.threadpool.%s.metrics.rollingStats.numBuckets";
	
	/**
	 * 线程池大小:10
	 */
	public static final int DEFAULT_THREADPOOL_CORE_SIZE = 10;
	
	public static final String EXECUTION_ISOLATION_STRATEGY_SEMAPHORE = "SEMAPHORE";
}