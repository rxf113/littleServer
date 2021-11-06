package common;

/**
 * 常量
 *
 * @author rxf113
 */
public class Constant {

    /**
     * 项目路径
     */
    public final static String USER_DIR = System.getProperty("user.dir");

    /**
     * 系统分隔符
     */
    public final static String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * 存文件的文件夹路径
     */
    public final static String FOLDER_PATH = System.getProperty("folder_path", "files");

}
