package com.example.commons.example;

import com.google.common.collect.Lists;
import com.example.commons.utils.ReadConfigUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

/***
 *
 *@Description xml文件解析、创建、修改案例
 * @author zhanghesheng
 * */
public class XmlExample {
    private static Logger logger = LoggerFactory.getLogger(XmlExample.class);

    /**
     * dom4j方式解析xml
     *
     * @param filePath xml文件路径
     * @return 所有的标签集
     * @author zhanghesheng
     * @date 2017/11/18
     */
    public static List<String> xmlFileParser(String filePath) {
        File file = new File(filePath);
        SAXReader saxReader = new SAXReader();
        List<String> list = Lists.newArrayList();
        try {
            Document document = saxReader.read(file);
            list = parserElement(document);
        } catch (DocumentException e) {
            logger.error("xml parser fail", e);
            e.printStackTrace();
        }
        return list;
    }

    private static List<String> parserElement(Document document) {
        List<String> list;
        Element rootElement = document.getRootElement();
        List<Element> childElements = rootElement.elements();
        List<String> resultList = Lists.newArrayList();
        resultList.add(rootElement.getName());
        list = forEachElements(childElements, resultList);
        return list;
    }

    /**
     * dom4j方式解析xml
     *
     * @param content xml字符串
     * @return 所有的标签集
     * @author zhanghesheng
     * @date 2017/11/18
     */
    public static List<String> xmlContentParser(String content) {
        if (content == null) {
            return null;
        }
        StringReader stringReader = new StringReader(content);
        InputSource inputSource = new InputSource(stringReader);
        SAXReader saxReader = new SAXReader();
        List<String> list = Lists.newArrayList();
        try {
            Document document = saxReader.read(inputSource);
            list = parserElement(document);
        } catch (DocumentException e) {
            logger.error("xml parser fail", e);
            e.printStackTrace();
        } finally {
            stringReader.close();
        }
        return list;
    }

    /**
     * dom4j方式创建或修改xml
     *
     * @param filename 文件路径
     * @return 1:成功,0:失败
     * @author zhanghesheng
     * @date 2017/11/18
     */
    public int newOrUpdateXml(String filename) {
        int result = 0;
        File file = new File(filename);
        if (file.exists() && StringUtils.isNotBlank(ReadConfigUtils.readFile(filename))) {
            //文件存在并且不为空
            try {
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(file);
                /** 修改内容之一: 如果book节点中show属性的内容为yes,则修改成no */
                /** 先用xpath查找对象 */
                List list = document.selectNodes("//firstChild/@name");
                Iterator iter = list.iterator();
                while (iter.hasNext()) {
                    Attribute attribute = (Attribute) iter.next();
                    if (attribute.getValue().equals("oneNode")) {
                        attribute.setValue("oneNode_bak");
                    }
                }
                /**
                 * 修改内容之二: 把owner项内容改为Tshinghua
                 * 并在owner节点中加入date节点,date节点的内容为2004-09-11,还为date节点添加一个属性type
                 */
                list = document.selectNodes("//firstChild");
                iter = list.iterator();
                if (iter.hasNext()) {
                    Element firstChildElement = (Element) iter.next();
                    Element dateElement = firstChildElement.addElement("date");
                    dateElement.setText("2017-11-19");
                    dateElement.addAttribute("id", "dateTime");
                }
                /** 修改内容之三: 若title内容为Dom4j Tutorials,则删除该节点 */
                list = document.selectNodes("/root/sencondChild");
                iter = list.iterator();
                while (iter.hasNext()) {
                    Element bookElement = (Element) iter.next();
                    Iterator iterator = bookElement.elementIterator("title");
                    while (iterator.hasNext()) {
                        Element titleElement = (Element) iterator.next();
                        if (titleElement.getText().equals("Hello,title2")) {
                            bookElement.remove(titleElement);
                        }
                    }
                }

                result = writeXmlContent(result, file,document);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } else {
            //文件为空
            //create xml
            Document document = DocumentHelper.createDocument();
            Element rootElement = document.addElement("root");
            rootElement.addComment("root Node create");

            Element rootFirstChild = rootElement.addElement("firstChild");
            rootFirstChild.addAttribute("name", "oneNode");
            rootFirstChild.addComment("first root-child Node");
            rootFirstChild.addText("this is first child Node's text");

            Element rootSecondChild = rootElement.addElement("sencondChild");
            Element sencondSonElement = rootSecondChild.addElement("title");
            sencondSonElement.setText("Hello,dom4j xml create title 1");
            Element title2 = rootSecondChild.addElement("title");
            title2.addText("Hello,title2");
            result = writeXmlContent(result, file,document);

        }

        return result;
    }

    /**
     * 格式化XML文档,并解决中文问题
     *
     * @param filename
     * @return
     */
    public int formatXMLFile(String filename) {
        int returnValue = 0;
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new File(filename));
            XMLWriter writer = null;
            /** 格式化输出,类型IE浏览一样 */
            OutputFormat format = OutputFormat.createPrettyPrint();
            /** 指定XML编码 */
            format.setEncoding("GBK");
            writer = new XMLWriter(new FileWriter(new File(filename)), format);
            writer.write(document);
            writer.close();
            /** 执行成功,需返回1 */
            returnValue = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return returnValue;

    }






    /** 将document中的内容写入文件中 */
    private int writeXmlContent(int result, File file,Document document) {
        try {
            XMLWriter writer = new XMLWriter(new FileWriter(file));
            writer.write(document);
            writer.close();
            /** 执行成功,需返回1 */
            result = 1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 递归遍历
     *
     * @param
     * @return
     * @author zhanghesheng
     * @date 2017/11/18
     */
    private static List<String> forEachElements(List<Element> elements, List<String> list) {
        if (elements == null || elements.isEmpty()) {
            return list;
        }
        for (Element chileElement : elements) {
            list.add(chileElement.getName());
            forEachElements(chileElement.elements(), list);
        }
        return list;
    }

    public static void main(String[] args) {
        String content = "<xml><a><testfile></testfile></a><c></c></xml>";
        List list = xmlContentParser(content);
        list.forEach(System.out::println);
    }

}
