package xcj.hs.controller;

import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xcj.hs.biz.UserManager;
import xcj.hs.dao.NeoDaoImpl;
import xcj.hs.dao.NeoDaoImpl;
import xcj.hs.util.PageList;
import xcj.hs.util.PageResultForBootstrap;
import xcj.hs.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;


@Controller
public class UserController {
    @Autowired
    UserManager userManager;


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

    /**
     * 登陆操作 (成功后跳转至index)
     * @param request
     * @param model
     * @param userVo
     * @return
     */
    @PostMapping("/user/login")
    public String login(HttpServletRequest request,Model model, UserVo userVo) {
        if(userManager.loginCheck(userVo.getUserAccount(),userVo.getUserPwd())){
            request.getSession().setAttribute("loginUserAccount",userVo.getUserAccount());
            return "redirect:/index";
        }
        model.addAttribute("msg","账户或密码错误");
        return "user/login";
    }



    /**
     * 注销操作
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/user/logout")
    public String logout(HttpServletRequest request,Model model) {
        request.getSession().removeAttribute("loginUserAccount");
        return "redirect:/";
    }



    /**
     * 注册界面
     * @param model
     * @return
     */
    @GetMapping("/user/register")
    public String registerPage(Model model) {
        return "user/register";
    }

    /**
     * 注册操作
     * @param userVo
     * @return
     */
    @PostMapping("/user/register")
    @ResponseBody
    public Map<String ,Object > register( UserVo userVo) {
        Map<String, Object> map = new HashMap<String, Object>();
        if(userManager.register( userVo)){
            map.put("success",true);
        }
        else{
            map.put("success",false);
            map.put("msg","当前账户名已经存在");
        }
        return map;
    }

    /**
     * 修改操作
     * @param userVo
     * @return
     */
    @PostMapping("/user/modify")
    @ResponseBody
    public Map<String ,Object > modify( UserVo userVo) {
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            userManager.modify( userVo);
        }
        catch (Exception e){
            map.put("success",false);
            map.put("msg",e.getMessage());
            return map;
        }
        map.put("success",true);
        return map;
    }


    /**
     *
     * @param userId
     * @return
     */
    @PostMapping("/user/findbyid")
    @ResponseBody
    public Map<String ,Object > findbyid( String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        UserVo userVo=userManager.findById(userId);

        map.put("success",true);
        map.put("data",userVo);
        return map;
    }


    /**
     *  修改角色
     * @param userId
     * @param roleId
     * @return
     */
    @PostMapping("/user/modifyRole")
    @ResponseBody
    public Map<String ,Object > modifyRole( String userId,String roleId) {
        Map<String, Object> map = new HashMap<String, Object>();
        userManager.modifyRole(userId,roleId);
        map.put("success",true);
        return map;
    }

    /**
     * 重置密码
     * @param userId
     * @return
     */
    @PostMapping("/user/rePwd")
    @ResponseBody
    public Map<String ,Object > rePwd( String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        userManager.rePwd(userId);
        map.put("success",true);
        return map;
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @PostMapping("/user/delete")
    @ResponseBody
    public Map<String ,Object > deleteUser( String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        userManager.deleteUser(userId);
        map.put("success",true);
        return map;
    }


    /**
     * 用户界面
     * @param model
     * @return
     */
    @GetMapping("/user/list")
    public String list(Model model) {
        return "user/list";
    }



    @RequestMapping("/user/pageList.json")
    @ResponseBody
    public Map<String,Object>  listUser(UserVo userVo , Integer pageSize, Integer pageNumber, String searchText, HttpServletRequest request,
            HttpServletResponse response) {
        Map<String,Object> resultMap = new HashMap();
        PageList<UserVo> pageList=new PageList<>();
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize);
        List<UserVo> list=userManager.pageFind(userVo,pageable);
//        List<UserVo> resultList = pageList.getPageList(list,pageSize,pageNumber);

        int total = userManager.getActiveUserNumber();
        resultMap.put("data",list);
        resultMap.put("total",total);
        return resultMap;
    }
}
