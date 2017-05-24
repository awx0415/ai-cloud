package com.ai.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ai.common.Symbol;
import org.apache.commons.lang3.StringUtils;

import com.ai.common.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * @desc 字符串处理工具类
 * @author wangjw6
 * @date 2016-7-12 上午10:36:14
 */
public final class StringHelper extends StringUtils
{
    private static final Pattern REGEX_BRACE = Pattern.compile("(?<=\\{)(.*?)(?=\\})"); // 匹配大括号中间内容
    private static final Pattern REGEX_BRACKET = Pattern.compile("(?<=\\[)(.*?)(?=\\])"); // 匹配中括号中间内容
    private static final Pattern REGEX_PARENTHESES = Pattern.compile("(?<=\\()(.*?)(?=\\))"); // 匹配小括号中间内容

    private StringHelper()
    {
        super();
    }

    /**
     * @desc 多个字符串任意一个为空则返回true
     * @param args
     * @return
     * @author wangjw6
     * @date 2016-7-25 下午9:06:29
     */
    public static boolean isBlank(String[] args)
    {
        boolean result = false;
        for (CharSequence cs : args)
        {
            if (isBlank(cs))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * @desc 多个字符串任意一个为null则返回true
     * @param args
     * @return
     * @author wangjw6
     * @date 2016-7-25 下午9:06:29
     */
    public static boolean isEmpty(String[] args)
    {
        boolean result = false;
        for (CharSequence cs : args)
        {
            if (isEmpty(cs))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public static boolean notEquals(CharSequence cs1, CharSequence cs2)
    {
        return !equals(cs1, cs2);
    }

    public static boolean notIn(String src, String separator, String... args)
    {
        return !in(src, separator, args);
    }

    /**
     * @desc 组装Key
     * @param keys
     * @return
     * @author wangjw6
     * @date 2016-7-12 上午10:37:25
     */
    public static String getKey(String... keys)
    {
        return getKey(Arrays.asList(keys));
    }

    public static String getKey(String tableId, String[] keys)
    {
        return getKey(tableId, null, keys);
    }

    /**   
     * @desc 组装Key
     * @param tableId
     * @param field
     * @param keys
     * @return 
     * @author wangjw6
     * @date 2016-9-18 下午3:53:35
     */
    public static String getKey(String tableId, String field, String[] keys)
    {
        List<String> keyList = new ArrayList<String>();
        keyList.add(tableId);
        keyList.add(field);
        keyList.addAll(Arrays.asList(keys));
        return getKey(keyList);
    }

    public static String getKey(List<String> keyList)
    {
        return join(keyList, Symbol.SEPARATOR);
    }

    public static String getKey(String[] keys, String split)
    {
        return join(keys, split);
    }

    /**
     * @desc 匹配大括号{}中间内容
     * @param str
     * @return
     * @author wangjw6
     * @date 2016-7-25 上午10:50:03
     */
    public static List<String> getBrace(String str)
    {
        return getRegexList(str, REGEX_BRACE);
    }

    /**
     * @desc 匹配中括号[]中间内容
     * @param str
     * @return
     * @author wangjw6
     * @date 2016-7-25 上午10:50:13
     */
    public static List<String> getBracket(String str)
    {
        return getRegexList(str, REGEX_BRACKET);
    }

    /**
     * @desc 匹配小括号()中间内容
     * @param str
     * @return
     * @author wangjw6
     * @date 2016-7-25 上午10:50:55
     */
    public static List<String> getParentheses(String str)
    {
        return getRegexList(str, REGEX_PARENTHESES);
    }

    /**
     * @desc 正则匹配
     * @param str
     * @param pattern
     * @return
     * @date 2016-6-8 下午12:13:00
     */
    public static List<String> getRegexList(String str, Pattern pattern)
    {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isBlank(str))
        {
            return list;
        }
        Matcher matcher = pattern.matcher(str);
        while (matcher.find())
        {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * @desc json格式化输出
     * @param jsonStr
     * @return
     * @author wangjw6
     * @date 2016-7-12 上午10:37:47
     */
    public static String jsonFormat(String jsonStr)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonStr);
        return gson.toJson(je);
    }

    /**
     * @desc 判断字符串是否包含
     * @param src
     * @param args
     * @return
     * @author wangjw6
     * @date 2016-7-16 上午11:02:27
     */
    public static boolean in(String src, String... args)
    {
        return in(src, Symbol.SEPARATOR, args);
    }

    /**
     * @desc 判断字符串是否包含
     * @param src
     * @param separator
     * @param args
     * @return
     * @author wangjw6
     * @date 2016-7-12 下午4:25:58
     */
    public static boolean in(String src, String separator, String[] args)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(separator);
        for (String tmp : args)
        {
            sb.append(tmp).append(separator);
        }
        return StringUtils.contains(sb.toString(), separator + src + separator);
    }

    /**
     * @desc Oracle sign函数（字符串是否大于0）
     * @param spCode
     * @return
     * @author wangjw6
     * @date 2016-7-25 下午9:54:21
     */
    public static boolean getOrclSign(String spCode)
    {
        return (isNumeric(spCode) && (Integer.valueOf(spCode) > Constant.ZERO));
    }

    /**
     * @desc 如果srcValue空，返回descValue
     * @param
     * @param descValue
     * @return
     * @author sunwh3
     * @date 2016-7-22 下午7:30:11
     */
    public static String nvl(String src, String defvl)
    {
        return isBlank(src) ? defvl : src;
    }

    /**
     * @desc srcTime在有效期范围内
     * @param srcTime
     * @param startTime
     * @param endTime
     * @return
     * @author wangjw6
     * @date 2016-7-27 下午5:19:23
     */
    public static boolean isBetween(String srcTime, String startTime, String endTime)
    {
        if (StringHelper.isEmpty(new String[] { srcTime, startTime, endTime }))
        {
            return false;
        }
        return (srcTime.compareTo(startTime) >= Constant.ZERO && srcTime.compareTo(endTime) <= Constant.ZERO);
    }

    /**
     * @desc 不在有效期范围内
     * @param srcTime
     * @param startTime
     * @param endTime
     * @return
     * @author wangjw6
     * @date 2016-7-27 下午5:25:21
     */
    public static boolean isNotBetween(String srcTime, String startTime, String endTime)
    {
        return !isBetween(srcTime, startTime, endTime);
    }

}
