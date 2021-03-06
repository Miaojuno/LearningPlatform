package xcj.hs.entity;

import lombok.Data;

@Data
public class Question {

  private String questionId;
  private String questionDetail;
  private String score;
  private String solution;
  private String typeDistribution;
  private String difficultyDistribution;
  private String type;
  private byte[] pic;
  private byte[] solutionPic;

  public Question() {}

  public Question(
      String questionId,
      String questionDetail,
      String score,
      String solution,
      String typeDistribution,
      String difficultyDistribution,
      String type,
      byte[] pic,
      byte[] solutionPic) {
    this.questionId = questionId;
    this.questionDetail = questionDetail;
    this.score = score;
    this.solution = solution;
    this.typeDistribution = typeDistribution;
    this.difficultyDistribution = difficultyDistribution;
    this.type = type;
    this.pic = pic;
    this.solutionPic = solutionPic;
  }
}
