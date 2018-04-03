package com.tencent.tsf.dubbo.hystrix;

import com.alibaba.dubbo.rpc.Result;

/**
 * Created by zhipeng on 2018/3/23. 
 */
public interface CircuitBreaker {
    Result circuitBreak();
}