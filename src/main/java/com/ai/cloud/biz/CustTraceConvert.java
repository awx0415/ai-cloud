package com.ai.cloud.biz;

import com.ai.cloud.UserTraceTL;
import com.ai.cloud.HBaseClient;
import com.ai.cloud.bean.FormatInfo;
import com.ai.common.util.DateHelper;
import com.ai.common.util.FileHelper;
import com.ai.common.util.MD5Helper;
import com.ai.common.util.StringHelper;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @desc 客户交互数据入库
 * @author anwx
 * @date 2017年3月6日 下午3:08:37
 */
public class CustTraceConvert implements ParallelConvert
{
    private final static Logger LOGGER = LoggerFactory.getLogger(CustTraceConvert.class);

    public void deal() throws Exception
    {
        long start = System.currentTimeMillis();
        FormatInfo formatInfo = UserTraceTL.FORMAT_INFO;
        List<File> listFiles = FileHelper.getListFiles(formatInfo.getSrcFilePath(), formatInfo.getBatchNo());
        if (listFiles.isEmpty())
        {
//            LOGGER.info("目录[{}]没有清单文件更新，继续监听...", formatInfo.getSrcFilePath());
            return;
        }

        int count = listFiles.size();
        int inCount = 0;
        for (int i = 0; i < count; i++)
        {
            Map<String, List<Put>> putListMap = new HashMap<String, List<Put>>();//key为表名，value为当前表要导入的数据
            File file = listFiles.get(i);
            // 获取文件名及文件内容
            String fileName = file.getName();
            /*暂时屏蔽
            if (filterFile(file, formatInfo))
            {
                continue;
            }
            */
            if(UserTraceTL.fileResolveImplInstance.fileCheck(file, formatInfo))
            {
                continue;
            }

            LOGGER.info("start read file [{}]", fileName);
            List<Map> infos = UserTraceTL.fileResolveImplInstance.resolve(file);
            for(Map info : infos)
            {
                List<Put> putList = new ArrayList<Put>();
                String serialNumber = (String)info.get("SERIAL_NUMBER");
                String starttime = (String)info.get("START_TIME");
                //rowkey是手机号码后三位_手机号码_操作时间_UUID
                int snLength = serialNumber.length();
                String rowKey = serialNumber.substring(snLength-3,snLength) + "_" + serialNumber + "_" + starttime + "_" + UUID.randomUUID();

                //拼表名
                String month = DateHelper.getMonth(starttime);
                String tableName = "USER_TRACE_" + month;

                if(putListMap.containsKey(tableName)){
                    putList = putListMap.get(tableName);
                }

                Put put = HBaseClient.bulidPut(rowKey, info, formatInfo); // 构造入hbase库的put对象
                putList.add(put);
                putListMap.put(tableName, putList);
//                if(true)
//                {
//                    if (HBaseClient.putHbase(tableName, putList))
//                    {
//                        FileHelper.bakSrcFile(bakFileList, formatInfo);
//                    }
//                }

//                lastTabName = tableName;
            }

            //入库及文件备份
            Set set =  putListMap.keySet();
            Iterator iter = set.iterator();
            while(iter.hasNext())
            {
                String tableName = (String)iter.next();
                List<Put> putList = putListMap.get(tableName);
                if(HBaseClient.putHbase(tableName, putList)){
                    FileHelper.bakSrcFile(file, formatInfo);
                    inCount += putList.size();
                }
            }
        }

        LOGGER.info("本次共处理[{}]个文件,入库条数为[{}],耗时[{}]毫秒", count, inCount, (System.currentTimeMillis() - start));
    }

    /**
     * @desc 过滤文件
     * @param file
     * @param formatInfo
     * @return
     * @author wangjw6
     * @throws IOException
     * @date 2017年3月7日 下午3:15:01
     */
    private boolean filterFile(File file, FormatInfo formatInfo) throws IOException
    {
        boolean flag = false;
        String fileName = file.getName();
        if (isTmp(fileName)
                || !(fileName.startsWith("bomc") && fileName.endsWith("dat")))
        {
            LOGGER.error("文件[{}]不符合入库条件", file.getAbsolutePath());
            FileHelper.bakFile(file, formatInfo.getErrorOutPath()); // 备份到错误目录
            flag = true;
        }
        return flag;
    }

    /**
     * @desc 判断是否是临时文件
     * @param fileName
     * @return
     * @author wangjw6
     * @date 2017年3月7日 下午3:20:16
     */
    private boolean isTmp(String fileName)
    {
        return (StringHelper.startsWith(fileName, "temp") || StringHelper.endsWith(fileName, "temp"));
    }

    /**
     * @desc 获取RowKey
     * @param fileName
     * @return
     * @author wangjw6
     * @date 2017年3月7日 下午3:21:12
     */
    private String getRowKey(String fileName)
    {
        String id = StringHelper.substring(fileName, 8, -4);
        return MD5Helper.getRowKey(id);
    }

}
