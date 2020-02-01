package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import xcj.hs.biz.RecordManager;
import xcj.hs.vo.RecordVo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/record")
public class RecordController {
  @Autowired RecordManager recordManager;

  /**
   * 做题界面
   *
   * @param model
   * @return
   */
  @GetMapping("/doQuestion")
  public String doQuestion(Model model) {
    return "question/doQuestion";
  }

  /**
   * 做题
   *
   * @param request
   * @param recordVo
   * @param file
   * @return
   */
  @PostMapping("/add")
  @ResponseBody
  public Map<String, Object> add(
      HttpServletRequest request, RecordVo recordVo, MultipartFile file) {
    Map<String, Object> map = new HashMap<>();
    try {
      if (file != null) {
        recordVo.setRecordUserPic(file.getBytes());
      }
      recordVo.setUserAccount((String) request.getSession().getAttribute("loginUserAccount"));
      recordManager.save(recordVo);
    } catch (Exception e) {
      map.put("success", false);
      map.put("msg", e.getMessage());
      return map;
    }
    map.put("success", true);
    return map;
  }

  /**
   * 老师批改题目界面
   *
   * @param model
   * @return
   */
  @GetMapping("/questionReview")
  public String questionReview(Model model) {
    return "question/questionReview";
  }

  /**
   * 获取待批改题目
   *
   * @param userAccount
   * @return
   */
  @PostMapping("/getOneUnreviewed")
  @ResponseBody
  public Map<String, Object> getOneUnreviewed(String userAccount) {
    Map<String, Object> map = new HashMap<>();
    RecordVo recordVo = recordManager.getOneUnreviewed(userAccount);
    if (recordVo == null) {
      map.put("success", false);
      return map;
    }
    map.put("data", recordVo);
    map.put("success", true);
    return map;
  }

  /**
   * 获取最近15日记录
   *
   * @param request
   * @return
   */
  @PostMapping("/get15daysRecordData")
  @ResponseBody
  public Map<String, Object> get15daysRecordData(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<>();

    map.put(
        "data",
        recordManager.get15daysRecordData(
            (String) request.getSession().getAttribute("loginUserAccount")));
    map.put("success", true);
    return map;
  }
}
