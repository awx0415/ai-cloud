package com.ai.cloud.bean;

import com.ai.common.util.DateHelper;
import com.csvreader.CsvReader;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pc on 2017-05-11.
 */
public class WebTransFileResolve implements FileResolve{

    public List<Map> resolve(File file) throws Exception{

        List<Map> infoList = new ArrayList<Map>();

        // 创建CSV读对象
        CsvReader csvReader = new CsvReader(file.getAbsolutePath(), ',', Charset.forName("SJIS"));

        // 读表头
        csvReader.readHeaders();
        while (csvReader.readRecord()){
            // 读这行的某一列
            String serialNumber = csvReader.get("oneparam:WT.hn_mobile");
            if(StringUtils.isBlank(serialNumber))
            {
                continue;
            }
            String dateTime = csvReader.get("DateTime");
            if(dateTime.length() == 10)
            {
                dateTime += "000";//文件里只有10位，需补足13位;
            }
            Map<String, String> info = new HashMap<String, String>();
            info.put("SERIAL_NUMBER", serialNumber);
            info.put("IN_MODE_CODE", "5");
            info.put("START_TIME", DateHelper.getDate(Long.parseLong(dateTime)));
//            info.put("TRADE_STAFF_ID", dateTime);
            infoList.add(info);
        }

        return  infoList;
    }

    public boolean fileCheck(File file, FormatInfo formatInfo) throws IOException {
        return false;
    }

    public static void main(String[] args) throws Throwable {
        String filePath = "h:/test/webtrans/work/test.csv";

        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("SJIS"));

            // 读表头
            csvReader.readHeaders();
            while (csvReader.readRecord()){
                // 读一整行
                System.out.println(csvReader.getRawRecord());
                // 读这行的某一列
                System.out.println(csvReader.get("oneparam:WT.hn_mobile"));
                System.out.println(csvReader.get("DateTime"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
