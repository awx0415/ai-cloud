package com.ai.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.base.Throwables;

/**
 * @desc Md5生成函数
 * @author wangjw6
 * @date 2017年3月1日 下午3:00:53
 */
public class MD5Helper
{
    private static MessageDigest md = null;

    /**
     * @desc 生成32位MD5值
     * @param oriKey
     * @return
     * @author wangjw6
     * @date 2017年3月1日 下午3:00:28
     */
    public synchronized static String getMD5(String oriKey)
    {
        try
        {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            Throwables.propagate(e);
        }
        md.reset();
        md.update(oriKey.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer(32);
        String str = "";
        for (byte b : digest)
        {
            str = Integer.toHexString((int) (b & 0xff));
            if (str.length() == 1)
            {
                sb.append("0");
            }
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * @desc md5(2、4、6)
     * @param oriKey
     * @return
     * @author wangjw6
     * @date 2017年3月1日 下午3:06:14
     */
    public static String getRowkeyPrefix(String oriKey)
    {
        String md5 = getMD5(oriKey);
        return StringHelper.join(md5.substring(1, 2), md5.substring(3, 4), md5.substring(5, 6));
    }

    /**
     * @desc 电子发票RowKey
     * @param oriKey
     * @return
     * @author wangjw6
     * @date 2017年3月1日 下午3:05:10
     */
    public static String getRowKey(String oriKey)
    {
        return StringHelper.join(getRowkeyPrefix(oriKey), oriKey);
    }
    
    /**   
     * @desc 清单查询RowKey
     * @param oriKey
     * @param startTime
     * @param endTime
     * @return 
     * @author wangjw6
     * @date 2017年3月7日 下午5:25:03
     */
    public static String getRowKey(String oriKey, String startTime, String def)
    {
        return StringHelper.join(getRowkeyPrefix(oriKey), oriKey, startTime, def);
    }
}
