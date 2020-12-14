package com.hujinwen.tools;

import com.hujinwen.utils.RandomUtils;
import sun.security.action.GetPropertyAction;

import java.io.*;
import java.security.AccessController;

/**
 * Created by hu-jinwen on 2020/6/4
 * <p>
 * 加强版 RandomAccessFile，扩展了一些方法
 */
public class RandomAccessFilePlus extends RandomAccessFile {

    private final String lineSeparator = AccessController.doPrivileged(new GetPropertyAction("line.separator"));

    public RandomAccessFilePlus(String name, String mode) throws IOException {
        super(name, mode);
    }

    public RandomAccessFilePlus(File file, String mode) throws IOException {
        super(file, mode);
    }

    /**
     * 读取一行（使用指定编码）
     */
    public final String readLine(String charsetName) throws IOException {
        try (
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            int c = -1;
            boolean eol = false;

            while (!eol) {
                switch (c = read()) {
                    case -1:
                    case '\n':
                        eol = true;
                        break;
                    case '\r':
                        eol = true;
                        long cur = getFilePointer();
                        if ((read()) != '\n') {
                            seek(cur);
                        }
                        break;
                    default:
                        byteArrayOutputStream.write(c);
                        break;
                }
            }

            if ((c == -1) && (byteArrayOutputStream.size() == 0)) {
                return null;
            }
            return byteArrayOutputStream.toString(charsetName);
        }

    }


    /**
     * 新起一行
     */
    public void newLine() throws IOException {
        write(lineSeparator.getBytes());
    }


    /**
     * 插入字节
     */
    public void insert(byte[] bytes) throws IOException {
        final File cacheFile = File.createTempFile(RandomUtils.uuid(), "");
        try (
                final FileOutputStream outputStream = new FileOutputStream(cacheFile);
                final FileInputStream inputStream = new FileInputStream(cacheFile)
        ) {
            long currentPointer = this.getFilePointer();

            final byte[] buffer = new byte[1024];
            int read;
            while ((read = this.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            this.seek(currentPointer);
            this.write(bytes);
            currentPointer = this.getFilePointer();

            while ((read = inputStream.read(buffer)) > 0) {
                this.write(buffer, 0, read);
            }

            this.seek(currentPointer);
        }
        cacheFile.delete();
    }

}
