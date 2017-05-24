package com.ai.cloud.bean;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2017-05-09.
 */
public class TestFile {

    public static void main(String[] args) throws Exception
    {
        File file = new File("h:/test/bomc/bomc1.dat");
        String pdfFile = FileUtils.readFileToString(file);
        System.out.println(pdfFile);


//        Map map = new HashMap(pdfFile);
    }
}
