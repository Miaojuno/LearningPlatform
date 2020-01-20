package xcj.hs.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "MSG")
@Data
public class Msg {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Column(name = "MSG_ID")
  private String msgId;

  @Column(name = "MSG_DATE")
  private String msgDate;

  @Column(name = "MSG_TITLE")
  private String msgTitle;

  @Column(name = "MSG_DETAIL")
  private String msgDetail;

  @Column(name = "MSG_ROLE_ID")
  private String msgRoleId;

  @Column(name = "MSG_USER_ID")
  private String msgUserId;

  @Column(name = "MSG_CREATER")
  private String msgCreater;
}
