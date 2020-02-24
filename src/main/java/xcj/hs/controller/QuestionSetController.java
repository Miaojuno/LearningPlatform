package xcj.hs.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import xcj.hs.biz.QuestionSetManager;
import xcj.hs.vo.QuestionSetVo;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/questionSet")
public class QuestionSetController {
  @Autowired QuestionSetManager questionSetManager;

  /**
   * 添加题目集页面
   *
   * @param model
   * @return
   */
  @GetMapping("/addQuestionSet")
  public String addQuestionSetPage(Model model) {
    return "questionSet/addQuestionSet";
  }

  /**
   * 添加题目集
   *
   * @param questionSetVo
   * @return
   */
  @PostMapping("/addQuestionSet")
  @ResponseBody
  public Map<String, Object> addQuestionSet(QuestionSetVo questionSetVo, MultipartFile file) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (file != null) {
        questionSetVo.setQsPic(file.getBytes());
      }
      String qsId = questionSetManager.add(questionSetVo);
      if (StringUtils.isNotBlank(qsId)) {
        map.put("success", true);
        map.put("qsId", qsId);
      } else {
        map.put("success", false);
        map.put("msg", "添加题集失败");
      }
    } catch (Exception e) {
      map.put("success", false);
      map.put("msg", e.getMessage());
      return map;
    }
    return map;
  }

  /**
   * findById
   *
   * @param
   * @return
   */
  @PostMapping("/findById")
  @ResponseBody
  public Map<String, Object> findById(String qsId) {
    Map<String, Object> resultMap = new HashMap();
    QuestionSetVo questionSetVo = questionSetManager.findById(qsId);
    if (questionSetVo != null) {
      resultMap.put("data", questionSetVo);
      resultMap.put("success", true);
    } else {
      resultMap.put("success", false);
    }

    return resultMap;
  }

  /**
   * 为题集添加题目
   *
   * @param qsId
   * @param questionId
   * @return
   */
  @PostMapping("/addQuestion")
  @ResponseBody
  public Map<String, Object> addQuestion(String qsId, String questionId) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (questionSetManager.addQuestion(qsId, questionId)) {
      map.put("success", true);
    } else {
      map.put("success", false);
      map.put("msg", "添加题集失败");
    }
    return map;
  }

  /**
   * 学生查看自己的题集
   *
   * @param model
   * @return
   */
  @GetMapping("/list")
  public String list(Model model) {
    return "questionSet/list";
  }

  /**
   * 通过账号获取指定用户的题集
   *
   * @param userAccount
   * @return
   */
  @PostMapping("/findByUserAccount")
  @ResponseBody
  public Map<String, Object> findByUserAccount(String userAccount) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("success", true);
    map.put("data", questionSetManager.findByUserAccount(userAccount));
    return map;
  }

  /**
   * findAll
   *
   * @return
   */
  @PostMapping("/findAll")
  @ResponseBody
  public Map<String, Object> findAll() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("success", true);
    map.put("data", questionSetManager.findAll());
    return map;
  }

  /**
   * 向题集中添加用户
   *
   * @param userAccount
   * @param qsId
   * @return
   */
  @PostMapping("/addUser")
  @ResponseBody
  public Map<String, Object> addUser(String userAccount, String qsId) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (questionSetManager.addUser(userAccount, qsId)) {
      map.put("success", true);
    } else {
      map.put("success", false);
    }
    return map;
  }

  /**
   * 按照题集做题
   *
   * @param model
   * @return
   */
  @GetMapping("/doQuestionByQs")
  public String doQuestionByQs(Model model) {
    return "questionSet/doQuestionByQs";
  }

  /**
   * 获取用户指定题集做题记录
   *
   * @param userAccount
   * @param qsId
   * @return
   */
  @PostMapping("/findQsRecordByUser")
  @ResponseBody
  public Map<String, Object> findQsRecordByUser(String userAccount, String qsId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("success", true);
    map.put("data", questionSetManager.findQsRecordByUser(userAccount, qsId));
    return map;
  }
}
