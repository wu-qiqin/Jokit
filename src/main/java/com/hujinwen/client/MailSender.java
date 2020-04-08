//package com.hujinwen.client;
//
//import com.hujinwen.core.ApplicationContext;
//import com.hujinwen.entity.MailConf;
//import com.hujinwen.entity.MailRecipient;
//import com.hujinwen.entity.exceptions.MailSendException;
//import com.sun.mail.util.MailSSLSocketFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.security.GeneralSecurityException;
//import java.util.Properties;
//
///**
// * Created by joe on 2020/4/3
// * <p>
// * 邮件发送
// */
//public class MailSender {
//    private static final Logger logger = LoggerFactory.getLogger(SSHClient.class);
//
//    private static MailConf conf = ApplicationContext.getContext().getMailConf();
//
//    private static Session session;
//
//    private static MailSender sender;
//
//    private MailSender() throws GeneralSecurityException {
//        if (session == null) {
//            sessionInit();
//        }
//    }
//
//    public static MailSender getSender() throws GeneralSecurityException {
//        if (sender == null) {
//            sender = new MailSender();
//        }
//        return sender;
//    }
//
//
//    private void sessionInit() throws GeneralSecurityException {
//        Properties prop = new Properties();
//        prop.setProperty("mail.host", conf.getHost());
//        prop.setProperty("mail.port", String.valueOf(conf.getPort()));
//        prop.setProperty("mail.transport.protocol", "smtp");
//        prop.setProperty("mail.smtp.auth", "true");
//
//        if (conf.isTls()) {
//            MailSSLSocketFactory sslSocketFactory = new MailSSLSocketFactory();
//            sslSocketFactory.setTrustAllHosts(true);
//            prop.put("mail.smtp.ssl.enable", "true");
//            prop.put("mail.smtp.ssl.socketFactory", sslSocketFactory);
//        }
//        session = Session.getInstance(prop);
//        // 不打印发送debug日志
//        session.setDebug(false);
//    }
//
//    /**
//     * 发送html邮件
//     */
//    public boolean sendHtml(String subject, String content, MailRecipient... recipients) throws MailSendException {
//        return send(subject, content, "text/html;charset=UTF-8", recipients);
//    }
//
//
//    /**
//     * 发送邮件
//     */
//    public boolean send(String subject, String content, String contentType, MailRecipient... recipients) throws MailSendException {
//
//        Transport transport = null;
//        try {
//            transport = session.getTransport();
//            transport.connect(conf.getUsername(), conf.getPassword());
//
//            final MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(conf.getFrom()));
//            for (MailRecipient recipient : recipients) {
//                message.addRecipient(recipient.recipientType, recipient.address);
//            }
//            message.setSubject(subject);
//            message.setContent(content, contentType);
//            transport.sendMessage(message, message.getAllRecipients());
//        } catch (Exception e) {
//            throw new MailSendException(e);
//        } finally {
//            if (transport != null) {
//                try {
//                    transport.close();
//                } catch (MessagingException e) {
//                    logger.error("transport close failed!", e);
//                }
//            }
//        }
//        return true;
//    }
//
//
//}
