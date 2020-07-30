package com.hujinwen.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by joe on 2020/4/8
 * <p>
 * url相关工具类
 */
public class UrlUtils {

    /**
     * 绝对路径抽取修改
     */
    public static String absUrl(String baseUrl, String href) {
        String result;

        if (StringUtils.isEmpty(href)) {
            return "";
        }

        if (href.startsWith("http:") || href.startsWith("https:")) {
            return href;
        }

        if (StringUtils.isEmpty(baseUrl) || href.equals("javascript;;")) {
            return "";
        }

        int index = baseUrl.indexOf("?");
        if (index > 0) {
            baseUrl = baseUrl.substring(0, index);
        }

        if (href.startsWith("//")) {
            result = baseUrl.substring(0, baseUrl.indexOf("//")) + href;
        } else if (href.startsWith("/")) {
            result = extractDomainUrl(baseUrl) + href;
        } else if (href.startsWith("../")) {
            while (href.startsWith("../")) {
                baseUrl = runToParent(baseUrl);
                href = href.substring(3);
            }
            result = baseUrl + href;
        } else if (href.startsWith("./")) {
            // 后面可能还会出现../
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/") + 1);
            href = href.substring(2);
            while (href.startsWith("../")) {
                baseUrl = runToParent(baseUrl);
                href = href.substring(3);
            }
            result = baseUrl + href;
        } else if (href.startsWith("?")) {
            // ?page=2&searchword=%C3%B0%CF%D5&searchtype=-1&searchtype1=
            result = baseUrl + href;
        } else {
            // https://www.52soku.cn/list.php
            // list.php?type=movie&rank=rankhot&cat=all&area=all&act=all&year=all&pageno=2
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/") + 1);
            result = baseUrl + href;
        }
        return result;
    }

    /**
     * 获取域名链接（包含https?://）
     */
    private static String extractDomainUrl(String baseUrl) {
        String result = "";
        int index1 = baseUrl.indexOf("://");
        if (index1 > -1) {
            int index2 = baseUrl.indexOf("/", index1 + 3);
            if (index2 > -1) {
                result = baseUrl.substring(0, index2);
            } else {
                result = baseUrl;
            }
        }
        return result;
    }

    /**
     * 链接截取到父路径
     */
    private static String runToParent(String baseUrl) {
        baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
        return baseUrl.substring(0, baseUrl.lastIndexOf("/") + 1);
    }

    /**
     * 抽取url中最后一段作为文件名
     *
     * @param url url
     */
    public static String extraFileName(String url) {
        int index = url.indexOf("?");
        if (index != -1) {
            url = url.substring(0, index);
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        int index1 = url.lastIndexOf("/");
        return url.substring(index1 + 1);
    }
}
