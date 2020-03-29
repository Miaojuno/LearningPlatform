package xcj.hs.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "IMG")
@Data
public class Img {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column( name = "IMG_ID")
    private String imgId;

    @Column( name = "IMG_CONTENT")
    private byte[] imgContent;

}
