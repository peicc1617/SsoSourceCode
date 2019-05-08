package xjtucad.valve;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import xjtucad.SSOManager1;
import xjtucad.manager.ITokenManager;
import xjtucad.util.KeyName;
import xjtucad.util.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserValve extends ValveBase {
    SSOManager1 ssoManager = SSOManager1.getInstance();
    ITokenManager tokenManager = ssoManager.getTokenManager();
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        System.out.println("调用UserValve验证token信息");
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse =(HttpServletResponse)response;
        HttpSession session = httpRequest.getSession();
        String token=null;
        Cookie[] cookies = httpRequest.getCookies();
        if(cookies!=null&&cookies.length>0){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(KeyName.TOKEN)){
                    token=cookie.getValue();
                    if(!tokenManager.containToken(token)){
                        token=null;
                    }
                    break;
                }
            }
        }
        if(token==null){
            //如果没有token，那么默认是退出状态
            System.out.println("未找到Token");
            TokenUtil.clearToken(httpRequest,httpResponse);
        }else {
            //如果有Token，认为不是退出状态
            System.out.println("Token有效");
            session.setAttribute(KeyName.USER,tokenManager.getUserInfo(token));
        }
        getNext().invoke(request, response);
    }
}
