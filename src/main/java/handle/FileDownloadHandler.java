package handle;

import com.sun.net.httpserver.HttpExchange;
import common.CusResponseEntity;
import common.RequestMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static common.Constant.*;

/**
 * 文件下载处理
 *
 * @author rxf113
 */
public class FileDownloadHandler implements CusHttpHandler {
    @Override
    public String path() {
        return "/api/download";
    }

    @Override
    public RequestMethod requestMethod() {
        return RequestMethod.GET;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, Object> map = new HashMap<>(2);
        for (String param : query.split("&")) {
            String[] split = param.split("=");
            map.put(split[0], split[1]);
        }
        String fileId = (String) map.get("fileId");
        if (fileId == null || "".equals(fileId.trim())) {
            CusResponseEntity.badRequest(exchange);
        }
        String fileName = getFullFileName(fileId);
        //下载文件
        File file = new File(USER_DIR + FILE_SEPARATOR + FOLDER_PATH + FILE_SEPARATOR + fileName);

        if (!file.exists()) {
            CusResponseEntity.notFound(exchange);
            return;
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        fileInputStream.close();
        CusResponseEntity.file(exchange, bytes, fileName);
    }

    private String getFullFileName(String fileId) {
        //下载txt文件
        return fileId + ".txt";
    }
}
