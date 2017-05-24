package com.ai.cloud;

import com.ai.cloud.bean.FileResolve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.cloud.bean.FormatInfo;
import com.ai.cloud.bean.LoadFormat;
import com.ai.cloud.biz.ParallelConvert;
import com.ai.common.util.ConfigHelper;
import com.ai.common.util.XmlHelper;
import com.ai.monitor.FileListener;
import com.ai.monitor.FileMonitor;
import com.google.common.base.Throwables;

public class UserTraceTL
{
    private final static Logger LOGGER = LoggerFactory.getLogger(UserTraceTL.class);

    public static FormatInfo FORMAT_INFO;
    public static ParallelConvert PARALLEL_CONVERT;
    public static FileResolve fileResolveImplInstance;

    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            System.out.println("Usage: load_file_name");
            System.exit(0);
        }
        String fileName = args[0];
        LOGGER.info("ETL进程正在启动...");
        UserTraceTL.init(fileName);
        try
        {
            FileMonitor fileMonitor = new FileMonitor(FORMAT_INFO.getIntervaLTime());
            fileMonitor.monitor(FORMAT_INFO.getSrcFilePath(), new FileListener());
            fileMonitor.start();
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("ETL进程启动结束...");
    }

    /**
     * @desc 初始化信息
     * @param fileName
     * @author wangjw6
     * @date 2017年3月6日 下午2:50:47
     */
    private static void init(String fileName)
    {
        LoadFormat format = XmlHelper.fromXML(ConfigHelper.getStream(fileName), LoadFormat.class);
        FORMAT_INFO = format.getFormatInfo();
        try
        {
            PARALLEL_CONVERT = (ParallelConvert) Class.forName(FORMAT_INFO.getParallelClass()).newInstance();
            fileResolveImplInstance = (FileResolve) Class.forName(FORMAT_INFO.getFileResolveImplClass()).newInstance();
            HBaseClient.connect(format); // 初始化HBase连接
            LOGGER.debug("初始化成功");
        }
        catch (Exception e)
        {
            LOGGER.debug("初始化失败");
            Throwables.propagate(e);
        }
    }

}
