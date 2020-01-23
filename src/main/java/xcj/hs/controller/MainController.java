package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xcj.hs.dao.impl.NeoDaoImpl;
import xcj.hs.service.NeoService;

import java.util.HashMap;
import java.util.Map;


@Controller
public class MainController {


    /**
     * 登陆界面
     * @param model
     * @return
     */
    @GetMapping("/")
    public String loginPage(Model model) {
        return "user/login";
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



}
