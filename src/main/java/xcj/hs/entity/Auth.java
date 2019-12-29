package xcj.hs.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "AUTH")
@Data
public class Auth {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column( name = "AUTH_ID")
    private String authId;

    @Column( name = "AUTH_NAME")
    private String authName;

    @Column( name = "AUTH_DESC")
    private String authDesc;


}
