package com.hujinwen.client;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by joe on 2020/4/2
 */
public class SSHClient implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(SSHClient.class);

    private String host;

    private String user;

    private String password;

    private Connection connection;

    public SSHClient(String host, String user, String password) throws IOException {
        this.host = host;
        this.user = user;
        this.password = password;
        sshConnect();
    }

    /**
     * ssh连接设备
     */
    private void sshConnect() throws IOException {
        this.connection = new Connection(host);
        this.connection.connect();

        final boolean isAuthenticated = this.connection.authenticateWithPassword(user, password);
        if (!isAuthenticated) {
            throw new IOException("Authentication failed");
        }
        logger.info("SSH connected host -> {}", host);
    }

    /**
     * 执行命令
     *
     * @param cmd     命令
     * @param maxLine 最大读取行数（防止长时间读取耗时的情况）
     */
    public String exec(String cmd, int maxLine) throws IOException {
        final StringBuilder sb = new StringBuilder();

        Session session = null;
        int errCount = 0;
        while (errCount++ < 3) {
            try {
                session = connection.openSession();
                session.execCommand(cmd);
                if (session.getStderr().available() > 0) {
                    throw new IOException("Command execution failed! cmd -> " + cmd);
                }
                try (
                        final InputStream stdout = session.getStdout();
                        final InputStreamReader inputStreamReader = new InputStreamReader(stdout);
                        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                ) {
                    int readCount = 0;
                    String line;
                    while ((line = bufferedReader.readLine()) != null && readCount++ < maxLine) {
                        sb.append(line);
                        sb.append("\n");
                    }
                }
                break;
            } catch (IllegalStateException ise) {
                logger.warn("Connection broken! reconnect...", ise);
                sshConnect();
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 执行获取结果字符串
     */
    public String exec(String cmd) throws IOException {
        return exec(cmd, 30);
    }


    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void close() {
        if (connection != null) {
            connection.close();
        }
    }
}
