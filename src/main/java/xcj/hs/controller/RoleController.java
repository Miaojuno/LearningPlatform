package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xcj.hs.biz.RoleManager;
import xcj.hs.biz.UserManager;
import xcj.hs.util.PageList;
import xcj.hs.vo.RoleVo;
import xcj.hs.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleManager roleManager;

    /**
     * 获取所有激活状态role
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public List<RoleVo> list() {
        return roleManager.getAllActiveRole();
    }


    /**
     * 角色界面
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "role/list";
    }



    @RequestMapping("/pageList.json")
    @ResponseBody
    public Map<String,Object>  listRole(RoleVo roleVo , Integer pageSize, Integer pageNumber, String searchText, HttpServletRequest request,
                                        HttpServletResponse response) {
        Map<String,Object> resultMap = new HashMap();
        PageList<RoleVo> pageList=new PageList<>();
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize);
        //暂时没有搜索功能
        List<RoleVo> list=roleManager.pageFind(roleVo,pageable);
//        List<RoleVo> resultList = pageList.getPageList(list,pageSize,pageNumber);

        int total = roleManager.getRoleNumber();
        resultMap.put("data",list);
        resultMap.put("total",total);
        return resultMap;
    }


    /**
     * 新增角色
     * @param roleVo
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Map<String ,Object > roleAdd( RoleVo roleVo) {
        Map<String, Object> map = new HashMap<String, Object>();
        if(roleManager.roleAdd(roleVo)){
            map.put("success",true);
            return map;
        }
        else {
            map.put("success",false);
            map.put("msg","当前角色名已经存在");
            return map;
        }
    }
}
