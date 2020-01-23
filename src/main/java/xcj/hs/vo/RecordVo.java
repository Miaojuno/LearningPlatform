package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecordVo implements Serializable {

  private String recordId;

  private String userAccount;

  private String userId;

  private String questionId;

  private String userSolution;

  private byte[] userPic;

  private String score;
}
