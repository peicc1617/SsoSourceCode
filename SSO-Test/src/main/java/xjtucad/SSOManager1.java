package xjtucad;

import xjtucad.manager.ITokenManager;
import xjtucad.manager.StandTokenManger;

public class SSOManager1 {
    static int mode=0;
    public final static  int SERVER_MODE=2;
    public final static int CLIENT_MODE = 1;
    public final static int STAND_MODE=0;
    ITokenManager tokenManager=null;
    static SSOManager1 ssoManager=new SSOManager1();
    private String ssoLoginUrl="";
    public static SSOManager1 getInstance(){
        return ssoManager;
    }
    private SSOManager1(){
        System.out.println("进入单例模式");
        tokenManager=new StandTokenManger();
        mode=STAND_MODE;
    }
    public ITokenManager getTokenManager(){
        return tokenManager;
    }
    public int getMode() {
        return mode;
    }
    //获取表单提交地址（可选，否则跳转默认提交地址）
    public String getSsoLoginUrl() {
        return ssoLoginUrl;
    }
}
