package com.ai.cloud.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("format")
public class FormatInfo
{
    private String id; // 格式化信息ID
    private String parallelClass; // 平行转换类
    private String tableName; // HBASE清单表名前缀
    private String columnFamily; // 列族名，hbase表列族
    private String column; // 字段
    private String separator; // 分隔符
    private long intervaLTime; // 间隔扫描时间 单位毫秒(ms)
    private int batchNo; // 单批次处理文件数
    private int batchPut; // 单批次处理清单(条)
    private String durability; // 设置写WAL （Write-Ahead-Log）的级别
    private String srcFilePath; // 源文件路径
    private String skipFile; // 符合条件的文件
    private boolean bakFileSwitch; // 文件备份开关，"true"表示备份清单文件
    private String bakFilePath; // 文件备份目录
    private String errorOutPath; // 错误文件输出目录
    private String fileResolveImplClass;//文件解析类

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getParallelClass()
    {
        return parallelClass;
    }

    public void setParallelClass(String parallelClass)
    {
        this.parallelClass = parallelClass;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getColumnFamily()
    {
        return columnFamily;
    }

    public void setColumnFamily(String columnFamily)
    {
        this.columnFamily = columnFamily;
    }

    public String getColumn()
    {
        return column;
    }

    public void setColumn(String column)
    {
        this.column = column;
    }

    public String getSeparator()
    {
        return separator;
    }

    public void setSeparator(String separator)
    {
        this.separator = separator;
    }

    public long getIntervaLTime()
    {
        return intervaLTime;
    }

    public void setIntervaLTime(long intervaLTime)
    {
        this.intervaLTime = intervaLTime;
    }

    public int getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(int batchNo)
    {
        this.batchNo = batchNo;
    }

    public int getBatchPut()
    {
        return batchPut;
    }

    public void setBatchPut(int batchPut)
    {
        this.batchPut = batchPut;
    }

    public String getDurability()
    {
        return durability;
    }

    public void setDurability(String durability)
    {
        this.durability = durability;
    }

    public String getSrcFilePath()
    {
        return srcFilePath;
    }

    public void setSrcFilePath(String srcFilePath)
    {
        this.srcFilePath = srcFilePath;
    }

    public String getSkipFile()
    {
        return skipFile;
    }

    public void setSkipFile(String skipFile)
    {
        this.skipFile = skipFile;
    }

    public boolean getBakFileSwitch()
    {
        return bakFileSwitch;
    }

    public void setBakFileSwitch(boolean bakFileSwitch)
    {
        this.bakFileSwitch = bakFileSwitch;
    }

    public String getBakFilePath()
    {
        return bakFilePath;
    }

    public void setBakFilePath(String bakFilePath)
    {
        this.bakFilePath = bakFilePath;
    }

    public String getErrorOutPath()
    {
        return errorOutPath;
    }

    public void setErrorOutPath(String errorOutPath)
    {
        this.errorOutPath = errorOutPath;
    }

    public String getFileResolveImplClass() {
        return fileResolveImplClass;
    }

    public void setFileResolveImplClass(String fileResolveImplClass) {
        this.fileResolveImplClass = fileResolveImplClass;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
