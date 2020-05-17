package xcj.hs.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xcj.hs.biz.RecordManager;
import xcj.hs.biz.UserManager;
import xcj.hs.entity.Point;
import xcj.hs.entity.Record;
import xcj.hs.entity.User;
import xcj.hs.service.NeoService;
import xcj.hs.service.RecordService;
import xcj.hs.service.UserService;
import xcj.hs.util.TimeUtil;
import xcj.hs.vo.NameValueVo;
import xcj.hs.vo.QuestionVo;
import xcj.hs.vo.RecordVo;
import xcj.hs.vo.UserVo;

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
  @Autowired UserManager userManager;

  @Override
  public RecordVo po2vo(Record record) {
    if (record == null) return null;
    RecordVo recordVo = super.po2vo(record);
    QuestionVo questionVo = neoService.findByQuestionId(recordVo.getQuestionId());
    recordVo.setQuestionDetail(questionVo.getQuestionDetail());
    recordVo.setQuestionPic(questionVo.getPic());
    recordVo.setSolution(questionVo.getSolution());
    recordVo.setSolutionPic(questionVo.getSolutionPic());
    recordVo.setQuestionScore(questionVo.getScore());
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

  @Cacheable(value = "get15daysRecordData")
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
                (dayRecords.stream().filter(o -> !"0".equals(o.getScore())).count()
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

  @Cacheable(value = "getErrorCountGroupByKind")
  public List<NameValueVo> getErrorCountGroupByKind(String userAccount) {
    List<NameValueVo> result = new ArrayList<>();
    List<RecordVo> recordVos =
        po2vo(recordService.findByUserId(userService.findByUserAccount(userAccount).getUserId()));

    Map<String, List<RecordVo>> recordVoGroupMap =
        recordVos.stream()
            .map(
                recordVo -> {
                  QuestionVo questionVo = neoService.findByQuestionId(recordVo.getQuestionId());
                  recordVo.setQuestionType(questionVo.getType());
                  recordVo.setQuestionScore(questionVo.getScore());
                  return recordVo;
                })
            .collect(Collectors.groupingBy(recordVo -> recordVo.getQuestionType()));

    for (Map.Entry<String, List<RecordVo>> entry : recordVoGroupMap.entrySet()) {
      NameValueVo nameValueVo = new NameValueVo();
      nameValueVo.setName(entry.getKey());
      nameValueVo.setValue(
          String.valueOf(
              entry.getValue().stream()
                  .mapToDouble(
                      recordVo -> {
                        if (StringUtils.isBlank(recordVo.getScore())
                            || recordVo.getQuestionScore().equals(recordVo.getScore())) return 0;
                        else if (recordVo.getScore().equals("0")) return 1;
                        else return 0.5;
                      })
                  .sum()));
      result.add(nameValueVo);
    }

    return result;
  }

  @Cacheable(value = "getSubordinateSituation")
  public List<Object> getSubordinateSituation(String userAccount) {
    List<Object> result = new ArrayList<>();
    for (UserVo user : userManager.findAllSubordinate(userAccount)) {
      Map<String, Object> smap = new HashMap<>();
      smap.put("user", user);
      smap.put(
          "count",
          String.valueOf(
              recordService.findByUserId(user.getUserId()).stream()
                  .filter(
                      r ->
                          TimeUtil.timeCompare(
                                  r.getDate(), TimeUtil.getSpecifiedTimeStr(TimeUtil.TIMESTR, -7))
                              .equals("2"))
                  .count()));
      result.add(smap);
    }
    return result;
  }

  public void updateScore(String recordId, String score) {
    recordService.updateScore(recordId, score);
  }

  public Map<String, Object> getRecordByDiff(String userAccount) {
    Map<String, Object> map = new HashMap<>();
    List<RecordVo> recordVos =
        po2vo(recordService.findByUserId(userService.findByUserAccount(userAccount).getUserId()));
    Map<String, List<RecordVo>> recordVoGroupMap =
        recordVos.stream()
            .map(
                recordVo -> {
                  QuestionVo questionVo = neoService.findByQuestionId(recordVo.getQuestionId());
                  recordVo.setQuestionDiff(diffConversion(questionVo.getDifficultyDistribution()));
                  recordVo.setQuestionScore(questionVo.getScore());
                  return recordVo;
                })
            .collect(Collectors.groupingBy(recordVo -> recordVo.getQuestionDiff()));
    Double allNumber = Double.valueOf(recordVos.stream().count());
    for (Map.Entry<String, List<RecordVo>> entry : recordVoGroupMap.entrySet()) {
      Map<String, Double> aMap = new HashMap<>();
      aMap.put(
          "correctRate",
          entry.getValue().stream()
              .mapToDouble(
                  recordVo -> {
                    if (StringUtils.isBlank(recordVo.getScore()) || recordVo.getScore().equals("0"))
                      return 0;
                    else if (recordVo.getQuestionScore().equals(recordVo.getScore())) return 1;
                    else return 0.5;
                  })
              .sum());
      aMap.put("numberRate", entry.getValue().stream().count() / allNumber);
      map.put(entry.getKey(), aMap);
    }
    return map;
  }

  @Cacheable(value = "getErrorPorint")
  public List<Map<String, Object>> getErrorPorint(String userAccount) {
    List<Map<String, Object>> listMap = new ArrayList<>();
    List<Record> records =
        recordService.get15daysRecords(userService.findByUserAccount(userAccount).getUserId());
    Map<Point, List<Record>> pointmap =
        records.stream().filter(o -> neoService.findPointByQuestionId(o.getQuestionId()).size()>0)
            .collect(
                Collectors.groupingBy(
                    o -> neoService.findPointByQuestionId(o.getQuestionId()).get(0)));
    for (Map.Entry<Point, List<Record>> entry : pointmap.entrySet()) {
      Map<String, Object> mapItem = new HashMap<>();
      mapItem.put(
          "count",
          String.valueOf(entry.getValue().stream().filter(o -> "0".equals(o.getScore())).count()));
      mapItem.put("point", entry.getKey());
      listMap.add(mapItem);
    }

    Map<String, Object> mapItem = new HashMap<>();
    mapItem.put("count", "0");
    mapItem.put("point", neoService.findPointByPointId("1"));
    listMap.add(mapItem);
    Map<String, Object> mapItem2 = new HashMap<>();
    mapItem2.put("count", "0");
    mapItem2.put("point", neoService.findPointByPointId("2"));
    listMap.add(mapItem2);
    Map<String, Object> mapItem3 = new HashMap<>();
    mapItem3.put("count", "0");
    mapItem3.put("point", neoService.findPointByPointId("3"));
    listMap.add(mapItem3);

    return listMap;
  }

  //  难度名转换
  private String diffConversion(String diff) {
    if (StringUtils.isBlank(diff)) return "diff1";
    else return "diff" + diff;
  }

  public RecordVo findByUserAccountAndQuestionId(String userAccount, String questionId) {
    return po2vo(
        recordService.findByUserIdAndQuestionId(
            userService.findByUserAccount(userAccount).getUserId(), questionId));
  }

  public Page<RecordVo> pageFind(String userAccount, Pageable pageable) {
    return po2vo(
        recordService.pageFind(userService.findByUserAccount(userAccount).getUserId(), pageable));
  }

  public RecordVo findById(String id) {
    return po2vo(recordService.findById(id));
  }

  //缓存清理
  @Scheduled(cron = "0 0 3 * * ?")
  @CacheEvict(
      value = {"get15daysRecordData", "getErrorCountGroupByKind","getSubordinateSituation","getErrorPorint"},
      allEntries = true)
  public void clearCache() {}
}
