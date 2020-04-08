package com.hujinwen.utils;

import java.text.MessageFormat;

/**
 * Created by joe on 2020/4/3
 * <p>
 * 邮件中的小工具
 */
public class MailTools {

    private static final MessageFormat TABLE_FORMAT = new MessageFormat("<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\"><thead><tr><th>host</th><th>status</th></tr></thead><tbody>{0}</tbody></table>");

    private static final MessageFormat TR_FORMAT = new MessageFormat("<tr><td>{0}</td><td>{1}</td></tr>");


    public static HtmlContent getHtmlContent() {
        return new HtmlContent();
    }

    public static abstract class MailContent {
        public abstract String getContent();
    }

    public static class HtmlContent extends MailContent {

        private final StringBuilder sb = new StringBuilder();

        public HtmlContent addTr(String[] trArgs) {
            sb.append(TR_FORMAT.format(trArgs));
            return this;
        }

        @Override
        public String getContent() {
            return TABLE_FORMAT.format(new Object[]{sb.toString()});
        }

    }


}
