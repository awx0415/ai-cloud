package com.ai.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.ai.cloud.bean.FormatInfo;
import com.ai.common.Symbol;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class FileHelper
{
    private final static Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

    private FileHelper()
    {

    }

    /**
     * @desc 获取文件列表
     * @param filePath
     * @param batchNo
     * @return
     * @author wangjw6
     * @date 2017年3月2日 上午9:51:24
     */
    public static List<File> getListFiles(String filePath, int batchNo)
    {
        List<File> fileList = Lists.newArrayList();
        readFile(fileList, filePath, batchNo);
        // 按文件修改时间升序排序
        getFileSort(fileList);
        return fileList;
    }

    /**
     * @desc 批量读取清单文件
     * @param resultList
     * @param srcPath
     * @param batchNo
     * @author wangjw6
     * @date 2017年3月7日 上午11:50:19
     */
    public static void readFile(List<File> resultList, String srcPath, int batchNo)
    {
        int curFileSize = 0;
        File curFile = new File(srcPath);
        if (!curFile.isDirectory())
        {
            curFileSize = 1;
            resultList.add(curFile);
        }
        else if (curFile.isDirectory())
        {
            File[] subfiles = curFile.listFiles();
            curFileSize = subfiles.length;
            for (File readFile : subfiles)
            {
                if (!readFile.isDirectory())
                {
                    resultList.add(readFile);
                }
                else if (readFile.isDirectory())
                {
                    readFile(resultList, readFile.getPath().toString(), batchNo);
                }

                if (resultList.size() >= batchNo)
                {
                    break;
                }
            }
        }
        if (curFileSize > 0)
        {
            LOGGER.debug("[{}]目录积压文件|目录数[{}],当前处理文件总数[{}]", srcPath, curFileSize, resultList.size());
        }
    }

    /**
     * @desc 备份或者删除已经入库的文件
     * @param listFile
     * @param formatInfo
     * @throws IOException
     * @author wangjw6
     * @date 2017年3月7日 下午3:10:52
     */
    public static void bakSrcFile(List<File> listFile, FormatInfo formatInfo) throws IOException
    {
        bakSrcFile(listFile, formatInfo.getBakFileSwitch(), formatInfo.getBakFilePath());
    }

    /**
     * @desc 备份或者删除已经入库的文件
     * @param listFile
     * @param bakSwitch
     * @author wangjw6
     * @param bakFilePath
     * @throws IOException
     * @date 2017年3月2日 下午2:35:26
     */
    public static void bakSrcFile(List<File> listFile, boolean bakSwitch, String bakFilePath) throws IOException
    {
        for (int i = 0; i < listFile.size(); i++)
        {
            bakSrcFile(listFile.get(i), bakSwitch, bakFilePath);
        }

        listFile.clear();
    }

    /**
     * @desc 备份或者删除已经入库的文件
     * @param file
     * @param formatInfo
     * @throws IOException
     * @author wangjw6
     * @date 2017年3月7日 下午3:10:52
     */
    public static void bakSrcFile(File file, FormatInfo formatInfo) throws IOException
    {
        bakSrcFile(file, formatInfo.getBakFileSwitch(), formatInfo.getBakFilePath());
    }

    /**
     * @desc 备份或者删除已经入库的文件
     * @param file
     * @param bakSwitch
     * @author wangjw6
     * @param bakFilePath
     * @throws IOException
     * @date 2017年3月2日 下午2:35:26
     */
    public static void bakSrcFile(File file, boolean bakSwitch, String bakFilePath) throws IOException
    {
        // 备份原始清单文件
        if (bakSwitch)
        {
            LOGGER.info("begin bak file ...");
            if (file.exists())
            {
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                String dirDay = df.format(new Date());
                bakFile(file, new File(bakFilePath + "/" + dirDay));
            }
        }
        else
        {
            LOGGER.info("begin delete file ...");
            if (file.exists() && file.isFile())
            {
                file.delete();
            }
        }
    }

    public static boolean bakFile(File srcPath, String bakPath) throws IOException
    {
        return bakFile(srcPath, new File(bakPath));
    }

    /**
     * @desc 备份文件
     * @param srcPath
     * @param bakPath
     * @return
     * @author wangjw6
     * @throws IOException
     * @date 2017年3月2日 上午9:21:55
     */
    public static boolean bakFile(File srcPath, File bakPath) throws IOException
    {
        FileUtils.forceMkdir(bakPath);
        if (!srcPath.isDirectory())
        {
            FileUtils.moveFile(srcPath, newBakFile(bakPath, srcPath.getName()));
        }
        else if (srcPath.isDirectory())
        {
            String[] filelist = srcPath.list();
            for (int i = 0; i < filelist.length; i++)
            {
                File srcFile = newFile(srcPath, filelist[i]);
                if (!srcFile.isDirectory())
                {
                    FileUtils.moveFile(srcPath, newBakFile(bakPath, filelist[i]));
                }
                else if (srcFile.isDirectory())
                {
                    bakFile(srcFile, newFile(bakPath, filelist[i]));
                }
            }
        }
        return true;
    }

    /**
     * @desc 根据路径和文件名创建
     * @param fileDir
     * @param fileName
     * @return
     * @author wangjw6
     * @date 2017年3月3日 上午10:22:04
     */
    private static File newFile(File fileDir, String fileName)
    {
        String filePath = StringHelper.join(fileDir.getAbsolutePath(), File.separator, fileName);
        return new File(filePath);
    }

    /**
     * @desc 添加时间后缀
     * @param fileDir
     * @param fileName
     * @return
     * @author wangjw6
     * @date 2017年3月3日 上午10:22:19
     */
    private static File newBakFile(File fileDir, String fileName)
    {
        String curTime = String.valueOf(System.nanoTime());
        String filePath = StringHelper.join(fileDir.getAbsolutePath(), File.separator, fileName, Symbol.DOT, curTime);
        return new File(filePath);
    }

    /**
     * @desc 文件列表按时间排序
     * @param fs
     * @return
     * @author wangjw6
     * @date 2017年3月2日 上午9:21:38
     */
    public static File[] getFileSort(File[] fs)
    {
        if (null != fs && fs.length > 0)
        {
            Arrays.sort(fs, new FileHelper.LastModified());
        }
        return fs;
    }

    /**
     * @desc 文件列表按时间排序
     * @param list
     * @return
     * @author wangjw6
     * @date 2017年3月2日 上午9:05:19
     */
    public static List<File> getFileSort(List<File> list)
    {
        if (null != list && list.size() > 0)
        {
            Collections.sort(list, new FileHelper.LastModified());
        }
        return list;
    }

    /**
     * @desc 根据修改时间排序
     * @author wangjw6
     * @date 2017年3月2日 上午9:15:12
     */
    static class LastModified implements Comparator<File>
    {
        public int compare(File file1, File file2)
        {
            long diff = file1.lastModified() - file2.lastModified();
            if (diff > 0)
            {
                return 1;
            }
            else if (diff == 0)
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
    }

    /**
     * @desc 将不符合命名规范的文件记录下来
     * @param content
     * @param path
     * @param fileName
     * @throws IOException
     * @author wangjw6
     * @date 2017年3月2日 下午2:34:01
     */
    public static void writeErrFile(String content, String path, String fileName) throws IOException
    {
        String enter = "\r\n";
        File accPth = new File(path);
        if (!accPth.exists())
        {
            accPth.mkdirs();
        }
        FileWriter writer = new FileWriter(path + "/" + fileName, true);
        writer.write(content + enter);
        writer.flush();
        writer.close();
    }
}
