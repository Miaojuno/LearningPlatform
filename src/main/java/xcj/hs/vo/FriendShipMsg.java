package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FriendShipMsg implements Serializable {

  private String msgUser;

  private String msgTime;

  //type为img时，存储id
  private String msgContent;

  //  type:text/img
  private String msgType;
}
