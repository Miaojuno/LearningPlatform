package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleVo implements Serializable {

    private String roleId;

    private String roleName;

    private String roleDesc;

    private String isActive;
}

