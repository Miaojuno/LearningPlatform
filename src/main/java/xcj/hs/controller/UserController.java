package xcj.hs.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xcj.hs.biz.UserManager;
import xcj.hs.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
  @Autowired UserManager userManager;

  /**
   * 登陆操作 (成功后跳转至index)
   *
   * @param request
   * @param model
   * @param userVo
   * @return
   */
  @PostMapping("/login")
  public String login(HttpServletRequest request, Model model, UserVo userVo) {
    if (userManager.loginCheck(userVo.getUserAccount(), userVo.getUserPwd())) {
      request.getSession().setAttribute("loginUserAccount", userVo.getUserAccount());
      request
          .getSession()
          .setAttribute("loginUserRole", userManager.getRoleName(userVo.getUserAccount()));
      return "redirect:/index";
    }
    model.addAttribute("msg", "账户或密码错误");
    return "user/login";
  }

  /**
   * 注销操作
   *
   * @param request
   * @param model
   * @return
   */
  @RequestMapping("/logout")
  public String logout(HttpServletRequest request, Model model) {
    request.getSession().removeAttribute("loginUserAccount");
    request.getSession().removeAttribute("loginUserRole");
    return "redirect:/";
  }

  /**
   * 注册界面
   *
   * @param model
   * @return
   */
  @GetMapping("/register")
  public String registerPage(Model model) {
    return "user/register";
  }

  /**
   * 注册操作
   *
   * @param userVo
   * @return
   */
  @CacheEvict(value = "userPageListCache", allEntries = true)
  @PostMapping("/register")
  @ResponseBody
  public Map<String, Object> register(UserVo userVo) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (userManager.register(userVo)) {
      map.put("success", true);
    } else {
      map.put("success", false);
      map.put("msg", "当前账户名已经存在");
    }
    return map;
  }

  /**
   * 修改操作
   *
   * @param userVo
   * @return
   */
  @CacheEvict(value = "userPageListCache", allEntries = true)
  @PostMapping("/modify")
  @ResponseBody
  public Map<String, Object> modify(UserVo userVo) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      userManager.modify(userVo);
    } catch (Exception e) {
      map.put("success", false);
      map.put("msg", e.getMessage());
      return map;
    }
    map.put("success", true);
    return map;
  }

  /**
   * @param userId
   * @return
   */
  @PostMapping("/findbyid")
  @ResponseBody
  public Map<String, Object> findbyid(String userId) {
    Map<String, Object> map = new HashMap<String, Object>();
    UserVo userVo = userManager.findById(userId);

    map.put("success", true);
    map.put("data", userVo);
    return map;
  }

  /**
   * 修改角色
   *
   * @param userId
   * @param roleId
   * @return
   */
  @CacheEvict(value = "userPageListCache")
  @PostMapping("/modifyRole")
  @ResponseBody
  public Map<String, Object> modifyRole(String userAccount, String roleId) {
    Map<String, Object> map = new HashMap<String, Object>();
    userManager.modifyRole(userAccount, roleId);
    map.put("success", true);
    return map;
  }

  /**
   * 修改上级
   *
   * @param subordinateId
   * @param superiorId
   * @return
   */
  @CacheEvict(value = "userPageListCache", allEntries = true)
  @PostMapping("/updateSuperior")
  @ResponseBody
  public Map<String, Object> updateSuperior(String subordinateId, String superiorId) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (userManager.updateSuperior(subordinateId, superiorId)) {
      map.put("success", true);
      return map;
    }
    map.put("success", false);
    return map;
  }

  /**
   * 重置密码
   *
   * @param userId
   * @return
   */
  @PostMapping("/rePwd")
  @ResponseBody
  public Map<String, Object> rePwd(String userId) {
    Map<String, Object> map = new HashMap<String, Object>();
    userManager.rePwd(userId);
    map.put("success", true);
    return map;
  }

  /**
   * 删除用户
   *
   * @param userId
   * @return
   */
  @CacheEvict(value = "userPageListCache", allEntries = true)
  @PostMapping("/delete")
  @ResponseBody
  public Map<String, Object> deleteUser(String userId) {
    Map<String, Object> map = new HashMap<String, Object>();
    userManager.deleteUser(userId);
    map.put("success", true);
    return map;
  }

  /**
   * 用户界面
   *
   * @param model
   * @return
   */
  @GetMapping("/list")
  public String list(Model model) {
    return "user/list";
  }

  /**
   * 分页查询
   *
   * @param userVo
   * @param pageSize
   * @param pageNumber
   * @return
   */
  @Cacheable(value = "userPageListCache")
  @RequestMapping("/pageList.json")
  @ResponseBody
  public Map<String, Object> listUser(UserVo userVo, Integer pageSize, Integer pageNumber) {
    Map<String, Object> resultMap = new HashMap();
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
    Page<UserVo> pages = userManager.pageFind(userVo, pageable);

    resultMap.put("data", pages.getContent());
    resultMap.put("total", pages.getTotalElements());
    return resultMap;
  }

  /**
   * 上级模块分页查询，根据所选角色返回结果
   *
   * @param userVo
   * @param pageSize
   * @param pageNumber
   * @param subordinateId
   * @return
   */
  @RequestMapping("/superiorPageList.json")
  @ResponseBody
  public Map<String, Object> superiorPageList(
      UserVo userVo,
      Integer pageSize,
      Integer pageNumber,
      String subordinateId,
      String subordinateAccount) {
    Map<String, Object> resultMap = new HashMap();
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
    if (StringUtils.isNotBlank(subordinateId) || StringUtils.isNotBlank(subordinateAccount)) {
      Page<UserVo> pages =
          userManager.superiorPageFind(userVo, subordinateId, subordinateAccount, pageable);
      if (pages==null){
          resultMap.put("data", null);
          resultMap.put("total", 0);
      }
      else {
          resultMap.put("data", pages.getContent());
          resultMap.put("total", pages.getTotalElements());
      }

    }
    return resultMap;
  }
}
