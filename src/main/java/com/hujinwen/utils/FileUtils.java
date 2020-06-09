package com.hujinwen.utils;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Created by joe on 2020/4/9
 * <p>
 * 文件相关工具
 */
public class FileUtils {

    private static final Pattern WIN_FILENAME_NOT_CONTAINS_PATTERN = Pattern.compile("[/\\\\:*?<>|]");

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

    /**
     * 替换掉文件名中，windows不允许出现的字符
     *
     * @param filename 文件名
     * @return 替换后的文件名
     */
    public static String replaceCharNotAllowInWinFilename(String filename) {
        return WIN_FILENAME_NOT_CONTAINS_PATTERN.matcher(filename).replaceAll("");
    }

}
