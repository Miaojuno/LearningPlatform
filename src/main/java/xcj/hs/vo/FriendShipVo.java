package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FriendShipVo implements Serializable {

  private String fsId;

  private String fsUser1;

  private String fsUser2;

  private String fsUser1Active;

  private String fsUser2Active;

  private String fsMsgRecord;

  private String fsUnread1;

  private String fsUnread2;

  // 被查询方
  private UserVo userVo;

  private String fsLastTime;
}
