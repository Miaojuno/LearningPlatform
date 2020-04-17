package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xcj.hs.biz.ApplyManager;
import xcj.hs.vo.ApplyVo;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/apply")
public class ApplyController {
  @Autowired ApplyManager applyManager;

  /**
   * 修改角色申请
   *
   * @param applyVo
   * @return
   */
  @PostMapping("/modifyRoleApply")
  @ResponseBody
  public Map<String, Object> modifyRoleApply(ApplyVo applyVo) {
    Map<String, Object> map = new HashMap<String, Object>();
    applyManager.modifyRoleApply(applyVo);
    map.put("success", true);
    return map;
  }

  /**
   * 修改上级申请
   *
   * @param applyVo
   * @return
   */
  @PostMapping("/modifySuperiorApply")
  @ResponseBody
  public Map<String, Object> modifySupeiorApply(ApplyVo applyVo) {
    Map<String, Object> map = new HashMap<String, Object>();
    applyManager.modifySupeiorApply(applyVo);
    map.put("success", true);
    return map;
  }

  /**
   * 上级变更审核界面
   *
   * @param model
   * @return
   */
  @GetMapping("/superiorApplyReview")
  public String superiorApplyReview(Model model) {
    return "apply/superiorApplyReviewList";
  }

  @RequestMapping("/superiorApplyPageList.json")
  @ResponseBody
  public Map<String, Object> superiorApplyPageList(
      String userAccount, String status, Integer pageSize, Integer pageNumber) {
    Map<String, Object> resultMap = new HashMap();
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
    Page<ApplyVo> pages = applyManager.superiorApplyPageFind(userAccount, status, pageable);

    resultMap.put("data", pages.getContent());
    resultMap.put("total", pages.getTotalElements());
    return resultMap;
  }

  /**
   * 角色变更审核界面
   *
   * @param model
   * @return
   */
  @GetMapping("/roleApplyReview")
  public String roleApplyReview(Model model) {
    return "apply/roleApplyReviewList";
  }

  @RequestMapping("/roleApplyPageList.json")
  @ResponseBody
  public Map<String, Object> roleApplyPageList(
      String status, Integer pageSize, Integer pageNumber) {
    Map<String, Object> resultMap = new HashMap();
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
    Page<ApplyVo> pages = applyManager.roleApplyPageFind(status, pageable);

    resultMap.put("data", pages.getContent());
    resultMap.put("total", pages.getTotalElements());
    return resultMap;
  }

  /**
   * pass上级申请
   *
   * @param applyId
   * @param isPass
   * @return
   */
  @PostMapping("/passApply")
  @ResponseBody
  public Map<String, Object> passApply(String applyId, String isPass) {
    Map<String, Object> map = new HashMap<String, Object>();
    applyManager.passApply(applyId, isPass);
    map.put("success", true);
    return map;
  }
}
