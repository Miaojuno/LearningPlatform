package xcj.hs.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "NEW_MSG")
@Data
public class NewMsg {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Column(name = "NM_ID")
  private String nmId;

  @Column(name = "USER_ID")
  private String userId;

  @Column(name = "NM_NUMBER")
  private String nmNumber;
}
