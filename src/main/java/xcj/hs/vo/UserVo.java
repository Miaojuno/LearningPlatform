package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserVo implements Serializable {

    private String userId;

    private String userAccount;

    private String userName;

    private String userPwd;

    private String roleId;

    private String roleName;

    private String isActive;

    private String superiorId;

    private String superiorName;

    private String userDesc;
}

