package xcj.hs.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xcj.hs.biz.RecordManager;
import xcj.hs.entity.Record;
import xcj.hs.service.RecordService;
import xcj.hs.service.UserService;
import xcj.hs.vo.RecordVo;

@Component
@Slf4j
public class RecordManagerImpl extends BaseManagerImpl<RecordVo, Record> implements RecordManager {
  @Autowired RecordService recordService;
  @Autowired
  UserService userService;

  public void save(RecordVo recordVo){
    Record record=vo2po(recordVo);
    record.setUserId(userService.findByUserAccount(recordVo.getUserAccount()).getUserId());
    recordService.save(record);
  }
}
