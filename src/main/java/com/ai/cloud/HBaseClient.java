package com.ai.cloud;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.cloud.bean.FormatInfo;
import com.ai.cloud.bean.LoadFormat;
import com.ai.common.Constant;
import com.ai.common.util.StringHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

/**
 * @desc 创建Hbase连接
 * @author wangjw6
 * @date 2017年3月1日 下午3:44:21
 */
public class HBaseClient
{
    private final static Logger LOGGER = LoggerFactory.getLogger(HBaseClient.class);

    private static Configuration conf;

    private static HConnection HCONNECTION = null;

    private HBaseClient()
    {

    }

    /**
     * @desc 连接参数设置
     * @author wangjw6
     * @throws IOException
     * @date 2017年3月1日 下午4:05:55
     */
    public static void connect(LoadFormat loadFormat) throws IOException
    {
        LOGGER.debug("start connect cloud...");
        String hosts = loadFormat.getZkQuorum(); // zookeeper主机名称，多个主机名称以,号分隔开
        String port = loadFormat.getZkClientPort(); // 客户端端口号
        Preconditions.checkNotNull(hosts, "%s IS NULL", Constant.HOSTS);
        Preconditions.checkNotNull(port, "%s IS NULL", Constant.PORT);

        conf = HBaseConfiguration.create();
        // 用客户端的配置覆盖hbase的配置
        conf.set(Constant.HOSTS, hosts);
        conf.set(Constant.PORT, port);
        // 配置Hadoop URL
//        conf.set(Constant.HDFS_URL, loadFormat.getDefaultFS());
        conf.setLong(Constant.HBASE_RPC_TIMEOUT, loadFormat.getRpcTimeOut());
        HCONNECTION = HConnectionManager.createConnection(conf);
        LOGGER.debug("connect cloud success");
    }

    /**
     * @desc 构建Put对象
     * @param rowkey
     * @param info
     * @param formatInfo
     * @return
     * @author wangjw6
     * @date 2017年3月6日 下午5:34:19
     */
    public static Put bulidPut(String rowkey, Map info, FormatInfo formatInfo)
    {
        Put put = new Put(rowkey.getBytes());
        // 设置写WAL （Write-Ahead-Log）的级别 ASYNC_WAL:当数据变动时，异步写WAL日志
        // SYNC_WAL:当数据变动时，同步写WAL日志; FSYNC_WAL:当数据变动时，同步写WAL日志，并且，强制将数据写入磁盘
        // SKIP_WAL:不写WAL日志; USE_DEFAULT:使用HBase全局默认的WAL写入级别，即 SYNC_WAL;
        put.setDurability(Durability.valueOf(formatInfo.getDurability()));

        String[] columns = StringHelper.splitPreserveAllTokens(formatInfo.getColumn(), formatInfo.getSeparator());
        String columnFamily = formatInfo.getColumnFamily();
        for (int i = 0, iLen = columns.length; i < iLen; i++)
        {
            String column = columns[i];
            String columnValue = (String)info.get(column);
            if (StringUtils.isNotBlank(columnValue))
            {
                put.add(columnFamily.getBytes(), columns[i].getBytes(), ((String)info.get(column)).getBytes());
            }
        }
        return put;
    }

    /**
     * @desc 分账期构造putList
     * @param put
     * @param tabName
     * @param putListMap
     * @author wangjw6
     * @date 2017年3月2日 下午2:41:51
     */
    public static void structPutList(Put put, String tabName, Map<String, List<Put>> putListMap)
    {
        if (!putListMap.containsKey(tabName))
        {
            List<Put> list = Lists.newArrayList();
            list.add(put);
            putListMap.put(tabName, list);
        }
        else
        {
            putListMap.get(tabName).add(put);
        }
    }

    /**
     * @desc 将List保存到Hbase
     * @param tableName
     * @param list
     * @return
     * @author wangjw6
     * @date 2017年3月6日 下午6:38:37
     */
    public static boolean putHbase(String tableName, List<Put> list)
    {
        boolean flag = false;
        try
        {
            HTableInterface table = HCONNECTION.getTable(tableName);
            table.setAutoFlush(false, false);
            // table.setAutoFlush(true);
            // table.setWriteBufferSize(6*1024*1024);
            table.put(list);
            table.flushCommits();
            table.close();
            LOGGER.info("put hbase [{}],record size[{}] success", tableName, list.size());
            flag = true;
        }
        catch (Exception e)
        {
            LOGGER.error("put hbase [{}],record size[{}] faliure", tableName, list.size());
            Throwables.propagate(e);
        }
        return flag;
    }
}
