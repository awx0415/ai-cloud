package com.ai.common.cache;

/**
 * @desc 缓存提供
 * @param <T>
 * @author wangjw6
 * @date 2016-7-12 上午10:08:33
 */
public interface ICacheSourceProvider<T>
{
    public T getSource() throws Exception;
}