package utils;


/**
 * 简单的获取id
 *
 * @author rxf113
 */
public class IdWorker {

    public static long nextId() {
        return new Sequence(null).nextId();
    }
}
