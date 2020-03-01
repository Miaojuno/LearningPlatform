package xcj.hs.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xcj.hs.biz.RecordManager;
import xcj.hs.entity.Record;
import xcj.hs.entity.User;
import xcj.hs.service.NeoService;
import xcj.hs.service.RecordService;
import xcj.hs.service.UserService;
import xcj.hs.util.TimeUtil;
import xcj.hs.vo.QuestionVo;
import xcj.hs.vo.RecordVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RecordManagerImpl extends BaseManagerImpl<RecordVo, Record> implements RecordManager {
  @Autowired RecordService recordService;
  @Autowired UserService userService;
  @Autowired NeoService neoService;

  @Override
  public RecordVo po2vo(Record record) {
    if(record==null) return null;
    RecordVo recordVo = super.po2vo(record);
    QuestionVo questionVo = neoService.findByQuestionId(recordVo.getQuestionId());
    recordVo.setQuestionDetail(questionVo.getQuestionDetail());
    recordVo.setQuestionPic(questionVo.getPic());
    recordVo.setSolution(questionVo.getSolution());
    recordVo.setSolutionPic(questionVo.getSolutionPic());
    return recordVo;
  }

  public void save(RecordVo recordVo) throws Exception {
    Record record = vo2po(recordVo);
    // 当前做题用户
    User user = userService.findByUserAccount(recordVo.getUserAccount());
    record.setUserId(user.getUserId());
    // 设置审阅人为当前学生的上级老师
    if (StringUtils.isBlank(user.getSuperiorId())) {
      throw new Exception("请先选择上级教师再做题");
    }
    record.setReviewerId(user.getSuperiorId());
    // 选择题和客观题自动判题(忽略大小写)
    QuestionVo questionVo = neoService.findByQuestionId(recordVo.getQuestionId());
    if ("客观题".equals(questionVo.getType())) {
      if (recordVo.getUserSolution().equalsIgnoreCase(questionVo.getSolution())) {
        record.setScore(questionVo.getScore());
      } else record.setScore("0");
    }
    if ("选择题".equals(questionVo.getType())) {
      if (recordVo.getUserSolution().equalsIgnoreCase(questionVo.getSolution().split("/")[0])) {
        record.setScore(questionVo.getScore());
      } else record.setScore("0");
    }
    recordService.save(record);
  }

  public RecordVo getOneUnreviewed(String userAccount) {
    Record record =
        recordService.getOneUnreviewed(userService.findByUserAccount(userAccount).getUserId());
    if (record != null) {
      return po2vo(record);
    }
    return null;
  }

  public Map<String, Object> get15daysRecordData(String userAccount) {
    Map<String, Object> map = new HashMap<>();
    List<RecordVo> recordVos =
        po2vo(
            recordService.get15daysRecords(userService.findByUserAccount(userAccount).getUserId()));
    List<String> dates = new ArrayList<>();
    List<Integer> nums = new ArrayList<>();
    List<Double> rates = new ArrayList<>();

    Map<String, List<RecordVo>> recordVoGroupMap =
        recordVos.stream().collect(Collectors.groupingBy(o -> o.getDate().substring(0, 8)));
    for (int i = -14; i <= 0; i++) {
      String timeStr = TimeUtil.getSpecifiedTimeStr(TimeUtil.DATESTR, i);
      dates.add(timeStr);
      if (recordVoGroupMap.containsKey(timeStr)) {
        List<RecordVo> dayRecords = recordVoGroupMap.get(timeStr);
        nums.add(dayRecords.size());
        rates.add(
            (double)
                (dayRecords.stream().filter(o -> "0".equals(o.getScore())).count()
                    / dayRecords.size()));
      } else {
        nums.add(0);
        rates.add(0.0);
      }
    }
    map.put(
        "dates",
        dates.stream()
            .map(o -> o.substring(4, 6) + "/" + o.substring(6, 8))
            .collect(Collectors.toList()));
    map.put("nums", nums);
    map.put("rates", rates);
    return map;
  }

  public RecordVo findByUserAccountAndQuestionId(String userAccount, String questionId) {
    return po2vo(
        recordService.findByUserIdAndQuestionId(
            userService.findByUserAccount(userAccount).getUserId(), questionId));
  }
}
