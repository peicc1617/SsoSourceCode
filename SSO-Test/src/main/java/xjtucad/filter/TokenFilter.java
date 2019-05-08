package xjtucad.filter;


import xjtucad.SSOManager1;
import xjtucad.manager.ITokenManager;
import xjtucad.util.KeyName;
import xjtucad.util.TokenUtil;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
//暂未启用
public class TokenFilter implements Filter {
    //获取SSOManager实例
    SSOManager1 ssoManager= SSOManager1.getInstance();
    //获取tokenManager实例
    ITokenManager tokenManager=ssoManager.getTokenManager();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入TokenFilter过滤器");
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpResponse =(HttpServletResponse)servletResponse;
        HttpSession session = httpRequest.getSession();
        String token=null;
        Cookie[] cookies = httpRequest.getCookies();
        if(cookies!=null&&cookies.length>0){
            for (Cookie cookie:cookies) {
                if(cookie.getName().equals(KeyName.TOKEN)){
                    //获取名为"AU_TOKEN"的cookie的值
                    token=cookie.getValue();
                    //验证token的有效性
                    if(!tokenManager.containToken(token)){
                        token=null;
                    }
                    break;
                }
            }

        }
        if(token==null){
            //tokenManager中没有对应的token（token失效）或者cookie中无token（之前未登录验证）
            TokenUtil.clearToken(httpRequest,httpResponse);
        }else{
            //有有效的token,设置session
            session.setAttribute(KeyName.USER,tokenManager.getUserInfo(token));
        }
        //放行
//        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("下一步重定向");
        httpResponse.sendRedirect("/webresources/redirect.html");
    }

    @Override
    public void destroy() {

    }
}
