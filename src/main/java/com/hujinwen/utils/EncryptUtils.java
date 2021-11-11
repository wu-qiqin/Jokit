package com.hujinwen.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.BitSet;

/**
 * Created by hu-jinwen on 2020/4/9
 * <p>
 * 加密解密相关工具包
 */
public class EncryptUtils {
    private static final char[] base64EncodeChars =
            new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    private static final byte[] base64DecodeChars =
            new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
                    19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
                    -1, -1};

    private static final BitSet dontNeedEncoding;

    static {
        dontNeedEncoding = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding.set(i);
        }
        dontNeedEncoding.set('+');
        /**
         * 这里会有误差,比如输入一个字符串 123+456,它到底是原文就是123+456还是123 456做了urlEncode后的内容呢？<br>
         * 其实问题是一样的，比如遇到123%2B456,它到底是原文即使如此，还是123+456 urlEncode后的呢？ <br>
         * 在这里，我认为只要符合urlEncode规范的，就当作已经urlEncode过了<br>
         * 毕竟这个方法的初衷就是判断string是否urlEncode过<br>
         */
        dontNeedEncoding.set('-');
        dontNeedEncoding.set('_');
        dontNeedEncoding.set('.');
        dontNeedEncoding.set('*');
        dontNeedEncoding.set('~');
    }

    /**
     * 加密
     *
     * @param data 明文的字节数组
     * @return 密文字符串
     */
    public static String base64Encode(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

    /**
     * 解密
     *
     * @param str 密文
     * @return 明文的字节数组
     * @throws UnsupportedEncodingException
     */
    public static byte[] base64Decode(String str) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        byte[] data = str.getBytes(StandardCharsets.US_ASCII);
        int len = data.length;
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len) {
            /* b1 */
            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1)
                break;
            /* b2 */
            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1)
                break;
            sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
            /* b3 */
            do {
                b3 = data[i++];
                if (b3 == 61)
                    return sb.toString().getBytes("iso8859-1");
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1)
                break;
            sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
            /* b4 */
            do {
                b4 = data[i++];
                if (b4 == 61)
                    return sb.toString().getBytes("iso8859-1");
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1)
                break;
            sb.append((char) (((b3 & 0x03) << 6) | b4));
        }
        return sb.toString().getBytes("iso8859-1");
    }

    public static String base64DecodeString(String str) {
        String rs = "";
        try {
            byte[] decodeByte = base64Decode(str);
            rs = new String(decodeByte);
        } catch (UnsupportedEncodingException ignored) {
        }

        return rs;
    }

    /**
     * 字符串做md5加密（32位）
     */
    public static String md5(String str) {
        return md5(str, false);
    }

    /**
     * 字符串做md5加密（32位）
     */
    public static String md5(String str, boolean isUpperCase) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        try {
            return md5(str.getBytes(StandardCharsets.UTF_8), isUpperCase);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字节数组做md5加密
     */
    public static String md5(byte[] bytes) {
        return md5(bytes, false);
    }

    /**
     * 字节数组做md5加密
     */
    public static String md5(byte[] bytes, boolean isUpperCase) {
        StringBuilder sb = new StringBuilder();
        try {
            for (byte b : md5ToBytes(bytes)) {
                sb.append(String.format("%02X", b));  // 10进制字符转16进制
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final String md5Str = sb.toString();
        return isUpperCase ? md5Str : md5Str.toLowerCase();
    }

    /**
     * 字符串做md5加密（32位）
     */
    public static byte[] md5ToBytes(String str) {
        if (str == null) {
            return null;
        }
        try {
            return md5ToBytes(str.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串做md5加密（16位）
     */
    public static String md5Bit16(String str) {
        return md5(str).substring(8, 24);
    }


    public static byte[] md5ToBytes(byte[] bytes) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(bytes);
        return md5.digest();
    }

    public static String sha256(String str) {
        try {
            return ConvertUtils.bytesToHexStr(sha256(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] sha256(byte[] bytes) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes);
            return messageDigest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断str是否urlEncoder.encode
     * <p>
     * 支持JAVA的URLEncoder.encode出来的string做判断。 即: 将' '转成'+'
     * 0-9a-zA-Z保留
     * '-'，'_'，'.'，'*'保留
     * 其他字符转成%XX的格式，X是16进制的大写字符，范围是[0-9A-F]
     *
     * @param str 需判断的字符串
     * @return 结果
     */
    public static boolean isUrlEncoded(String str) {
        boolean needEncode = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (dontNeedEncoding.get((int) c)) {
                continue;
            }
            if (c == '%' && (i + 2) < str.length()) {
                // 判断是否符合urlEncode规范
                char c1 = str.charAt(++i);
                char c2 = str.charAt(++i);
                if (isDigit16Char(c1) && isDigit16Char(c2)) {
                    continue;
                }
            }
            // 其他字符，肯定需要urlEncode
            needEncode = true;
            break;
        }

        return !needEncode;
    }

    /**
     * 判断c是否是16进制的字符
     *
     * @param c 需判断的字符
     * @return boolean
     */
    public static boolean isDigit16Char(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F');
    }

}
