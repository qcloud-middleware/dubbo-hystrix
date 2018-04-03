package com.tencent.tsf.dubbo.hystrix.utils;

import java.lang.reflect.Field;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * Created by zhipeng on 2018/3/23. 
 */
public class RpcContextUtil {
	/**
	 * 如果hystrix隔离策略是Thread,则需要将RpcContext对象从dubbo线程传递到hystrix线程
	 */
	@SuppressWarnings("unchecked")
	public static void setValueInThreadLocal(RpcContext value) {
		RpcContext currentThreadRpcContext = RpcContext.getContext();

		Field LOCAL;
		try {
			LOCAL = currentThreadRpcContext.getClass().getDeclaredField("LOCAL");

			LOCAL.setAccessible(true);

			ThreadLocal<RpcContext> threadLocal = (ThreadLocal<RpcContext>) LOCAL.get(currentThreadRpcContext);
			threadLocal.set(value);

			LOCAL.setAccessible(false);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}