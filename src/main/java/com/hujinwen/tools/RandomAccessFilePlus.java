package com.hujinwen.tools;

import sun.security.action.GetPropertyAction;

import java.io.*;
import java.security.AccessController;
import java.util.ArrayList;

/**
 * Created by joe on 2020/6/4
 * <p>
 * 加强版的 RandomAccessFile
 */
public class RandomAccessFilePlus extends RandomAccessFile {

    private final String lineSeparator = AccessController.doPrivileged(new GetPropertyAction("line.separator"));

    private final ArrayList<Long> linePointerCache = new ArrayList<>();

    private long lineCount = 0;

    public RandomAccessFilePlus(String name, String mode) throws IOException {
        super(name, mode);
        calculateFile();
    }

    public RandomAccessFilePlus(File file, String mode) throws IOException {
        super(file, mode);
        calculateFile();
    }

    /**
     * 计算文件信息，行信息
     */
    private void calculateFile() throws IOException {
        lineCount = 0;
        linePointerCache.clear();

        boolean isNewLine = true;
        boolean end = false;

        while (!end) {
            if (isNewLine) {
                lineCount++;
                linePointerCache.add(getFilePointer());
                isNewLine = false;
            }

            switch (this.read()) {
                case -1:
                    end = true;
                    break;
                case '\r':
                    final long pointer = getFilePointer();
                    if (this.read() != '\n') {
                        this.seek(pointer);
                    }
                case '\n':
                    isNewLine = true;
                    break;
            }
        }
    }

    /**
     * 返回内容总行数
     */
    public long lineSize() {
        return lineCount;
    }

    /**
     * 获取当前游标所在行的行号
     */
    public long getLinePointer() throws IOException {
        final long currentPointer = getFilePointer();
        for (int i = 0; i < linePointerCache.size(); i++) {
            if (currentPointer <= linePointerCache.get(i)) {
                return i;
            }
        }
        return -1L;
    }

    /**
     * 跳到指定行的行首
     */
    public void seekLine(long linePos) throws IOException {
        if (linePos >= lineCount) {
            throw new IndexOutOfBoundsException("Beyond maximum row!");
        }
        this.seek(linePointerCache.get((int) linePos));
    }

    /**
     * 新起一行
     */
    public void newLine() throws IOException {
        write(lineSeparator.getBytes());
    }

    /**
     * 删除当前游标所在行
     */
    public void deleteLine() throws IOException {
        deleteLine(getLinePointer());
    }

    /**
     * 删除制定行
     */
    public void deleteLine(long linePointer) throws IOException {
        if (linePointer == lineCount - 1) {
            // 最后一行
            seekLine(linePointer);
            final long filePointer = this.getFilePointer();

            seek(filePointer - 2);
            if (this.read() == '\r') {
                seek(filePointer - 2);
            }
            this.setLength(this.getFilePointer());
            return;
        } else {
            final File tempFile = File.createTempFile("cache", "");
            try (
                    final FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
                    final FileInputStream tempInputStream = new FileInputStream(tempFile);
            ) {
                seekLine(linePointer + 1);

                final byte[] buffer = new byte[1024];
                int read;
                while ((read = this.read(buffer)) > 0) {
                    tempOutputStream.write(buffer, 0, read);
                }
                tempOutputStream.flush();

                seekLine(linePointer);
                while ((read = tempInputStream.read(buffer)) > 0) {
                    this.write(buffer, 0, read);
                }

                final long deletedLineLength = linePointerCache.get((int) linePointer + 1) - linePointerCache.get((int) linePointer);
                this.setLength(this.length() - deletedLineLength);
            }
            tempFile.delete();
        }
        calculateFile();
    }

    /**
     * 插入字节
     */
    public void insert(byte[] bytes) throws IOException {
        final File cacheFile = File.createTempFile("cache", "");
        try (
                final FileOutputStream outputStream = new FileOutputStream(cacheFile);
                final FileInputStream inputStream = new FileInputStream(cacheFile);
        ) {
            final long currentPointer = this.getFilePointer();

            final byte[] buffer = new byte[1024];
            int read;
            while ((read = this.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            this.seek(currentPointer);
            this.write(bytes);

            while ((read = inputStream.read(buffer)) > 0) {
                this.write(buffer, 0, read);
            }
        }
        cacheFile.delete();
    }

}
