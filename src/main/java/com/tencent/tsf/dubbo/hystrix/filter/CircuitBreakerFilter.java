package com.tencent.tsf.dubbo.hystrix.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.tencent.tsf.dubbo.hystrix.CircuitBreaker;
import com.tencent.tsf.dubbo.hystrix.CircuitBreakerFactory;

/**
 * Created by zhipeng on 2018/3/23. 
 */
@Activate(group = Constants.CONSUMER)
public class CircuitBreakerFilter implements Filter {
	private CircuitBreakerFactory circuitBreakerFactory;

	public void setCircuitBreakerFactory(CircuitBreakerFactory circuitBreakerFactory) {
		this.circuitBreakerFactory = circuitBreakerFactory;
	}

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		if (circuitBreakerFactory != null) {
			CircuitBreaker circuitBreaker = circuitBreakerFactory.getCircuitBreaker(invoker, invocation);
			if (circuitBreaker != null) {
				return circuitBreaker.circuitBreak();
			}
		}
		return invoker.invoke(invocation);
	}
}