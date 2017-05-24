package com.ai.cloud.biz;

import com.ai.common.Constant;
import com.ai.common.util.StringHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017-05-09.
 */
public class HBaseTest {

    private static HConnection HCONNECTION = null;
    private static Configuration conf;

    public static void main(String[] args) throws Exception{
        HBaseTest.connect();

        List<Put> list = new ArrayList<Put>();
        Put put = HBaseTest.bulidPut("522_13467517522", new String[]{"13467517522", "0"});
        list.add(put);
        HBaseTest.putHbase("USER_TRACE", list);

    }

    public static Put bulidPut(String rowkey, String[] values)
    {
        Put put = new Put(rowkey.getBytes());
        // 设置写WAL （Write-Ahead-Log）的级别 ASYNC_WAL:当数据变动时，异步写WAL日志
        // SYNC_WAL:当数据变动时，同步写WAL日志; FSYNC_WAL:当数据变动时，同步写WAL日志，并且，强制将数据写入磁盘
        // SKIP_WAL:不写WAL日志; USE_DEFAULT:使用HBase全局默认的WAL写入级别，即 SYNC_WAL;
//        put.setDurability(Durability.valueOf("ASYNC_WAL"));

        String[] columns = StringHelper.splitPreserveAllTokens("SERIAL_NUMBER,IN_MODE_CODE", ",");
        String columnFamily = "F";
        for (int i = 0, iLen = columns.length; i < iLen; i++)
        {
            if (StringHelper.isNotBlank(values[i]))
            {
                put.add(columnFamily.getBytes(), columns[i].getBytes(), values[i].getBytes());
            }
        }
        return put;
    }

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
            System.out.println("---------------------");
            table.flushCommits();
            System.out.println("======================");
            table.close();
            flag = true;
        }
        catch (Exception e)
        {
            Throwables.propagate(e);
        }
        finally
        {
            list.clear();
        }
        return flag;
    }

    public static void connect() throws IOException
    {
        String hosts = "192.168.10.128"; // zookeeper主机名称，多个主机名称以,号分隔开
        String port = "2181"; // 客户端端口号
        Preconditions.checkNotNull(hosts, "%s IS NULL", Constant.HOSTS);
        Preconditions.checkNotNull(port, "%s IS NULL", Constant.PORT);

        conf = HBaseConfiguration.create();
        // 用客户端的配置覆盖hbase的配置
        conf.set(Constant.HOSTS, hosts);
//        conf.set(Constant.PORT, port);
        // 配置Hadoop URL
//        conf.set(Constant.HDFS_URL, "hdfs://192.168.10.128:9000");
//        conf.setLong(Constant.HBASE_RPC_TIMEOUT, 60000);
        HCONNECTION = HConnectionManager.createConnection(conf);
    }
}
