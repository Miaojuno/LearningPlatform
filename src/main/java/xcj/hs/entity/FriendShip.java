package xcj.hs.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "FRIEND_SHIP")
@Data
public class FriendShip {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Column(name = "FS_ID")
  private String fsId;

  @Column(name = "FS_USER1")
  private String fsUser1;

  @Column(name = "FS_USER2")
  private String fsUser2;

  // active：1为激活 0为拉黑 null为未确认
  @Column(name = "FS_USER1_ACTIVE")
  private String fsUser1Active;

  @Column(name = "FS_USER2_ACTIVE")
  private String fsUser2Active;

  // 聊天记录
  @Column(name = "FS_MSG_RECORD")
  private String fsMsgRecord;

  @Column(name = "FS_UNREAD1")
  private String fsUnread1;

  @Column(name = "FS_UNREAD2")
  private String fsUnread2;

  @Column(name = "FS_LAST_TIME")
  private String fsLastTime;
}
