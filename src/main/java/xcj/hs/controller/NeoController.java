package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xcj.hs.dao.impl.NeoDaoImpl;
import xcj.hs.entity.Point;
import xcj.hs.entity.Question;
import xcj.hs.service.NeoService;
import xcj.hs.vo.QuestionVo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/neo")
public class NeoController {

  @Autowired NeoService neoService;

  /**
   * 导入（知识点 题目 关系）
   *
   * @param model
   * @return
   */
  @GetMapping("/excelupload")
  public String excelupload(Model model) {
    return "main/excelupload";
  }

  @PostMapping("/excelupload")
  @ResponseBody
  public Map<String, Object> excelupload(@RequestParam("file") MultipartFile file, Model model)
      throws Exception {
    Map<String, Object> resultMap = new HashMap();
    String msg = neoService.excelUpload(file);
    resultMap.put("success", true);
    resultMap.put("msg", msg);
    return resultMap;
  }

  /**
   * 导入题目
   *
   * @param model
   * @return
   */
  @GetMapping("/questionupload")
  public String questionupload(Model model) {
    return "main/questionupload";
  }

  /**
   * 添加题目页面
   *
   * @param model
   * @return
   */
  @GetMapping("/addQuestion")
  public String addQuestionPage(Model model) {
    return "question/addQuestion";
  }

  /**
   * 添加题目
   *
   * @param file1
   * @param file2
   * @param question
   * @return
   */
  @PostMapping("/addQuestion")
  @ResponseBody
  public Map<String, Object> addQuestion(
      MultipartFile file1, MultipartFile file2, Question question) {
    Map<String, Object> resultMap = new HashMap();
    try {
      if (file1 != null) {
        question.setPic(file1.getBytes());
      }
      if (file2 != null) {
        question.setSolutionPic(file2.getBytes());
      }
    } catch (IOException e) {
      resultMap.put("success", false);
      resultMap.put("msg", e.getMessage());
    }
    String questionId = neoService.addQuestion(question);
    resultMap.put("success", true);
    resultMap.put("questionId", questionId);
    return resultMap;
  }

  @PostMapping("/questionupload")
  @ResponseBody
  public Map<String, Object> questionupload(@RequestParam("file") MultipartFile file, Model model)
      throws Exception {
    Map<String, Object> resultMap = new HashMap();
    String msg;
    try {
      msg = NeoDaoImpl.questionUpload(file);
    } catch (Exception e) {
      resultMap.put("success", false);
      resultMap.put("msg", e.getMessage());
      return resultMap;
    }
    resultMap.put("success", true);
    resultMap.put("msg", msg);
    return resultMap;
  }

  /**
   * getRandomQuestion
   *
   * @param request
   * @return
   */
  @PostMapping("/getRandomQuestion")
  @ResponseBody
  public Map<String, Object> getRandomQuestion(HttpServletRequest request, String pointId) {
    Map<String, Object> resultMap = new HashMap();
    QuestionVo questionVo =
        neoService.getRandomQuestion(
            (String) request.getSession().getAttribute("loginUserAccount"), pointId);
    if (questionVo != null) {
      resultMap.put("data", questionVo);
      resultMap.put("success", true);
    } else {
      resultMap.put("success", false);
    }

    return resultMap;
  }

  /**
   * findQuestionById(通过noe4j节点id找question，非questionId)
   *
   * @param
   * @return
   */
  @PostMapping("/findQuestionById")
  @ResponseBody
  public Map<String, Object> findQuestionById(String id) {
    Map<String, Object> resultMap = new HashMap();
    QuestionVo questionVo = neoService.findQuestionById(id);
    if (questionVo != null) {
      resultMap.put("data", questionVo);
      resultMap.put("success", true);
    } else {
      resultMap.put("success", false);
    }

    return resultMap;
  }

  /**
   * findByQuestionId
   *
   * @param
   * @return
   */
  @PostMapping("/findByQuestionId")
  @ResponseBody
  public Map<String, Object> findByQuestionId(String questionId) {
    Map<String, Object> resultMap = new HashMap();
    QuestionVo questionVo = neoService.findByQuestionId(questionId);
    if (questionVo != null) {
      resultMap.put("data", questionVo);
      resultMap.put("success", true);
    } else {
      resultMap.put("success", false);
    }

    return resultMap;
  }

  /**
   * findPointById(通过noe4j节点id找Point，非PointId)
   *
   * @param
   * @return
   */
  @PostMapping("/findPointById")
  @ResponseBody
  public Map<String, Object> findPointById(String id) {
    Map<String, Object> resultMap = new HashMap();
    Point point = neoService.findPointById(id);
    if (point != null) {
      resultMap.put("data", point);
      resultMap.put("success", true);
    } else {
      resultMap.put("success", false);
    }

    return resultMap;
  }

  /**
   * 根据知识点内容查找知识点list
   *
   * @param pointDetail
   * @return
   */
  @Cacheable(value = "findPointByDetailCache")
  @RequestMapping("/findPointByDetail.json")
  @ResponseBody
  public Map<String, Object> findPointByDetail(String pointDetail) {
    Map<String, Object> resultMap = new HashMap();
    List<Point> points = neoService.findPointByDetail(pointDetail);
    if (points != null && points.size() != 0) {
      resultMap.put("data", points);
      resultMap.put("success", true);
    } else {
      resultMap.put("success", false);
    }

    return resultMap;
  }
}
