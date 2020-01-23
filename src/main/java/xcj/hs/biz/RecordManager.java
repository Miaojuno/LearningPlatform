package xcj.hs.biz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xcj.hs.vo.RecordVo;
import xcj.hs.vo.UserVo;

import java.util.List;

public interface RecordManager {
  void save(RecordVo recordVo);
}
