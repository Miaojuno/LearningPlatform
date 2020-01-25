package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecordVo implements Serializable {

  private String recordId;

  private String userAccount;

  private String userId;

  private String questionId;

  private String questionDetail;

  private byte[] questionPic;

  private String solution;

  private byte[] SolutionPic;

  private String userSolution;

  private byte[] recordUserPic;

  private String score;

  private String date;

  private String reviewerId;
}
