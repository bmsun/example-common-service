package com.example.commons.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;

/***
 * @Date 2017/11/25
 *@Description 文件解压缩与压缩工具类
 * @author zhanghesheng
 * */
public class FileCompressUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileCompressUtils.class);

    /**
     * @param srcPath  源文件路径
     * @param pass     密令
     * @return 目标文件
     * @author zhanghesheng
     * @date 2017/11/25
     * @Description zip方式加密压缩
     */
    public static File encryptZip(String srcPath,  String pass) throws ZipException {
      return encryptZip(srcPath,srcPath,pass);
    }


    /**
     * @param srcPath  源文件路径
     * @param destPath 目标文件路径
     * @param pass     密令
     * @return 目标文件
     * @author zhanghesheng
     * @date 2017/11/25
     * @Description zip方式加密压缩
     */
    public static File encryptZip(String srcPath, String destPath, String pass) throws ZipException {
        File srcDir = new File(srcPath);
        if (!srcDir.exists()) {
            LOGGER.error("source file path not exists. srcPath=" + srcPath);
            return null;
        }

        File destDir = new File(destPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        if (destDir.isFile()) {
            LOGGER.error("destPath expected a directory, but is an exists file");
            return null;
        }
        String destStr = destDir + ".zip";
        File destFile = new File(destStr);
        ZipFile zipFile = new ZipFile(destFile);

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        //有密码时，加密
        if(StringUtils.isNotBlank(pass)){
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setPassword(pass);
        }
        zipFile.addFolder(srcPath, parameters);
        return zipFile.getFile();
    }

    /**
     * @param downloadFile 源文件路径
     * @param targetFile   目标文件路径
     * @param pass         密令
     * @return 目标文件
     * @author zhanghesheng
     * @date 2017/11/25
     * @Description zip方式解压缩加密文件
     */
    public static void encryptUnZip(String downloadFile, String targetFile, String pass) throws ZipException {
        ZipFile zipFile = new ZipFile(downloadFile);
        File destDir = new File(targetFile);     // 解压目录
        if (destDir.isDirectory() && !destDir.exists()) {
            destDir.mkdir();
        }
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(pass);
        }
        zipFile.extractAll(targetFile);
    }

    /**
     * 解压tar.gz 文件
     *
     * @param file      要解压的tar.gz文件对象
     * @param outputDir 要解压到某个指定的目录下
     * @throws IOException
     */
    public static File unTarGz(File file, String outputDir) throws IOException {
        TarInputStream tarIn = null;
        File tmpFile =null;
        try {
            tarIn = new TarInputStream(new GZIPInputStream(
                    new BufferedInputStream(new FileInputStream(file))),
                    1024 * 2);

            createDirectory(outputDir, null);//创建输出目录

            TarEntry entry = null;
            while ((entry = tarIn.getNextEntry()) != null) {
                if (entry.isDirectory()) {//是目录
                    entry.getName();
                    createDirectory(outputDir, entry.getName());//创建空目录

                } else {//是文件
                     tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/", null);//创建输出目录
                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(tmpFile);
                        int length = 0;
                        byte[] b = new byte[2048];
                        while ((length = tarIn.read(b)) != -1) {
                            out.write(b, 0, length);
                        }
                    } catch (IOException ex) {
                        throw ex;
                    } finally {
                        if (out != null)
                            out.close();
                    }
                }
            }
        } catch (IOException ex) {
            throw new IOException("解压归档文件出现异常", ex);
        } finally {
            try {
                if (tarIn != null) {
                    tarIn.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭tarFile出现异常", ex);
            }
        }
        return tmpFile;
    }


    public static void unGzipFile(String sourcedir) {
        String ouputfile = "";
        try {
            //建立gzip压缩文件输入流
            FileInputStream fin = new FileInputStream(sourcedir);
            //建立gzip解压工作流
            GZIPInputStream gzin = new GZIPInputStream(fin);
            //建立解压文件输出流
            ouputfile = sourcedir.substring(0, sourcedir.lastIndexOf('.'));
            FileOutputStream fout = new FileOutputStream(ouputfile);

            int num;
            byte[] buf = new byte[1024];

            while ((num = gzin.read(buf, 0, buf.length)) != -1) {
                fout.write(buf, 0, num);
            }

            gzin.close();
            fout.close();
            fin.close();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return;
    }


    @Deprecated
    public static File zip(String srcPath, String destPath) {
        File srcDir = new File(srcPath);
        if (!srcDir.exists()) {
            LOGGER.error("source file path not exists. srcPath=" + srcPath);
            return null;
        }

        File destDir = new File(destPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        if (destDir.isFile()) {
            LOGGER.error("destPath expected a directory, but is an exists file");
            return null;
        }

        File destFile = new File(destDir, srcDir.getName() + ".zip");

        Project project = new Project();
        Zip zip = new Zip();
        zip.setProject(project);
        zip.setDestFile(destFile);

        FileSet fileSet = new FileSet();
        fileSet.setProject(project);
        fileSet.setDir(srcDir);

        zip.addFileset(fileSet);
        zip.execute();
        LOGGER.info("compress files success. " + srcPath + "==>" + destFile.getAbsolutePath());

        return destFile;
    }

    @Deprecated
    private static void unZip(String srcZipFilePath, String destUnzipDir) throws Exception {

        InputStream in = null;
        FileOutputStream out = null;
        try {

            File destDir = new File(destUnzipDir);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(srcZipFilePath);
            java.util.Enumeration item = zipFile.getEntries();
            org.apache.tools.zip.ZipEntry zipEntry = null;
            while (item.hasMoreElements()) {
                zipEntry = (org.apache.tools.zip.ZipEntry) item.nextElement();
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    File f = new File(destDir, name);
                    f.mkdirs();
                } else {
                    String fileName = zipEntry.getName();
                    if (fileName.contains(File.separator)) {
                        String subDirStr = fileName.substring(0, fileName.lastIndexOf(File.separator));
                        if (!StringUtils.isBlank(subDirStr)) {
                            File subDir = new File(destUnzipDir, subDirStr);
                            subDir.mkdirs();
                        }
                    }

                    File f = new File(destDir, fileName);

                    try {
                        in = zipFile.getInputStream(zipEntry);
                        out = new FileOutputStream(f);

                        byte[] by = new byte[4096];
                        int c;
                        while ((c = in.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                    } catch (Exception ex) {
                        LOGGER.error("Unzip file error.", ex);
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {

                            }
                        }

                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e) {
                            }
                        }
                    }

                }
            }
        } catch (Exception ex) {
            LOGGER.error("Unzip file error.", ex);
        }
    }

    /**
     * 构建目录
     *
     * @param outputDir
     * @param subDir
     */
    public static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }

    public static void main(String[] args) throws Exception {
        //File file = new File("/tmp/pack/uploads.tar.gz");
       // unTarGz(file,"/tmp");

        unGzipFile("/tmp/pack/uploads.gzip");
    }


}
