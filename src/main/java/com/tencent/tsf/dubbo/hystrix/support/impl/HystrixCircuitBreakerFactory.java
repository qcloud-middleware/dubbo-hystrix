package com.tencent.tsf.dubbo.hystrix.support.impl;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.tencent.tsf.dubbo.hystrix.CircuitBreaker;
import com.tencent.tsf.dubbo.hystrix.support.AbstractCircuitBreakerFactory;

/**
 * Created by zhipeng on 2018/3/23. 
 */
public class HystrixCircuitBreakerFactory extends AbstractCircuitBreakerFactory {
    @Override
    protected CircuitBreaker createCircuitBreaker(Invoker<?> invoker, Invocation invocation) {
        return new HystrixCircuitBreaker(invoker, invocation);
    }
}