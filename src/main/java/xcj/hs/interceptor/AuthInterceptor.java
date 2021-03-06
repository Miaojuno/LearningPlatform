package xcj.hs.interceptor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import xcj.hs.biz.UserManager;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource(value = {"classpath:security.properties"})
public class AuthInterceptor extends HandlerInterceptorAdapter {

  @Autowired UserManager userManager;

  @Value("${Authority.student}")
  List<String> studentAuthority;

  @Value("${Authority.teacher}")
  List<String> teacherAuthority;

  @Value("${Authority.leader}")
  List<String> leaderAuthority;

  @Value("${Authority.admin}")
  List<String> adminAuthority;


  Map<String, List<String>> groupMap;
  List<String> studentUrls;
  List<String> teacherUrls;
  List<String> leaderUrls;
  List<String> adminUrls;
  List<String> allUrls;

  public AuthInterceptor() {}

  @SneakyThrows
  @PostConstruct
  void initUrls() {
    groupMap = new HashMap<>();
    Properties properties = new Properties();
    properties.load(AuthInterceptor.class.getClassLoader().getResourceAsStream("security.properties"));
    for (String key : properties.stringPropertyNames()) {
      if(key.contains(".") && key.split("\\.")[0].equals("Group")){
        groupMap.put(key.split("\\.")[1], Arrays.asList(properties.getProperty(key).trim().split(",")));
      }
    }
    studentUrls = new ArrayList<>();
    teacherUrls = new ArrayList<>();
    leaderUrls = new ArrayList<>();
    adminUrls = new ArrayList<>();
    allUrls = new ArrayList<>();

    for (String authority : studentAuthority) {
      studentUrls.addAll(groupMap.get(authority));
    }
    for (String authority : teacherAuthority) {
      teacherUrls.addAll(groupMap.get(authority));
    }
    for (String authority : leaderAuthority) {
      leaderUrls.addAll(groupMap.get(authority));
    }
    for (String authority : adminAuthority) {
      adminUrls.addAll(groupMap.get(authority));
    }

    for (Map.Entry<String, List<String>> item : groupMap.entrySet()) {
      allUrls.addAll(item.getValue());
    }
    allUrls = allUrls.stream().distinct().collect(Collectors.toList());
  }

  public final boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler)
      throws ServletException, IOException {
    String loginUserAccount = (String) request.getSession().getAttribute("loginUserAccount");
    String loginUserRole = (String) request.getSession().getAttribute("loginUserRole");
    String servletPath = request.getServletPath();
    if (StringUtils.isBlank(loginUserAccount)) {
      response.sendRedirect("/gologin");
      return false;
    }
    // 验证权限
    if (checkAuth(loginUserAccount, loginUserRole, servletPath)) {
      log.info("当前登陆用户：-" + loginUserAccount + "-------------------" + "拦截器通过URL：" + servletPath);
      return true;
    } else {
      log.error("当前登陆用户：-" + loginUserAccount + "-------------------" + "拦截器拦截URL：" + servletPath);
      handleNotAuthorized(request, response, handler);
      return false;
    }
  }

  protected void handleNotAuthorized(
      HttpServletRequest request, HttpServletResponse response, Object handler)
      throws ServletException, IOException {
    // response.sendError(403);
    response.sendRedirect("/nopermission");
  }

  private boolean checkAuth(String loginUserAccount, String loginUserRole, String servletPath) {
    // 系统管理员有权访问所有页面
    if ("superadmin".equals(loginUserAccount)) {
      return true;
    }

    List<String> permissionUrls =
        "学生".equals(loginUserRole)
            ? studentUrls
            : "教师".equals(loginUserRole)
                ? teacherUrls
                : "领导".equals(loginUserRole)
                    ? leaderUrls
                    : "管理员".equals(loginUserRole) ? adminUrls : null;


    if (!permissionUrls.contains(servletPath) && allUrls.contains(servletPath)) {
      if(!permissionUrls.contains("/"+servletPath.split("/")[1]+"/"+"*")){
        return false;
      }
    }
    return true;
  }
}
