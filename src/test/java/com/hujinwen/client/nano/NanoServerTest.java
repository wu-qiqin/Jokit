package com.hujinwen.client.nano;

import com.hujinwen.entity.annotation.nano.Router;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Router("/genShield")
    private Response helloResponse(IHTTPSession session) {
        try {
            session.parseBody(new HashMap<>());
        } catch (Exception ignored) {
        }

        final Map<String, List<String>> parameters = session.getParameters();
        if (!parameters.containsKey("url") || !parameters.containsKey("xy_common_params")) {
            return Response.newFixedLengthResponse("Miss params!");
        }
        final List<String> urlList = parameters.get("url");
        final List<String> paramList = parameters.get("xy_common_params");
        if (urlList.isEmpty() || paramList.isEmpty()) {
            return Response.newFixedLengthResponse("Miss params!");
        }
        String url = urlList.get(0);
        String xy_common_params = paramList.get(0);
        return Response.newFixedLengthResponse("{数据}" + url + xy_common_params);
    }


}