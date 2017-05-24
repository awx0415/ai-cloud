package com.ai.monitor;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**   
 * @desc 文件监控类
 * @author wangjw6
 * @date 2017年3月1日 下午4:33:24
 */
public class FileMonitor
{
    FileAlterationMonitor monitor = null;

    public FileMonitor(long interval) throws Exception
    {
        monitor = new FileAlterationMonitor(interval);
    }

    public void monitor(String path, FileAlterationListener listener)
    {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);
        observer.addListener(listener);
    }

    public void stop() throws Exception
    {
        monitor.stop();
    }

    public void start() throws Exception
    {
        monitor.start();
    }
}
