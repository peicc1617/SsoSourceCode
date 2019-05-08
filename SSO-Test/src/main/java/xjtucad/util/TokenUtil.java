package xjtucad.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenUtil {
    public static void clearToken(HttpServletRequest request, HttpServletResponse response){
        //构造名为"AU_TOKEN"的cookie
        Cookie tokenCookie = new Cookie(KeyName.TOKEN,"");
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath("/");
        //构造名为"CAI_USER_INFO"的cookie
        Cookie userInfoCookie = new Cookie(KeyName.COOKIE_USER_INFO,"");
        userInfoCookie.setMaxAge(0);
        userInfoCookie.setPath("/");
        //返回给客户端
        response.addCookie(tokenCookie);
        response.addCookie(userInfoCookie);
        //移除session中名为"userInfo"的session信息
        request.getSession().removeAttribute(KeyName.USER);
    }
}
