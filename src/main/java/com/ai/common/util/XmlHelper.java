package com.ai.common.util;

import java.io.File;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.base.Throwables;
import com.thoughtworks.xstream.XStream;

/**
 * @desc xml转换工具类
 * @author wangjw6
 * @date 2016-7-12 上午10:41:44
 */
public final class XmlHelper
{

    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    private XmlHelper()
    {
    }

    /**
     * @desc 序列化XML
     * @param obj
     * @return
     * @date 2016-6-13 上午10:18:59
     */
    public static <T> String toXML(Object obj)
    {
        XStream stream = getXStream();
        stream.processAnnotations(obj.getClass());
        return new StringBuffer(XML_DECLARATION).append(stream.toXML(obj)).toString();
    }

    /**
     * @desc 反序列化XML
     * @param xmlStr
     * @param clazz
     * @return
     * @date 2016-6-13 上午10:19:09
     */
    public static <T> T fromXML(String xmlStr, Class<T> clazz)
    {
        XStream stream = getXStream();
        stream.processAnnotations(clazz);
        Object obj = stream.fromXML(xmlStr);
        return objCast(obj, clazz);
    }

    /**
     * @desc 反序列化XML
     * @param xmlStr
     * @param clazz
     * @return
     * @date 2016-6-13 上午10:19:09
     */
    public static <T> T fromXML(File file, Class<T> clazz)
    {
        XStream stream = getXStream();
        stream.processAnnotations(clazz);
        Object obj = stream.fromXML(file);
        return objCast(obj, clazz);
    }

    /**
     * @desc 反序列化XML
     * @param xmlStr
     * @param clazz
     * @return
     * @date 2016-6-13 上午10:19:09
     */
    public static <T> T fromXML(InputStream is, Class<T> clazz)
    {
        XStream stream = getXStream();
        stream.processAnnotations(clazz);
        Object obj = stream.fromXML(is);
        return objCast(obj, clazz);
    }

    private static <T> T objCast(Object obj, Class<T> clazz)
    {
        T t = null;
        try
        {
            t = clazz.cast(obj);
        }
        catch (ClassCastException e)
        {
            Throwables.propagate(e);
        }
        return t;
    }

    /**
     * @desc 获取指定节点的值
     * @param xpath
     * @param dataStr
     * @return
     * @date 2016-6-13 上午10:19:18
     */
    public static String getNodeValue(String xpath, String dataStr)
    {
        String value = "";
        try
        {
            // 将字符串转为xml
            Document document = DocumentHelper.parseText(dataStr);
            // 查找节点
            Element element = (Element) document.selectSingleNode(xpath);
            if (element != null)
            {
                value = element.getStringValue();
            }
        }
        catch (DocumentException e)
        {
            Throwables.propagate(e);
        }
        return value;
    }

    /**
     * @desc 获取根节点
     * @param xpath
     * @param dataStr
     * @return
     * @date 2016-6-13 下午3:03:18
     */
    public static Element getRootElement(InputStream is)
    {
        Element element = null;
        try
        {
            SAXReader reader = new SAXReader();
            Document document = reader.read(is);
            element = document.getRootElement();
        }
        catch (DocumentException e)
        {
            Throwables.propagate(e);
        }
        return element;
    }

    /**
     * @desc 获取Xstream实例
     * @return
     * @date 2016-6-13 上午10:19:35
     */
    public static XStream getXStream()
    {
        return new XStream();
    }

}
