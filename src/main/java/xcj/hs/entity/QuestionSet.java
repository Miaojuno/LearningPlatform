package xcj.hs.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "QUESTION_SET")
@Data
public class QuestionSet {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Column(name = "QS_ID")
  private String qsId;

  @Column(name = "QS_PIC")
  private byte[] qsPic;

  @Column(name = "QS_NAME")
  private String qsName;

  @Column(name = "QS_OWNER")
  private String qsOwner;

  @Column(name = "QUESTION_IDS")
  private String questionIds;

  @Column(name = "USER_IDS")
  private String userIds;
}
