package com.ai.common.cache;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

/**
 * @desc 缓存工具类
 * @author wangjw6
 * @date 2017年3月1日 上午11:11:00
 */
public final class CacheHelper
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheHelper.class);

    private CacheHelper()
    {

    }

    /**
     * @desc 缓存信息获取
     * @param cacheMap
     * @param cacheKey
     * @param provider
     * @return
     * @author wangjw6
     * @date 2017年3月1日 上午11:10:33
     */
    public static <K, V> V get(Map<K, V> cacheMap, K cacheKey, ICacheSourceProvider<V> provider)
    {
        V value = null;
        try
        {
            if (cacheMap != null && cacheMap.containsKey(cacheKey))
            {
                LOGGER.debug("get from local map cache-key [{}] ", cacheKey);
                return (V) cacheMap.get(cacheKey);
            }
            else
            {
                if (provider == null)
                {
                    return null;
                }
                value = provider.getSource();

                LOGGER.debug("set cache-key[{}] from source data", cacheKey);

                if (cacheMap != null && value != null)
                {
                    cacheMap.put(cacheKey, value);
                }
            }
        }
        catch (Exception e)
        {
            Throwables.propagate(e);
        }
        return value;
    }

}
