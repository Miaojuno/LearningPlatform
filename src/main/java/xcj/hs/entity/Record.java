package xcj.hs.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "RECORD")
@Data
public class Record {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column( name = "REC_ID")
    private String recordId;

    @Column( name = "REC_USER_ID")
    private String userId;

    @Column( name = "REC_QUESTION_ID")
    private String questionId;

    @Column( name = "REC_USER_SOLUTION")
    private String userSolution;

    @Column( name = "REC_USER_PIC")
    private byte[] userPic;

    @Column( name = "REC_SCORE")
    private String score;

    @Column( name = "REC_DATE")
    private String date;
}
