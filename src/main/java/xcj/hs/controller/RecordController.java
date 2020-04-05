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

  /**
   * 获取三种类型题目错误数
   *
   * @param request
   * @return
   */
  @PostMapping("/getErrorCountGroupByKind")
  @ResponseBody
  public Map<String, Object> getErrorCountGroupByKind(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<>();

    map.put(
        "data",
        recordManager.getErrorCountGroupByKind(
            (String) request.getSession().getAttribute("loginUserAccount")));
    map.put("success", true);
    return map;
  }

  @PostMapping("/updateScore")
  @ResponseBody
  public Map<String, Object> updateScore(String recordId, String score) {
    Map<String, Object> map = new HashMap<>();

    recordManager.updateScore(recordId, score);
    map.put("success", true);
    return map;
  }

  /**
   * 学生七天内状况
   *
   * @param request
   * @return
   */
  @PostMapping("/getSubordinateSituation")
  @ResponseBody
  public Map<String, Object> getSubordinateSituation(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<>();

    map.put(
        "data",
        recordManager.getSubordinateSituation(
            (String) request.getSession().getAttribute("loginUserAccount")));
    map.put("success", true);
    return map;
  }

  /**
   * getRecordByDiff
   *
   * @param request
   * @return
   */
  @PostMapping("/getRecordByDiff")
  @ResponseBody
  public Map<String, Object> getRecordByDiff(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<>();

    map.put(
        "data",
        recordManager.getRecordByDiff(
            (String) request.getSession().getAttribute("loginUserAccount")));
    map.put("success", true);
    return map;
  }

  /**
   * findByUserAccountAndQuestionId
   *
   * @param userAccount
   * @param questionId
   * @return
   */
  @PostMapping("/findByUserAccountAndQuestionId")
  @ResponseBody
  public Map<String, Object> findReofindByUserAccountAndQuestionIdcrd(
      String userAccount, String questionId) {
    Map<String, Object> map = new HashMap<>();
    RecordVo recordVo = recordManager.findByUserAccountAndQuestionId(userAccount, questionId);
    if (recordVo != null) {
      map.put("data", recordVo);
      map.put("success", true);
    } else {
      map.put("msg", "不存在");
      map.put("success", false);
    }
    return map;
  }

  @GetMapping("/list")
  public String rlist(Model model) {
    return "record/myRecord";
  }

  @RequestMapping("/pageList.json")
  @ResponseBody
  public Map<String, Object> listUser(String userAccount, Integer pageSize, Integer pageNumber) {
    Map<String, Object> resultMap = new HashMap();
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
    Page<RecordVo> pages = recordManager.pageFind(userAccount, pageable);

    resultMap.put("data", pages.getContent());
    resultMap.put("total", pages.getTotalElements());
    return resultMap;
  }

  @RequestMapping("/findById")
  @ResponseBody
  public Map<String, Object> findById(String id) {
    Map<String, Object> resultMap = new HashMap();

    resultMap.put("success", true);
    resultMap.put("data", recordManager.findById(id));
    return resultMap;
  }
}
