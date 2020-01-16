package xcj.hs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xcj.hs.dao.NeoDaoImpl;

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


    /**
     * 导入（知识点 题目 关系）
     * @param model
     * @return
     */
    @GetMapping("/excelupload")
    public String excelupload(Model model) {
        return "main/excelupload";
    }

    @PostMapping("/excelupload")
    @ResponseBody
    public Map<String,Object> excelupload(@RequestParam("file")MultipartFile file, Model model) throws Exception {
        Map<String,Object> resultMap = new HashMap();
        String msg;
        try{
            msg=NeoDaoImpl.excelUpload(file);
        }
        catch (Exception e){
            resultMap.put("success",false);
            resultMap.put("msg",e.getMessage());
            return resultMap;
        }
        resultMap.put("success",true);
        resultMap.put("msg",msg);
        return resultMap;
    }


    /**
     * 导入题目
     * @param model
     * @return
     */
    @GetMapping("/questionupload")
    public String questionupload(Model model) {
        return "main/questionupload";
    }


    @PostMapping("/questionupload")
    @ResponseBody
    public Map<String,Object> questionupload(@RequestParam("file")MultipartFile file, Model model) throws Exception {
        Map<String,Object> resultMap = new HashMap();
        String msg;
        try{
            msg=NeoDaoImpl.questionUpload(file);
        }
        catch (Exception e){
            resultMap.put("success",false);
            resultMap.put("msg",e.getMessage());
            return resultMap;
        }
        resultMap.put("success",true);
        resultMap.put("msg",msg);
        return resultMap;
    }
}
