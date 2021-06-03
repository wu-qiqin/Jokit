package com.hujinwen.client.nano;

import com.hujinwen.entity.annotations.nano.Router;
import com.hujinwen.exceptions.nano.RouterHandleException;
import com.hujinwen.utils.ReflectUtils;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.Response;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by hu-jinwen on 12/17/20
 */
public class NanoServer extends NanoHTTPD {

    private static final Map<String, Method> ROUTER_MAPPING = new HashMap<>();

    public NanoServer(int port) {
        this(null, port);
    }

    public NanoServer(String hostname, int port) {
        super(hostname, port);
        loadRouter();
    }


    @Override
    public Response handle(IHTTPSession session) {
        String uri = session.getUri();

        Method method = ROUTER_MAPPING.get(uri);
        if (method != null) {
            try {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return (Response) method.invoke(this, session);
            } catch (Exception e) {
                throw new RouterHandleException(e);
            }
        }
        return Response.newFixedLengthResponse("Hello World!");
    }


    /**
     * 加载路由信息
     */
    private void loadRouter() {
        Method[] declaredMethods = ReflectUtils.getDeclaredMethods(this.getClass());

        for (Method method : declaredMethods) {
            // 没有路由注解的过滤
            Router router = method.getAnnotation(Router.class);
            if (router == null) {
                continue;
            }
            // 返回值过滤
            if (method.getReturnType() != Response.class) {
                continue;
            }
            // 参数过滤
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                continue;
            }
            if (parameterTypes[0] != IHTTPSession.class) {
                continue;
            }

            String value = router.value();
            ROUTER_MAPPING.put(value, method);
        }
    }


}
