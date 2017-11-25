package com.moxie.commons.utils;


import com.thoughtworks.xstream.XStream;
import lombok.Data;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

public class MyTest {

    /**
     * @return
     * @author zhanghesheng
     * @description xstream方式xml与Object互转
     * @date 2017/11/19
     */
    @Test
    public void testXml() {
        XStream xs = new XStream();
        Bean1 bean1 = new Bean1();
        bean1.setF1("aaa");
        bean1.setF2("bbb");
        String toXML = xs.toXML(bean1);
        Bean1 fromXML = (Bean1) xs.fromXML(toXML);
        System.out.println(toXML);
        System.out.println(fromXML.getF1() + "\t" + fromXML.getF2());
    }

    @Test
    public void testFile() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource("");
        String dir = System.getProperty("user.dir")+"/src/main/resources/";
        //File file = new File(dir+"a.txt");
       // if(file.exists()) file.delete();
        //file.createNewFile();
        System.out.println(dir);
        System.out.println(resource.getPath());

    }

    @Test
    public void testString() throws Exception {
        String s = "\\123";
      // String name = s.substring() ;
//        String file=s.substring(0,s.lastIndexOf("/"));

        System.out.println(s.substring(0,1).equalsIgnoreCase("\\"));
        //System.out.println(file);

    }

    @Test
    public void testjsoup() throws Exception { }

    @Data
    public static class Bean1 {
        String f1;
        String f2;
        String f3;
    }
}



