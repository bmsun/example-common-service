package com.example.commons.utils;

import jodd.util.HtmlDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/***
 *
 *@Description
 * @author zhanghesheng
 * */
@Slf4j
public class FileUtils {
    private static final String DIR_SEPARATOR_UNIX = "/";
    private static final String DIR_SEPARATOR_WINDOWS = "\\";
    private static final String USER_DIR = "user.dir";
    private static final String ROOT_SOURCE = "/src/main/resources/";


    public static boolean isExists(String fileName) {
        if (fileName == null || StringUtils.isBlank(fileName)) return false;
        File file = new File(fileName);
        if (file.exists()) return true;
        return false;
    }

    /***
     * @Date 2018/3/22
     * @param fileName 文件名称
     * @Description 1、传入参数是相对路径时，文件存储位置：／src/main/resources＋相对路径；
     *              2、传入参数是绝对路径时，文件存储本地。
     * @return File
     * @author zhanghesheng
     * */
    public static File creatFile(String fileName) {
        File file = null;
        if (fileName == null || StringUtils.isBlank(fileName)) {
            return file;
        }
        if (isExists(fileName)) {
            file = new File(fileName);
        } else {
            //文件名
            String name = null;
            //文件路径
            String filePath = null;
            //默认路径：工程目录下／src/main/resources
            String dir = System.getProperty(USER_DIR) + ROOT_SOURCE;
            //绝对路径
            if (DIR_SEPARATOR_UNIX.equalsIgnoreCase(fileName.substring(0, 1)) || DIR_SEPARATOR_WINDOWS.equalsIgnoreCase(fileName.substring(0, 1))) {
                if (fileName.contains(DIR_SEPARATOR_UNIX) && !fileName.contains(DIR_SEPARATOR_WINDOWS)) {
                    name = fileName.substring(fileName.lastIndexOf(DIR_SEPARATOR_UNIX) + 1);
                    filePath = fileName.substring(0, fileName.lastIndexOf(DIR_SEPARATOR_UNIX));
                } else if (fileName.contains(DIR_SEPARATOR_WINDOWS) && !fileName.contains(DIR_SEPARATOR_UNIX)) {
                    name = fileName.substring(fileName.lastIndexOf(DIR_SEPARATOR_WINDOWS) + 1);
                    filePath = fileName.substring(0, fileName.lastIndexOf(DIR_SEPARATOR_UNIX));
                }

            } else {//若是相对路径，存储位置为默认路径＋相对路径
                if (fileName.contains(DIR_SEPARATOR_UNIX)) {
                    name = fileName.substring(fileName.lastIndexOf(DIR_SEPARATOR_UNIX) + 1);
                    filePath = dir + fileName.substring(0, fileName.lastIndexOf(DIR_SEPARATOR_UNIX));
                } else if (fileName.contains(DIR_SEPARATOR_WINDOWS)) {
                    name = fileName.substring(fileName.lastIndexOf(DIR_SEPARATOR_WINDOWS) + 1);
                    filePath = dir + fileName.substring(0, fileName.lastIndexOf(DIR_SEPARATOR_UNIX));
                } else {
                    name = fileName;
                }
            }
            if (filePath != null || StringUtils.isNotBlank(filePath)) {
                file = new File(filePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String realFileName = file.getPath() + DIR_SEPARATOR_UNIX + name;
                file = new File(realFileName);
            } else {
                file = new File(dir + name);
            }
            //create new File
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static int deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
            return 1;
        }
        return 0;
    }


    public static byte[] file2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(bos);
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            log.error("action=file2byte, occur exception", e);
        } catch (IOException e) {
            log.error("action=file2byte, occur exception", e);
        }
        return buffer;
    }


    /**解决乱码问题*/
    public static String toUtf8String(String s){
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if (c >= 0 && c <= 255){sb.append(c);}
            else{
                byte[] b;
                try { b = Character.toString(c).getBytes("utf-8");}
                catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**解决乱码问题*/
    protected String handleResponseBody(String content) {
        if(StringUtils.isBlank(content)) {
            return content;
        }
        if(content.contains("æ")) {
            try {
                content = new String(content.getBytes("ISO-8859-1"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        content = HtmlDecoder.decode(content);
        // todo……
        return content;
    }







    public static void main(String[] args) throws Exception {
        File file = creatFile("b/test.html");
        String canonicalPath = file.getCanonicalPath();
        System.out.println(canonicalPath);
        System.out.println(deleteFile(canonicalPath));
    }
}
