package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionSetVo implements Serializable {

  private String qsId;

  private String qsName;

  private byte[] qsPic;

  private String qsOwner;

  //  姓名
  private String qsOwnerName;

  private String questionIds;

  // 题目数
  private String questionNumber;

  private String userIds;

  // 当前用户完成题目数（用于用户查询时）
  private String finishedNumber;

  // 当前用户未完成题目ids（用于用户查询时）
  private String unfinishedQuestionIds;
}
