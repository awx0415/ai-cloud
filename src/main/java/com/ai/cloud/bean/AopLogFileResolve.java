package com.ai.cloud.bean;

import com.ai.common.util.DateHelper;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2017-05-18.
 */
public class AopLogFileResolve implements FileResolve{

    private final static Logger LOGGER = LoggerFactory.getLogger(AopLogFileResolve.class);

    public static String split_sign = "\\^";

    public List<Map> resolve(File file) throws Exception {
        List<Map> infoList = new ArrayList<Map>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        while((line = br.readLine()) != null){
            Map map = new HashMap();
            String[] strarr = line.split(split_sign);
            for (String key_value : strarr) {
                String[] arr = key_value.split("=", 2);
                String key = arr[0].trim();
                String value = arr[1];
                if ("ACCESS_NUM".equals(key)){
                    value = value.trim();
                    if(StringUtils.isNotBlank(value)){
                        map.put("SERIAL_NUMBER", value);
                    }
                }
                if ("FLOWINTIME".equals(key)){
                    value = value.trim();
                    if(StringUtils.isNotBlank(value)){
                        map.put("START_TIME", DateHelper.getDate(Long.parseLong(value)));
                    }
                }
                if("REQFLOWMSG".equals(key)){
                    value = value.replace("[", "");
                    value = value.replace("]", "");
                    JSONObject json = JSONObject.fromObject(value);
                    Map reqFlowMsgMap = (Map)json;
                    if(reqFlowMsgMap.containsKey("TRADE_STAFF_ID")
                            || reqFlowMsgMap.containsKey("IN_MODE_CODE")){
                        map.put("TRADE_STAFF_ID", (String)reqFlowMsgMap.get("TRADE_STAFF_ID"));
                        map.put("IN_MODE_CODE", (String)reqFlowMsgMap.get("IN_MODE_CODE"));
                    }
                }
            }

            if(map.containsKey("SERIAL_NUMBER")
                    && map.containsKey("START_TIME")
//                    && map.containsKey("TRADE_STAFF_ID")
                    ){
                infoList.add(map);
            }
        }

        return infoList;
    }

    public boolean fileCheck(File file, FormatInfo formatInfo) throws IOException {
        return false;
    }

    public static void main(String[] args) throws Exception{
        File f = new File("H:/test/aoplog/work/2_1001.20170508182531.dat.6");
        AopLogFileResolve test = new AopLogFileResolve();
        test.resolve(f);
    }
}
