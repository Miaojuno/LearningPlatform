package xcj.hs.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;
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

  @Override
  public RecordVo po2vo(Record record){
    RecordVo recordVo=super.po2vo(record);
    BASE64Encoder encoder = new BASE64Encoder();
    if (record.getUserPic() != null) {
      recordVo.setUserPicStr(encoder.encode(record.getUserPic()));
    }
    return recordVo;
  }


  public void save(RecordVo recordVo){
    Record record=vo2po(recordVo);
    record.setUserId(userService.findByUserAccount(recordVo.getUserAccount()).getUserId());
    recordService.save(record);
  }
}
