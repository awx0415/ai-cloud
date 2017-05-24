package com.ai.common;

/**
 * @desc 系统常量
 * @author wangjw6
 * @date 2016-7-12 上午10:43:03
 */
public final class Constant
{
    private Constant()
    {

    }

    public final static String HDFS_URL = "fs.defaultFS";

    /** 连接参数 */
    public final static String HOSTS = "hbase.zookeeper.quorum"; // zookeeper服务器
    public final static String PORT = "hbase.zookeeper.property.clientPort";// zookeeper端口

    /** 线程池相关参数 */
    public final static String THREADPOOL_COREPOOLSIZE = "threadpool.core.size";// 默认并发运行的线程数
    public final static String THREADPOOL_MAXPOOLSIZE = "threadpool.max.size";// 默认池中最大线程数
    public final static String THREADPOOL_KEEPALIVETIME = "threadpool.keepalivetime";// 默认池中线程最大等待时间

    public final static String HBASE_RPC_TIMEOUT = "hbase.rpc.timeout";

    /** 默认连接名 **/
    public static final String CONN_NAME = "default";

    /** 数字0 **/
    public static final int ZERO = 0;

}
