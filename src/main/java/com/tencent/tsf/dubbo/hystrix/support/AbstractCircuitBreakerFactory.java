package com.tencent.tsf.dubbo.hystrix.support;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.tencent.tsf.dubbo.hystrix.CircuitBreaker;
import com.tencent.tsf.dubbo.hystrix.CircuitBreakerFactory;

/**
 * Created by zhipeng on 2018/3/23. 
 */
public abstract class AbstractCircuitBreakerFactory implements CircuitBreakerFactory {
    @Override
    public CircuitBreaker getCircuitBreaker(Invoker<?> invoker, Invocation invocation) {
        return createCircuitBreaker(invoker, invocation);
    }

    protected abstract CircuitBreaker createCircuitBreaker(Invoker<?> invoker, Invocation invocation);
}