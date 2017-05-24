package com.ai.cloud.bean;

import com.ai.common.util.DateHelper;
import com.ai.common.util.FileHelper;
import com.google.common.base.Throwables;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by pc on 2017-05-09.
 */
public class BomcFileResolve implements FileResolve{

    private final static Logger LOGGER = LoggerFactory.getLogger(BomcFileResolve.class);

    public List<Map> resolve(File file) throws Exception {

        List<Map> list = new ArrayList<Map>();

        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        try {
            while (true) {
                Map info = (Map) ois.readObject();
                String probetype = (String)info.get("probetype");
                if("app".equals(probetype))
                {
                    Map ext = (Map)info.get("ext");
                    if(ext != null && ext.containsKey("SERIAL_NUMBER"))
                    {
                        info.put("SERIAL_NUMBER", (String)ext.get("SERIAL_NUMBER"));
                        info.put("IN_MODE_CODE", (String)ext.get("IN_MODE_CODE"));
                        list.add(info);
                    }
                }
                info.put("START_TIME", DateHelper.getDate((Long.parseLong((String)info.get("starttime")))));
                info.put("TRADE_STAFF_ID", info.get("operid"));
            }
        } catch (Exception e) {
            ois.close();
        }

        return list;
    }

    public boolean fileCheck(File file, FormatInfo formatInfo) throws IOException {
        boolean flag = false;
        String fileName = file.getName();
        if (!(fileName.startsWith("bomc") && fileName.endsWith("dat")))
        {
            LOGGER.error("文件[{}]不符合入库条件", file.getAbsolutePath());
            FileHelper.bakFile(file, formatInfo.getErrorOutPath()); // 备份到错误目录
            flag = true;
        }
        return flag;
    }

    public static void main(String[] args) throws Exception {

//        File file = new File("h:/test/bomc/bomc.app-node40-srv01.04121620.dat");
//        FileResolve fileResolve = new BomcFileResolve();
//        fileResolve.resolve(file);
        String fileName = "bomc.app-node01-srv02.02061730.dat";
        System.out.println(fileName.startsWith("bomc"));
        System.out.println(fileName.endsWith("dat"));

        Map a = new HashMap();
        String bb = (String)a.get("da");
        System.out.println(bb);
        System.out.println(StringUtils.isNotBlank(bb));
    }
}
