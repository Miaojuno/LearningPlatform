package xcj.hs.biz.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import xcj.hs.biz.BaseManager;
import xcj.hs.biz.RoleManager;
import xcj.hs.biz.UserManager;
import xcj.hs.entity.Role;
import xcj.hs.entity.User;
import xcj.hs.service.RoleService;
import xcj.hs.service.UserService;
import xcj.hs.vo.RoleVo;
import xcj.hs.vo.UserVo;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleManagerImpl extends BaseManagerImpl<RoleVo,Role> implements RoleManager {
    @Autowired
    RoleService roleService;

    public List<RoleVo> getAllActiveRole(){
        return po2vo(roleService.getAllActiveRole());
    }


    public List<RoleVo> pageFind(RoleVo roleVo, Pageable pageable){
        return po2vo(roleService.pageFind(roleVo,pageable));
    }

    public int getRoleNumber(){
        return roleService.getRoleNumber();
    }

    public boolean roleAdd(RoleVo roleVo){
        return roleService.roleAdd(vo2po(roleVo));
    }
}
