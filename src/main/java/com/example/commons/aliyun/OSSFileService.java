package com.example.commons.aliyun;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * OSS文件下载服务,并解压文件
 */
public class OSSFileService {

    //aliyun提供
    private static String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    //aliyun提供
    private static String accessKeyId = "848bpHq8WF5fmxuU";
    //aliyun提供
    private static String accessKeySecret = "ig4BlVxdGyhLeD6wmtlOjQgL9hdGLz";
    private static String bucketName = "oper-bucket";
    private static String downloadDir = "/tmp/pack/downloads";
    private static String encodePass = "lskjfkji2ejskldfjiwoej234";

    private static Integer maxConnections = 100;
    private static Integer connectionTimeout = 5000;
    private static Integer maxErrorRetry = 3;
    private static Integer socketTimeout = 2000;

    /**
     * 初始化OSSClient
     *
     * @return
     */
    public static OSSClient init() {
        // 设置网络参数
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxConnections(maxConnections);
        clientConfiguration.setConnectionTimeout(connectionTimeout);
        clientConfiguration.setMaxErrorRetry(maxErrorRetry);
        clientConfiguration.setSocketTimeout(socketTimeout);

        // 初始化Oss Client
        return new OSSClient(endpoint, accessKeyId, accessKeySecret, clientConfiguration);
    }

    /**
     * 使用给定密码解压指定的ZIP压缩文件到指定目录
     *
     * @param downloadFile 指定的ZIP压缩文件路径
     * @param targetFile   解压目录
     * @param pass         ZIP文件的密码
     * @param taskid       任务ID
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static void encryptUnZip(String downloadFile, String targetFile, String pass, String taskid) throws ZipException {
        ZipFile zipFile = new ZipFile(downloadFile);

        // 解压目录
        File destDir = new File(targetFile);
        if (destDir.isDirectory() && !destDir.exists()) {
            destDir.mkdir();
        }
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(pass);
        } else {
            targetFile = String.format("%s/%s", targetFile, taskid);
        }
        zipFile.extractAll(targetFile);
    }

    /**
     * 创建一个文件夹
     *
     * @param destPath 文件夹路径
     */
    public static void createFile(String destPath) {
        File destDir = new File(destPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 下载taskid对应的文件下载并解压到相应目录下
     *
     * @param taskid  任务ID
     * @param fileDir 文件目录,不填默认/tmp/pack/downloads
     * @return
     */
    public static String downloadFile(String taskid, String fileDir) {
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSSClient ossClient = init();

        if (StringUtils.isBlank(fileDir)) {
            fileDir = downloadDir;
        }

        createFile(fileDir);

        String downloadFile = String.format("%s/%s.zip", fileDir, taskid);

        ossClient.getObject(new GetObjectRequest(bucketName, taskid), new File(downloadFile));

        try {
            encryptUnZip(downloadFile, fileDir, encodePass, taskid);
        } catch (ZipException e) {
            e.printStackTrace();
        }

        fileDir = String.format("%s/%s", fileDir, taskid);
        return fileDir;
    }

    /**
     * 下载taskid对应的文件下载并解压到默认/tmp/pack/downloads目录下
     *
     * @param taskid 任务ID
     * @return
     */
    public static String downloadFile(String taskid) {
        return downloadFile(taskid, null);
    }


    public static void main(String[] args) {
        String fileDirectory = downloadFile("efa7e940-d1c9-11e7-8ccf-00163e13e22b");
        try {
            //自动打开文件
            java.awt.Desktop.getDesktop().open(new File(fileDirectory));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
