package xcj.hs.service;

import org.springframework.data.domain.Pageable;
import xcj.hs.entity.Role;
import xcj.hs.vo.RoleVo;

import java.util.List;

public interface RoleService {
    /**
     * 获取所有可用的role
     * @return
     */
    public List<Role> getAllActiveRole();

    /**
     *通过roleid获取role
     * @param roleId
     * @return
     */
    public Role findRoleByRoleId(String roleId);


    List<Role> pageFind(RoleVo userVo,Pageable pageable);

    int getRoleNumber();
}
