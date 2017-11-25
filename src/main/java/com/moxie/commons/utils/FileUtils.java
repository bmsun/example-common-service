package com.moxie.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/***
 *
 *@Description
 * @author zhanghesheng
 * */
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

    public static File creatFile(String fileName) {
        File file = null;
        if (fileName == null || StringUtils.isBlank(fileName)) {
            return file;
        }
        if (isExists(fileName)) {
            file = new File(fileName);
        } else {
            String name = null;
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
                    file.mkdirs();
                }
                String realFileName = file.getPath() + DIR_SEPARATOR_UNIX + name;
                file = new File(realFileName);
            } else {
                file = new File(dir + name);
            }
            //create new File
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
