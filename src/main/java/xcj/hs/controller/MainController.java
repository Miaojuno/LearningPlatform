package xcj.hs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

  /**
   * 登陆界面
   *
   * @param model
   * @return
   */
  @GetMapping("/")
  public String loginPage(HttpServletRequest request, Model model) {
    if (request.getSession().getAttribute("loginUserAccount") == null) {
      return "user/login";
    } else {
      return "index";
    }
  }

  @GetMapping("/gologin")
  public String gologin(Model model) {
    return "main/autoRedirect";
  }

  @RequestMapping("/nopermission")
  public String nopermission(Model model) {
    return "main/nopermission";
  }

  /**
   * 首页
   *
   * @param model
   * @return
   */
  @GetMapping("/index")
  public String index(Model model) {
    return "main/index";
  }

  @GetMapping("/page1")
  public String page1(Model model) {
    return "nopermission";
  }

  @GetMapping("/wsTest")
  public String wsTest(Model model) {
    return "main/wsTest";
  }
}
