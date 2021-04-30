package com.hujinwen.utils;

import com.hujinwen.entity.enums.ContentType;
import com.hujinwen.entity.enums.FileType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by hu-jinwen on 2020/4/9
 * <p>
 * 文件相关工具
 */
public class FileUtils {
    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    private static final Yaml YAML_LOADER = new Yaml();

    private static final Pattern WIN_FILENAME_NOT_CONTAINS_PATTERN = Pattern.compile("[/\\\\:*?<>|]");

    private static final Map<String, String> STREAM_TYPE_MAPPER = new HashMap<>();

    private static final Map<String, String> CONTENT_TYPE_MAPPER = new HashMap<>();

    static {
        for (FileType fileType : FileType.values()) {
            final String typeName = fileType.typeName;

            for (String code : fileType.streamCodes) {
                STREAM_TYPE_MAPPER.put(code, typeName);
            }
        }

        for (ContentType contentType : ContentType.values()) {
            CONTENT_TYPE_MAPPER.put(contentType.fileType, contentType.contentType);
        }
    }

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

    /**
     * 预测文件扩展名
     */
    public static String predictExtName(String filePath) {
        int index = filePath.lastIndexOf(".");
        if (index > 0) {
            return filePath.substring(index + 1);
        }
        return "";
    }

    /**
     * 预测文件扩展名
     * <p>
     * * 预测过后的 input stream 不可用
     */
    public static String predictExtName(InputStream inputStream) {
        try {
            byte[] bytes = new byte[5];
            if (inputStream.read(bytes, 0, bytes.length) > -1) {
                String fileCode = bytesToHexString(bytes);

                if (fileCode != null && fileCode.length() == 10) {
                    return STREAM_TYPE_MAPPER.get(fileCode.toLowerCase());
                }
            }
        } catch (Exception e) {
            logger.warn("Can't predict extract name from input stream!", e);
        }
        return "";
    }

    /**
     * 预测 http Content-Type
     * <p>
     * * 读取过后的 input stream 不可用
     */
    public static String predictContentType(InputStream inputStream) {
        final String extName = predictExtName(inputStream);
        if (StringUtils.isEmpty(extName)) {
            return ContentType.DEFAULT.contentType;
        }
        return predictContentType(extName);
    }

    /**
     * 预测 http Content-Type
     */
    public static String predictContentType(String extName) {
        final String contentType = CONTENT_TYPE_MAPPER.get(extName);
        return contentType == null ? ContentType.DEFAULT.contentType : contentType;
    }

    private static String bytesToHexString(byte[] src) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (null == src || src.length < 1) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取resource绝对路径
     */
    public static String getResourcePath() {
        return Objects.requireNonNull(FileUtils.class.getClassLoader().getResource("")).getPath();
    }

    /**
     * 获取resource绝对路径
     */
    public static File getResourceFile(String filename) {
        File resourceFile = null;
        final URL url = FileUtils.class.getClassLoader().getResource(filename);
        if (url != null) {
            try {
                resourceFile = new File(url.toURI());
            } catch (URISyntaxException ignored) {
            }
        }
        return resourceFile;
    }

    /**
     * 以inputstream的形式读取resource下的文件
     */
    public static InputStream getResourceAsStream(String filename) {
        return FileUtils.class.getClassLoader().getResourceAsStream(filename);
    }

    /**
     * 读取properties文件内容，以properties对象返回
     */
    public static Properties readProperties(String filepath) throws IOException {
        final Properties prop = new Properties();
        try (
                final FileInputStream fileInputStream = new FileInputStream(filepath)
        ) {
            prop.load(fileInputStream);
        }
        return prop;
    }

    /**
     * 加载yaml文件
     */
    public static <T> T loadYamlFile(InputStream inputStream, Class<T> clazz) {
        return YAML_LOADER.loadAs(inputStream, clazz);
    }

}
