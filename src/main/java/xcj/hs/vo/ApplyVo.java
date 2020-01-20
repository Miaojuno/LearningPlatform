package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class ApplyVo implements Serializable {

    private String applyId;

    // 角色改变 上级改变
    private String type;

    private String userId;

    private String userAccount;

    private String oldId;

    private String newId;

    //  申请中 通过 拒绝
    private String status;

    private String reason;
}

