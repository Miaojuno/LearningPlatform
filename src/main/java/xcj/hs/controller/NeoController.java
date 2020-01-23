package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xcj.hs.dao.impl.NeoDaoImpl;
import xcj.hs.service.NeoService;

import java.util.HashMap;
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
   * getRandomQuestion
   *
   * @param model
   * @return
   */
  @PostMapping("/getRandomQuestion")
  @ResponseBody
  public Map<String, Object> getRandomQuestion(Model model) {
    Map<String, Object> resultMap = new HashMap();
    resultMap.put("data", neoService.getRandomQuestion());
    resultMap.put("success", true);
    return resultMap;
  }
}
