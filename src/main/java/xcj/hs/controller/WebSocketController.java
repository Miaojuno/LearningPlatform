package xcj.hs.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xcj.hs.biz.FriendShipManager;
import xcj.hs.service.impl.WebSocketServer;
import xcj.hs.vo.FriendShipVo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ws/chat")
public class WebSocketController {
  @Autowired FriendShipManager friendShipManager;

  /**
   * 群发消息内容
   *
   * @param message
   * @return
   */
  @GetMapping("/sendAll")
  @ResponseBody
  public String sendAllMessage(String message) {
    try {
      WebSocketServer.BroadCastInfo(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "ok";
  }

  /**
   * 指定会话发消息
   *
   * @param message 消息内容
   * @param userAccount
   * @return
   */
  @GetMapping("/sendOne")
  @ResponseBody
  public String sendOneMessage(String message, String userAccount) {
    try {
      WebSocketServer.SendMessage(message, userAccount);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "ok";
  }

  @PostMapping("/messageRecordUpload")
  @ResponseBody
  public Map<String, Object> recordUpload(String fsId, String toAccount) {
    Map<String, Object> map = new HashMap<String, Object>();
    FriendShipVo friendShipVo = friendShipManager.findById(fsId);
    JSONObject jsonObject = new JSONObject();
    String message = jsonObject.toJSONString(friendShipVo);
    try {
      WebSocketServer.SendMessage(message, toAccount);
    } catch (IOException e) {
      e.printStackTrace();
    }
    map.put("success", true);
    return map;
  }
}
