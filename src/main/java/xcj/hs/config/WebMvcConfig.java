package xcj.hs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xcj.hs.interceptor.AuthInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Autowired
    AuthInterceptor authInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //静态资源、登陆注册界面 不拦截
        registry.addInterceptor(authInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/static/**","/user/login","/user/register","/user/logout",
                        "/**/webjars/**","/**/js/**","/error","/**/images/**","/**/css/**",
                        "/","/gologin","/nopermission");
    }
}
