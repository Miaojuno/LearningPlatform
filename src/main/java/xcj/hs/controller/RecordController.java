package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;
import xcj.hs.biz.RecordManager;
import xcj.hs.biz.UserManager;
import xcj.hs.entity.Record;
import xcj.hs.service.UserService;
import xcj.hs.vo.RecordVo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/record")
public class RecordController {
  @Autowired RecordManager recordManager;

  /**
   * 做题
   * @param request
   * @param recordVo
   * @param file
   * @return
   */
  @PostMapping("/add")
  @ResponseBody
  public Map<String, Object> add(HttpServletRequest request, RecordVo recordVo,  MultipartFile file) {
    Map<String, Object> map = new HashMap<String, Object>();
//    BASE64Encoder base64Encoder = new BASE64Encoder();
    try {
      if (file != null) {
//        String base64Pic = base64Encoder.encode(file.getBytes());
        recordVo.setUserPic(file.getBytes());
      }
      recordVo.setUserAccount((String) request.getSession().getAttribute("loginUserAccount"));
      recordManager.save(recordVo);
    } catch (IOException e) {
      map.put("success", false);
      map.put("msg", e.getMessage());
      return map;
    }
    map.put("success", true);
    return map;
  }
}
