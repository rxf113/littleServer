package common;

/**
 * 请求方式
 *
 * @author rxf113
 */

public enum RequestMethod {
    GET("GET"),
    POST("POST");

    RequestMethod(String val) {
        this.val = val;
    }

    private String val;

    public String getVal() {
        return val;
    }
}
