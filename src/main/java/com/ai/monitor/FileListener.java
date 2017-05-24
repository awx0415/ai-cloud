package com.ai.monitor;

import java.io.File;

import com.ai.cloud.UserTraceTL;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc 文件变化监听器
 * @author wangjw6
 * @date 2017年3月1日 下午4:34:09
 */
public class FileListener implements FileAlterationListener
{
    private final static Logger LOGGER = LoggerFactory.getLogger(FileListener.class);

    public void onStart(FileAlterationObserver observer)
    {
        long start = System.currentTimeMillis();
        String filePath = observer.getDirectory().getAbsolutePath();
//        LOGGER.debug("文件监听开始[{}]", filePath);
        try
        {
//            LOGGER.debug("执行类PARALLEL_CONVERT:" + UserTraceTL.PARALLEL_CONVERT.getClass().getName());
            UserTraceTL.PARALLEL_CONVERT.deal();
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
//        LOGGER.info("耗时[{}]mm", (System.currentTimeMillis() - start));
    }

    public void onDirectoryCreate(File directory)
    {
        // LOGGER.info("");
    }

    public void onDirectoryChange(File directory)
    {
        // LOGGER.info("");
    }

    public void onDirectoryDelete(File directory)
    {
        // LOGGER.info("");
    }

    public void onFileCreate(File file)
    {
        // LOGGER.info("");
    }

    public void onFileChange(File file)
    {
        // LOGGER.info("");
    }

    public void onFileDelete(File file)
    {
        // LOGGER.info("");
    }

    public void onStop(FileAlterationObserver observer)
    {
        // LOGGER.info("");
    }

}
