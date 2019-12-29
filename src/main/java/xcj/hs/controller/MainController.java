package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xcj.hs.biz.UserManager;
import xcj.hs.dao.NeoDaoImpl;
import xcj.hs.util.PageList;
import xcj.hs.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class MainController {
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
        return "main/page1";
    }

    @GetMapping("/wqinput")
    public String wqinput(Model model) {
        return "main/wqinput";
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


}
