package com.hujinwen.client.nano;

import com.hujinwen.entity.annotation.nano.Router;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.Response;

import java.io.IOException;


class NanoServerTest {


    public static void main(String[] args) throws IOException {
        final MyServer server = new MyServer(8848);
        server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

}


class MyServer extends NanoServer {

    public MyServer(int port) {
        super(port);
    }

    @Router("/Hello")
    private Response helloResponse(IHTTPSession session) {
        System.out.println();
        return Response.newFixedLengthResponse("hello response");
    }


}