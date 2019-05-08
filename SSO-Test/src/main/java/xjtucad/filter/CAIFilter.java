package xjtucad.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class CAIFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入CAIFilter");
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse rep = (HttpServletResponse)response;
        boolean jump = false;
        if (req.getRequestURI().indexOf("/CAI-") == 0) {
            Map<String, String> userInfo = (Map)req.getSession().getAttribute("userInfo");
            if (userInfo == null || userInfo.size() == 0) {
                String serviceURL = req.getRequestURL().toString();
                System.out.println("模板未登录，跳转至登录页面");
                System.out.println("我是新的");
                jump = true;
            }
        }

        if (jump) {
            rep.sendRedirect("/webresources/userLogin.jsp?serviceURL=" + req.getRequestURL());
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
