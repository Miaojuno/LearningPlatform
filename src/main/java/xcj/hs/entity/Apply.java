package xcj.hs.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "APPLY")
@Data
public class Apply {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Column(name = "APPLY_ID")
  private String applyId;

  @Column(name = "USER_ID")
  private String userId;

  // 0:角色变更申请 1:上级变更申请
  @Column(name = "TYPE")
  private String type;

  @Column(name = "OLD_ID")
  private String oldId;

  @Column(name = "NEW_ID")
  private String newId;

  //  0:申请中 1:通过 2:拒绝
  @Column(name = "STATUS")
  private String status;

  @Column(name = "REASON")
  private String reason;
}
