package com.hujinwen.client.ssh;

import com.hujinwen.exceptions.ssh.SSHCmdExecException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class SSHClientTest {

    @Test
    void test() throws IOException {
        final SSHClient sshClient = new SSHClient("153.35.238.242", 20209, "root", "18656567215");

        try {
            final String resutl = sshClient.exec("curl http://httpbin.org/ip -x http://127.0.0.1:1888", 2000L);
            System.out.println();
        } catch (SSHCmdExecException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

}