package xcj.hs.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ROLE_AUTH")
@Data
public class RoleAuth {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column( name = "ROLE_AUTH_ID")
    private String roleAuthId;

    @Column( name = "ROLE_ID")
    private String roleId;

    @Column( name = "AUTH_ID")
    private String authId;


}
