package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FriendShipMsg implements Serializable {

  private String msgUser;

  private String msgTime;

  private String msgContent;
}
