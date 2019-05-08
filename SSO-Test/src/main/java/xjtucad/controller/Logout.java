package xjtucad.controller;

import xjtucad.SSOManager1;
import xjtucad.manager.ITokenManager;
import xjtucad.util.KeyName;
import xjtucad.util.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Logout extends HttpServlet {
    SSOManager1 ssoManager = SSOManager1.getInstance();
    ITokenManager tokenManager = ssoManager.getTokenManager();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入Login方法执行退出");
        HttpSession session = req.getSession();
        String auToken = (String) session.getAttribute("AU_TOKEN");
        //如果当前Session中存在登录信息，那么清除Session中的用户信息和对应的Token信息
        if(auToken!=null){
            tokenManager.removeUserInfo(auToken);
            session.removeAttribute(KeyName.TOKEN);
            session.removeAttribute(KeyName.USER);
        }
        //无论是否存在用户信息，都清除一下Cookie中的Token信息
        TokenUtil.clearToken(req,resp);
    }
}
