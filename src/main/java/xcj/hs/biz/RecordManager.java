package xcj.hs.biz;

import xcj.hs.vo.RecordVo;

public interface RecordManager {
  void save(RecordVo recordVo) throws Exception;

  RecordVo getOneUnreviewed(String userAccount);
}
