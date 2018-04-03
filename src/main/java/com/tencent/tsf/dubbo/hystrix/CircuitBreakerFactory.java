package com.tencent.tsf.dubbo.hystrix;

import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

/**
 * Created by zhipeng on 2018/3/23. 
 */
@SPI("hystrix")
public interface CircuitBreakerFactory {
	@Adaptive
	CircuitBreaker getCircuitBreaker(Invoker<?> invoker, Invocation invocation);
}