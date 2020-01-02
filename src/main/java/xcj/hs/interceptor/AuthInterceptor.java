package xcj.hs.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@PropertySource(value = { "classpath:application.properties" })
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Value("server.port")
    String sss;
    public AuthInterceptor() {
    }


    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        String loginUserAccount = (String) request.getSession().getAttribute("loginUserAccount");
        String servletPath = request.getServletPath();
        if(StringUtils.isBlank(loginUserAccount)){
            response.sendRedirect("/gologin");
            return false;
        }
//        log.info("当前登陆用户：-"+loginUserAccount);
        //验证权限
        if(checkAuth(loginUserAccount,servletPath)){
            log.info("当前登陆用户：-"+loginUserAccount+"-------------------"+"拦截器通过URL："+servletPath);
            return true;
        }
        else {
            log.error("当前登陆用户：-"+loginUserAccount+"-------------------"+"拦截器拦截URL："+servletPath);
            handleNotAuthorized( request,  response,  handler);
            return false;
        }

//        this.handleNotAuthorized(request, response, handler);
//        return false;
    }

    protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
//        response.sendError(403);
        response.sendRedirect("/nopermission");
    }

    private boolean checkAuth(String loginUserAccount,String servletPath){
        //管理员有权访问所有页面
        if("admin".equals(loginUserAccount)){
            return true;
        }
        //只需登陆就能访问首页
        if(StringUtils.isNotBlank(loginUserAccount) && servletPath.equals("/index")){
            return true;
        }



        return false;
    }
}
