package common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 自定义几个简单的响应
 *
 * @author rxf113
 */
public class CusResponseEntity {

    static ObjectMapper objectMapper = new ObjectMapper();


    public static class Entity {
        public Entity(String data, Integer code, String msg) {
            this.data = data;
            this.code = code;
            this.msg = msg;
        }

        public Entity() {
        }

        private String data;
        private Integer code;
        private String msg;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static void error(HttpExchange exchange) {
        Entity okEntity = new Entity(null, 500, "serverError");
        base(exchange, okEntity);
    }

    public static void error(HttpExchange exchange, Headers headers) {
        Entity okEntity = new Entity(null, 500, "serverError");
        base(exchange, okEntity, headers);
    }

    public static void notFound(HttpExchange exchange) {
        Entity okEntity = new Entity(null, 404, "fileNotFound");
        base(exchange, okEntity);
    }

    public static void notFound(HttpExchange exchange, Headers headers) {
        Entity okEntity = new Entity(null, 404, "fileNotFound");
        base(exchange, okEntity, headers);
    }


    public static void badRequest(HttpExchange exchange) {
        Entity okEntity = new Entity(null, 400, "bad request");
        base(exchange, okEntity);
    }

    public static void badRequest(HttpExchange exchange, Entity entity) {
        base(exchange, entity);
    }

    public static void badRequest(HttpExchange exchange, Headers headers) {
        Entity Entity = new Entity(null, 400, "bad request");
        base(exchange, Entity, headers);
    }

    public static void ok(HttpExchange exchange) {
        Entity okEntity = new Entity(null, 200, "success");
        base(exchange, okEntity);
    }

    public static void ok(HttpExchange exchange, Entity entity) {
        base(exchange, entity);
    }

    public static void ok(HttpExchange exchange, Headers headers) {
        Entity okEntity = new Entity(null, 200, "success");
        base(exchange, okEntity, headers);
    }


    public static void base(HttpExchange exchange, Entity entity) {
        Headers headers = new Headers();
        headers.put("Content-Type", new ArrayList<>(Collections.singletonList("application/json; charset=utf-8")));
        headers.put("Access-Control-Allow-Origin", new ArrayList<>(Collections.singletonList("*")));

        base(exchange, entity, headers);
    }

    public static void file(HttpExchange exchange, byte[] bytes, String fileName) {
        try {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.put("Access-Control-Allow-Origin", new ArrayList<>(Collections.singletonList("*")));
            responseHeaders.put("Content-Type", new ArrayList<>(Collections.singletonList("application/json; charset=utf-8")));
            responseHeaders.put("Content-Disposition", new ArrayList<>(Collections.singletonList("attachment; filename=" + fileName)));
            responseHeaders.put("Access-Control-Expose-Headers", new ArrayList<>(Collections.singletonList("Content-Disposition")));

            exchange.sendResponseHeaders(200, 0);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(bytes);
            responseBody.close();
        } catch (Exception e) {

        }


    }

    public static void base(HttpExchange exchange, Entity entity, Headers headers) {
        try {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.putAll(headers);
            //默认全都返回200,通过code判断具体状态码
            exchange.sendResponseHeaders(200, 0);
            byte[] bytes = objectMapper.writeValueAsBytes(entity);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(bytes);
            responseBody.close();
        } catch (Exception e) {

        }
    }
}
