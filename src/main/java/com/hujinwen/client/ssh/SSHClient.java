package com.hujinwen.client.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.hujinwen.exceptions.ssh.SSHCmdExecException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by hu-jinwen on 2020/4/2
 */
public class SSHClient implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(SSHClient.class);

    private String host;

    private int port;

    private String user;

    private String password;

    private Connection connection;

    public SSHClient(String host, String user, String password) throws IOException {
        this.host = host;
        this.port = 22;
        this.user = user;
        this.password = password;
        sshConnect();
    }

    public SSHClient(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    /**
     * ssh连接设备
     */
    private void sshConnect() throws IOException {
        this.connection = new Connection(host, port);
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
     * @param timeout 超时时间（毫秒）
     */
    public String exec(String cmd, int maxLine, long timeout) throws SSHCmdExecException {
        final StringBuilder sb = new StringBuilder();
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread execThread = new Thread(() -> {
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
                    countDownLatch.countDown();
                    break;
                } catch (IllegalStateException ise) {
                    logger.warn("Connection broken! reconnect...", ise);
                    try {
                        sshConnect();
                    } catch (IOException ignored) {
                    }
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage(), ioe);
                } finally {
                    if (session != null) {
                        session.close();
                    }
                }
            }
        });
        execThread.start();
        try {
            final boolean isSuccess = countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
            if (!isSuccess) {
                execThread.interrupt();
                throw new SSHCmdExecException();
            }
        } catch (InterruptedException ignored) {
        }

        return sb.toString();
    }


    /**
     * 执行命令
     *
     * @param cmd 命令
     */
    public String exec(String cmd) throws SSHCmdExecException {
        return exec(cmd, 30, 30000);
    }

    /**
     * 执行命令
     *
     * @param cmd     命令
     * @param timeout 超时时间（毫秒）
     */
    public String exec(String cmd, long timeout) throws SSHCmdExecException {
        return exec(cmd, 30, timeout);
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
