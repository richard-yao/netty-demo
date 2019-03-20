package com.richard.study.test.file;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/13 11:36
 * Description:
 */
public class FileCopyTest {

    private String filePath = null;
    private String copyFilePath = null;

    @Before
    public void initFilePath() {
        filePath = "E:\\var\\test.zip";
        copyFilePath = "E:\\var\\copy.zip";
        File copyFile = new File(copyFilePath);
        if (copyFile.exists()) {
            copyFile.delete();
        }
    }

    @Test
    public void testFileCopyWithCommonIo() {
        long startTime = System.currentTimeMillis();
        File file = new File(filePath);
        try {
            InputStream fileStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(new File(copyFilePath));
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = fileStream.read(bytes)) > -1) {
                outputStream.write(bytes, 0, length);
            }
            fileStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("Copy file with stream cost time: " + costTime);
    }

    @Test
    public void testFileCopyWithNioUtil() {
        long startTime = System.currentTimeMillis();
        File originFile = new File(filePath);
        File copyFile = new File(copyFilePath);
        try {
            // This copy method actually using newInputStream to read data and write into newOutputStream
            FileCopyUtils.copy(originFile, copyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("Copy file with Spring FileCopyUtils: " + costTime);
    }

    @Test
    public void testFileCopyWithNioChannel() {
        long startTime = System.currentTimeMillis();
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            FileOutputStream outputStream = new FileOutputStream(new File(copyFilePath));
            // Using FileChannel's transfer method to copy bytes
            inputChannel = inputStream.getChannel();
            outputChannel = outputStream.getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            inputStream.close();
            outputStream.close();
            inputChannel.close();
            outputChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("Copy file with Nio channel: " + costTime);
    }

    @Test
    public void testFileCopyWithFiles() {
        long startTime = System.currentTimeMillis();
        File originFile = new File(filePath);
        File copyFile = new File(copyFilePath);
        try {
            Files.copy(originFile.toPath(), copyFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("Copy file with Java Files: " + costTime);
    }
}
