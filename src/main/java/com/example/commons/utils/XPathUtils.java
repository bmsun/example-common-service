package com.example.commons.utils;

import com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl;
import org.apache.commons.lang3.ArrayUtils;
import org.htmlcleaner.*;
import org.jdom2.Document;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.AbstractXMLOutputProcessor;
import org.jdom2.output.support.FormatStack;
import org.jdom2.util.NamespaceStack;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 *@Description
 * @author zhanghesheng
 * */
public class XPathUtils {

    private static Logger logger = LoggerFactory.getLogger(XPathUtils.class);
    private static final String TEXT_REG_EX ="\\/text\\(\\)(\\[(\\d+)\\])?$";
    private static int contentNodeCount = 0;
    private static int targetContentNodeIndex = 0;

    /**
     * @param select  jsoup 表达式
     * @param content 需要解析的文本
     * @return collection
     * @author zhanghesheng
     * @date 2017/11/22
     */
    public static Collection<? extends String> useJsoupSelect(String select, String content) {
        LinkedList splits = new LinkedList();
        try {
            Elements eles = Jsoup.parse(content).select(select);
            if (eles != null) {
                int size = eles.size();

                for(int i = 0; i < size; ++i) {
                    Element element = (Element)eles.get(i);
                    splits.add(element.outerHtml());
                }
            }
        } catch (Exception var7) {
            logger.error("select use jsoup error! select " + select);
        }

        return splits;
    }
    /**
     * @param xpathExp xpath表达式
     * @param content  需要解析的文本
     * @return List<String>
     * @author zhanghesheng
     * @date 2017/11/18
     */
    public static Collection<? extends String> useXpathSelect(String xpathExp, String content) {
        //对要解析的文本进行预处理
        content=contentPreClean(content);
        List<String> splits = new LinkedList();
        Pattern pattern = Pattern.compile(TEXT_REG_EX);
        Matcher matcher = pattern.matcher(xpathExp);
        boolean extractText = matcher.find();
        Integer textIndex = null;
        if (extractText) {
            try {
                String indexString = xpathExp.substring(matcher.start(2), matcher.end(2));
                textIndex = Integer.parseInt(indexString);
            } catch (Exception var18) {

            }

            xpathExp = xpathExp.substring(0, matcher.start());
        }

        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            TagNode node = cleaner.clean(content);
            XMLOutputter out = new XMLOutputter();
            out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
            out.setXMLOutputProcessor(new XPathUtils.CustomProcessor());
            JDomSerializer jdomSerializer = new JDomSerializer(cleaner.getProperties(), false);
            Object[] elements = node.evaluateXPath(xpathExp);
            if (ArrayUtils.isNotEmpty(elements)) {
                Object[] var12 = elements;
                int var13 = elements.length;

                for(int var14 = 0; var14 < var13; ++var14) {
                    Object obj = var12[var14];
                    if (obj instanceof TagNode) {
                        TagNode resultNode = (TagNode)obj;
                        if (extractText) {
                            String resultNodeString = resultNode.getText().toString();
                            if (textIndex != null) {
                                splits.add(getTextNodeByIndex(resultNode, textIndex.intValue()).replace("\n", ""));
                            } else {
                                splits.add(resultNodeString);
                            }
                        } else {
                            Document doc = jdomSerializer.createJDom(resultNode);
                            splits.add(out.outputString(doc.getRootElement()));
                        }
                    } else if (obj instanceof CharSequence) {
                        splits.add(String.valueOf(obj));
                    }
                }
            }
        } catch (Exception var19) {
            logger.error("select use xpath error! xpath " + xpathExp);
        }

        return splits;
    }

    public static Collection<? extends String> useSaxonXpathSelect(String xpath, String content) {
         content=contentPreClean(content);
        LinkedList xPathSelectResults = new LinkedList();
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            TagNode node = cleaner.clean(content);
            org.w3c.dom.Document doc = (new DomSerializer(new CleanerProperties())).createDOM(node);
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            XPathFactory xpf = XPathFactoryImpl.newInstance("http://java.sun.com/jaxp/xpath/dom");
            XPath xPath = xpf.newXPath();
            XPathExpression compile = xPath.compile(xpath);
            NodeList nodes = (NodeList)compile.evaluate(doc, XPathConstants.NODESET);

            for(int i = 0; i < nodes.getLength(); ++i) {
                Node nowNode = nodes.item(i);
                if (nowNode.getNodeType() == 3) {
                    xPathSelectResults.add(nowNode.getTextContent().replace(" ", ""));
                } else {
                    org.w3c.dom.Document newDoc = builder.newDocument();
                    Node copyNode = newDoc.importNode(nowNode, true);
                    newDoc.appendChild(copyNode);
                    xPathSelectResults.add(documentToString(newDoc));
                }
            }
        } catch (Exception var16) {
            logger.error("select use saxon xpath error! xpath " + xpath);
        }

        return xPathSelectResults;
    }


    public static String documentToString(org.w3c.dom.Document document) throws TransformerException {
        XMLOutputter out = new XMLOutputter();
        out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
        out.setXMLOutputProcessor(new XPathUtils.CustomProcessor());
        DOMBuilder in = new DOMBuilder();
        Document jDomDoc = in.build(document);
        return out.outputString(jDomDoc.getRootElement());
    }

    private static String getTextNodeByIndexRecursion(HtmlNode node) {
        if (node instanceof ContentNode) {
            ++contentNodeCount;
            if (contentNodeCount == targetContentNodeIndex) {
                return ((ContentNode)node).getContent();
            }
        }

        if (node instanceof TagNode) {
            Iterator var1 = ((TagNode)node).getAllChildren().iterator();

            while(var1.hasNext()) {
                Object childNode = var1.next();
                String ret = getTextNodeByIndexRecursion((HtmlNode)childNode);
                if (ret != null) {
                    return ret;
                }
            }
        }

        return null;
    }

    private static String getTextNodeByIndex(HtmlNode node, int index) {
        contentNodeCount = 0;
        targetContentNodeIndex = index;
        return getTextNodeByIndexRecursion(node);
    }
    //对要解析的文本进行预处理
    private static String contentPreClean(String content) {
        content = content.trim();
        if (content.endsWith("</td>") || content.endsWith("</tr>")) {
            StringBuilder value = new StringBuilder();
            value.append("<table>").append(content).append("</table>");
            content = value.toString();
        }

        return content;
    }


    static final class CustomProcessor extends AbstractXMLOutputProcessor {
        CustomProcessor() {
        }
        @Override
        public void process(Writer out, Format format, org.jdom2.Element element) throws IOException {
            FormatStack fStack = new FormatStack(format);
            fStack.setEscapeOutput(false);
            this.printElement(out, fStack, new NamespaceStack(), element);
            out.flush();
        }
    }

    public static void main(String[] args) {
        //String content1 = ReadConfigUtils.readFile("call-detail-page_201709_20171117173924993.html");
        String content1 = ReadConfigUtils.readFile("testfile/test.html");
        List<String> list1 = (List<String>)useSaxonXpathSelect("//tr/td[2]/text()[1]",content1);
        list1.forEach(System.out::println);
    }
}
