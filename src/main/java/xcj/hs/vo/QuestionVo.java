package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionVo implements Serializable {

  private String questionId;
  private String questionDetail;
  private String score;
  private String solution;
  private String typeDistribution;
  private String difficultyDistribution;
  private byte[] pic;
  private byte[] solutionPic;
}
