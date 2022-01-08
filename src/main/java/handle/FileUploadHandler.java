package handle;

import com.sun.net.httpserver.HttpExchange;
import common.CusResponseEntity;
import common.RequestMethod;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CusFileUtil;
import utils.IdWorker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static common.Constant.*;


/**
 * 处理文件上传，文件保存到当前项目文件夹下的files文件夹里
 *
 * @author rxf113
 */
public class FileUploadHandler implements CusHttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadHandler.class);

    @Override
    public String path() {
        return "/api/upload";
    }

    @Override
    public RequestMethod requestMethod() {
        return RequestMethod.POST;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //获取文件
        List<FileItem> fileItems = CusFileUtil.getFileItems(exchange);
        //限制只能一个文件
        if (fileItems.size() != 1) {
            //响应400
            CusResponseEntity.badRequest(exchange);
            return;
        }
        FileItem fileItem = fileItems.get(0);
        String name = fileItem.getName();
        String saveName;
        File fileFolder = new File(USER_DIR + FILE_SEPARATOR + FOLDER_PATH);
        if (!fileFolder.exists()) {
            if (!fileFolder.mkdirs()) {
                logger.error("mkdir failed, fileFolder: {}",fileFolder.getName());
            }
        }
        File file = new File(USER_DIR + FILE_SEPARATOR + FOLDER_PATH + FILE_SEPARATOR + (saveName = getSaveFileName(name)));
        if (!file.exists()) {
            if (file.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileItem.getInputStream().transferTo(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } else {
                logger.error("create failed, file: {}",file.getName());
            }
        }
        logger.info("received file [{}], save file [{}]", name, saveName);
        //响应
        CusResponseEntity.Entity entity = new CusResponseEntity.Entity(saveName, 200, "上传成功!");
        CusResponseEntity.ok(exchange, entity);

    }

    private String getSaveFileName(String name) {
        String[] split = name.split("\\.");
        return IdWorker.nextId() + "." + split[1];
    }

}
