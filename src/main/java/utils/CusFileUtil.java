package utils;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.InputStream;
import java.util.List;

/**
 * 通过 apache.commons.fileupload 获取文件
 *
 * @author rxf113
 */
public class CusFileUtil {

    static FileUpload upload = new FileUpload(new DiskFileItemFactory());

    public static List<FileItem> getFileItems(HttpExchange exchange) {
        try {
            return upload.parseRequest(new RequestContext() {

                @Override
                public String getCharacterEncoding() {
                    return "UTF-8";
                }

                @Override
                public int getContentLength() {
                    return 0; //tested to work with 0 as return
                }

                @Override
                public String getContentType() {
                    return exchange.getRequestHeaders().getFirst("Content-type");
                }

                @Override
                public InputStream getInputStream() {
                    return exchange.getRequestBody();
                }

            });
        } catch (FileUploadException e) {
            throw new RuntimeException(e);
        }
    }
}
