package com.hujinwen.utils;

import java.io.*;

/**
 * Created by joe on 2020/7/7
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    /**
     * 深拷贝
     * （这里使用Jackson转换来做拷贝）
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T obj) throws IOException {
        final String objContent = JsonUtils.toString(obj);
        return (T) JsonUtils.toObj(objContent, obj.getClass());
    }

    /**
     * 对象序列化
     */
    public static void serializeObj(Serializable obj, String path) throws IOException {
        try (
                final FileOutputStream fileOutputStream = new FileOutputStream(path);
                final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(obj);
        }
    }

    /**
     * 对象反序列化
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserializeObj(String path) throws IOException, ClassNotFoundException {
        try (
                final FileInputStream fileInputStream = new FileInputStream(path);
                final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            return (T) objectInputStream.readObject();
        }
    }

}
