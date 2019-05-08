package xjtucad.model;

import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data implements Serializable {

    /**
     * 删除操作
     */
    public static final int OP_DELETE = 2;
    public static final int OP_UPDATE= 1;
    public static final int OP_CONNECT = 0;
    private int method = 0;
    private Map<String,String> userInfo = new HashMap<String,String>();
    private String token="";

    public Data(int method, Map<String, String> userInfo,String token) {
        this.method = method;
        this.token= token;
        this.userInfo = userInfo;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Map<String, String> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Map<String, String> userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static void scWriteAll(int op, List<Map<String,String>> maps, SocketChannel socketChannel){

    }

    @Override
    public String toString() {
        return "Data{" +
                "method=" + method +
                ", userInfo=" + userInfo +
                ", token='" + token + '\'' +
                '}';
    }
}
