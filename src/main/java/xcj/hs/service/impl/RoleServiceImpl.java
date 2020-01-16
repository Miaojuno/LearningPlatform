package xcj.hs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xcj.hs.dao.RoleDao;
import xcj.hs.dao.UserDao;
import xcj.hs.entity.Role;
import xcj.hs.entity.User;
import xcj.hs.service.RoleService;
import xcj.hs.service.UserService;
import xcj.hs.vo.RoleVo;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;

    public List<Role> getAllActiveRole(){
        return roleDao.findByIsActiveEquals("1");
    }


    public Role findRoleByRoleId(String roleId){
        return roleDao.findByRoleId(roleId);
    }

    public Role findRoleByRoleName(String roleName){
        return roleDao.findByRoleName(roleName);
    }

    public Page<Role> pageFind(RoleVo roleVo, Pageable pageable){
        return roleDao.pageFind(pageable);
    }

    public int getRoleNumber(){
        return (int) roleDao.count();
    }

    public boolean roleAdd(Role role){
        if(roleDao.findByRoleName(role.getRoleName())==null){
            role.setIsActive("1");
            roleDao.save(role);
            return true;
        }
        else return false;
    }

    public void roleModify(Role role){
        roleDao.modifyRoleDescAndIsActivebyId(role.getRoleDesc(),role.getIsActive(),role.getRoleId());
    }


}
