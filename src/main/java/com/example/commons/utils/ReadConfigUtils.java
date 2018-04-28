package com.example.commons.utils;


import java.io.*;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 *
 *@Description 文件读取与写入
 * @author zhanghesheng
 * */
public class ReadConfigUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(ReadConfigUtils.class);
    private static final String DEFAUT_ENCODING = "utf-8";
    public static final String LINE_SEPARATOR_UNIX = "\n";
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
    public static final char DIR_SEPARATOR_UNIX = '/';
    public static final char DIR_SEPARATOR_WINDOWS = '\\';

    /**
     * @param fileName 文件名
     * @return String 文件内容
     * @Description 原始的文件读取方法
     * @author zhanghesheng
     * @date 2017/11/18
     */
    public static String readFileByLines(String fileName) {
        InputStream inputStream = null;
        InputStreamReader inputFileReader;
        String tempString;
        BufferedReader reader = null;
        String content = "";
        try {//绝对路径
            if (fileName.charAt(0) == DIR_SEPARATOR_UNIX || fileName.charAt(0) == DIR_SEPARATOR_WINDOWS) {
                inputStream = new FileInputStream(fileName);
            } else {//相对路径
                inputStream = ReadConfigUtils.class.getResourceAsStream(DIR_SEPARATOR_UNIX + fileName);
            }
            inputFileReader = new InputStreamReader(inputStream, DEFAUT_ENCODING);
            reader = new BufferedReader(inputFileReader);

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                content += tempString + LINE_SEPARATOR_UNIX;
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.error("the file read failed ", e);
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return content;
    }

    /**
     * @param fileName 文件路径 可以是绝对路径，也可以是Resource目录下的相对路径
     * @return String 文件内容
     * @Description 直接用apache 工具类IOUtils(建议使用)
     * @author zhanghesheng
     * @date 2017/11/18
     */
    public static String readFile(String fileName) {
        if (fileName == null || StringUtils.isBlank(fileName)) return null;
        String str = null;
        InputStream inputStream = null;
        FileInputStream file;
        BufferedReader reader;
        InputStreamReader inputFileReader;
        try {//绝对路径
            if (fileName.charAt(0) == DIR_SEPARATOR_UNIX || fileName.charAt(0) == DIR_SEPARATOR_WINDOWS) {
                file = new FileInputStream(fileName);
                inputFileReader = new InputStreamReader(file, DEFAUT_ENCODING);
                reader = new BufferedReader(inputFileReader);
                str = IOUtils.toString(reader);
            } else {//相对路径
                //fileName为根目录下的路径：Resource:XX/xx.xx
                inputStream = ReadConfigUtils.class.getResourceAsStream(DIR_SEPARATOR_UNIX + fileName);
               //等同inputStream = ReadConfigUtils.class.getClassLoader().getResourceAsStream(fileName);
                str = IOUtils.toString(inputStream, DEFAUT_ENCODING);
            }
        } catch (IOException e) {
            LOGGER.error("the file read failed ", e);
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return str;
    }

    /**
     * @param content  要写入的内容
     * @param filePath 文件路径
     * @param fileName 写入的文件名
     * @return void
     * @author zhanghesheng
     * @date 2017/11/24
     */
    public static void writeFile(String content, String filePath, String fileName) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file =new File(filePath+File.separator+fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        ByteArrayInputStream is=null;
        FileOutputStream fos=null;
        try {
             is = new ByteArrayInputStream(content.getBytes());
             fos = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int len = -1;
            while((len = is.read(buffer) )!= -1){
                fos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);
        }
    }


    public static void main(String[] args)throws Exception {
        System.out.println(readFile("des.js"));
    }
}
