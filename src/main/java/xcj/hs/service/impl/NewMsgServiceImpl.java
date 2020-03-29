package xcj.hs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcj.hs.dao.NewMsgDao;
import xcj.hs.entity.NewMsg;
import xcj.hs.service.NewMsgService;

@Service
public class NewMsgServiceImpl implements NewMsgService {
  @Autowired NewMsgDao newMsgDao;

  public void addNumber(String userId, int number) {
    NewMsg newMsg = newMsgDao.findByUserId(userId);
    if (newMsg == null) {
      newMsg = new NewMsg();
      newMsg.setUserId(userId);
      newMsg.setUserId(String.valueOf(number));
    } else {
      newMsg.setNmNumber(String.valueOf(Integer.parseInt(newMsg.getNmNumber()) + number));
    }
    newMsgDao.save(newMsg);
  }

  public NewMsg findByUserId(String userId) {
    return newMsgDao.findByUserId(userId);
  }
}
