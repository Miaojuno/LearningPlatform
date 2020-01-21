package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
}