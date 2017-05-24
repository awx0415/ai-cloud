package com.ai.common.util;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc 配置文件加载
 * @author wangjw6
 * @date 2016-7-16 下午1:09:32
 */
public final class ConfigHelper
{
    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigHelper.class);

    private ConfigHelper()
    {

    }

    public static InputStream getStream(String fileName)
    {
        LOGGER.info("classpath:[{}]", fileName);
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
}
