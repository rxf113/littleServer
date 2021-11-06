package handle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import common.CusResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.CusHttpServer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 转发处理
 *
 * @author rxf113
 */
public class DispatcherHandler implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherHandler.class);

    /**
     * key: 方法+路径    value: CusHttpHandler
     * eg: key: post#/api/xxx   value: handler
     */
    Map<String, CusHttpHandler> httpHandlers;

    public DispatcherHandler(List<CusHttpHandler> httpHandlers) {
        this.httpHandlers = httpHandlers.stream().collect(Collectors.toMap(i -> i.requestMethod() + "#" + i.path(), i -> i));
    }


    @Override
    public void handle(HttpExchange exchange) {
        String uri = exchange.getRequestURI().getRawPath();
        logger.info("accept request uri: {}", uri);
        String requestMethod = exchange.getRequestMethod();
        String key = requestMethod + "#" + uri;
        //根据uri 转发 post#/api/xxx
        CusHttpHandler cusHttpHandler = httpHandlers.get(key);
        if (cusHttpHandler != null) {
            try {
                cusHttpHandler.handle(exchange);
            } catch (Exception e) {
                CusResponseEntity.error(exchange);

            }
        } else {
            //响应 404
            CusResponseEntity.notFound(exchange);
        }
    }
}
