package xcj.hs.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "USER")
@Data
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column( name = "USER_ID")
    private String userId;

    @Column( name = "USER_ACCOUNT")
    private String userAccount;

    @Column( name = "USER_NAME")
    private String userName;

    @Column( name = "USER_PWD")
    private String userPwd;

    @Column( name = "ROLE_ID")
    private String roleId;

    @Column( name = "IS_ACTIVE")
    private String isActive;

    @Column( name = "SUPERIOR_ID")
    private String superiorId;

    @Column( name = "USER_DESC")
    private String userDesc;
}
