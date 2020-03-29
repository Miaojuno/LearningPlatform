package xcj.hs.service;

import xcj.hs.entity.NewMsg;

public interface NewMsgService {
  void addNumber(String userId, int number);

  NewMsg findByUserId(String userId);
}
