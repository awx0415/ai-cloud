package com.ai.cloud.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("loadformat")
public class LoadFormat
{
    @XStreamAlias("hbase.zookeeper.quorum")
    private String zkQuorum;
    @XStreamAlias("hbase.zookeeper.property.clientPort")
    private String zkClientPort;
    @XStreamAlias("fs.defaultFS")
    private String defaultFS;
    @XStreamAlias("hbase.rpc.timeout")
    private long rpcTimeOut;
    @XStreamAlias("format")
    private FormatInfo formatInfo;

    public String getZkQuorum()
    {
        return zkQuorum;
    }

    public void setZkQuorum(String zkQuorum)
    {
        this.zkQuorum = zkQuorum;
    }

    public String getZkClientPort()
    {
        return zkClientPort;
    }

    public void setZkClientPort(String zkClientPort)
    {
        this.zkClientPort = zkClientPort;
    }

    public String getDefaultFS()
    {
        return defaultFS;
    }

    public void setDefaultFS(String defaultFS)
    {
        this.defaultFS = defaultFS;
    }

    public long getRpcTimeOut()
    {
        return rpcTimeOut;
    }

    public void setRpcTimeOut(long rpcTimeOut)
    {
        this.rpcTimeOut = rpcTimeOut;
    }

    public FormatInfo getFormatInfo()
    {
        return formatInfo;
    }

    public void setFormatInfo(FormatInfo formatInfo)
    {
        this.formatInfo = formatInfo;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
