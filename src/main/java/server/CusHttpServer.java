package server;

import com.sun.net.httpserver.HttpServer;
import handle.CusHttpHandler;
import handle.DispatcherHandler;
import handle.FileDownloadHandler;
import handle.FileUploadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 启动服务
 *
 * @author rxf113
 */
public class CusHttpServer {

    private static final Logger logger = LoggerFactory.getLogger(CusHttpServer.class);

    public static void main(String[] args) throws IOException {

        List<CusHttpHandler> list = new ArrayList<>();
        list.add(new FileUploadHandler());
        list.add(new FileDownloadHandler());
        int serverPort = 9999;
        if (args.length == 1) {
            serverPort = Integer.parseInt(args[0]);
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.setExecutor(getExecutor());
        //请求
        server.createContext("/", new DispatcherHandler(list));
        server.start();
        logger.info("=====服务启动 端口:[{}]=======", serverPort);
    }

    static AtomicInteger threadNum = new AtomicInteger(0);

    private static Executor getExecutor() {
        return new ThreadPoolExecutor(2,
                Runtime.getRuntime().availableProcessors(),
                60,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(8),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("littleServer executor - " + threadNum.getAndIncrement());
                    return thread;
                },
                (r, executor) -> {
                    if (!executor.isShutdown()) {
                        r.run();
                    }
                    logger.error("littleServer executor reject handler executed");
                });
    }
}
