package com.ai.cloud.bean;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2017-05-15.
 */
public interface FileResolve {

    public List<Map> resolve(File file) throws Exception;

    public boolean fileCheck(File file, FormatInfo formatInfo) throws IOException;
}
