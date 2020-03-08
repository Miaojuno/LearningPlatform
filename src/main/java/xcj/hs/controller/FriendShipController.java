package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xcj.hs.biz.FriendShipManager;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/friendShip")
public class FriendShipController {
  @Autowired FriendShipManager friendShipManager;

  /**
   * 好友关系主界面
   * @param model
   * @return
   */
  @GetMapping("/main")
  public String mainPage(Model model) {
    return "friendShip/main";
  }

  /**
   * user1向user2发起好友请求/user1接受user2发起的好友请求
   *
   * @param userAccount1
   * @param userAccount2
   * @return
   */
  @PostMapping("/addOrAccept")
  @ResponseBody
  public Map<String, Object> addOrAccept(String userAccount1, String userAccount2) {
    Map<String, Object> map = new HashMap<String, Object>();
    friendShipManager.activeShip(userAccount1, userAccount2);
    map.put("success", true);
    return map;
  }

  /**
   * 获取当前用户的激活状态好友
   *
   * @param userAccount
   * @return
   */
  @PostMapping("/getActive")
  @ResponseBody
  public Map<String, Object> getActive(String userAccount) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("success", true);
    map.put("data", friendShipManager.findAllFriendActive(userAccount));
    return map;
  }

  /**
   * 获取当前用户的请求状态好友
   *
   * @param userAccount
   * @return
   */
  @PostMapping("/getOnRequest")
  @ResponseBody
  public Map<String, Object> getOnRequest(String userAccount) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("success", true);
    map.put("data", friendShipManager.findAllFriendOnRequest(userAccount));
    return map;
  }
}
