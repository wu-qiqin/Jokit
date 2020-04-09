package com.hujinwen.utils;

import java.io.File;

/**
 * Created by joe on 2020/4/9
 * <p>
 * 文件相关工具
 */
public class FileUtils {

    /**
     * 创建文件夹，（检查后）
     *
     * @param path 路径
     */
    public static File checkAndMkdirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 删除一个文件
     *
     * @param fileName
     */
    public static boolean deleteFile(String fileName) {
        return deleteFile(new File(fileName));
    }

    public static boolean deleteFile(File file) {
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

}
