package handle;

import com.sun.net.httpserver.HttpHandler;
import common.RequestMethod;

/**
 * @author rxf113
 */
public interface CusHttpHandler extends HttpHandler {

    /**
     * 请求匹配路径: /api/list
     *
     * @return path
     */
    String path();

    /**
     * 请求匹配方法: POST / GET
     *
     * @return path
     */
    RequestMethod requestMethod();
}
