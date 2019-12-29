package xcj.hs.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ROLE")
@Data
public class Role {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column( name = "ROLE_ID")
    private String roleId;

    @Column( name = "ROLE_NAME")
    private String roleName;

    @Column( name = "ROLE_DESC")
    private String roleDesc;

    @Column( name = "IS_ACTIVE")
    private String isActive;

}
