package xcj.hs.biz.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import xcj.hs.biz.UserManager;
import xcj.hs.entity.User;
import xcj.hs.service.RoleService;
import xcj.hs.service.UserService;
import xcj.hs.vo.UserVo;

import java.util.List;

@Component
public class UserManagerImpl extends BaseManagerImpl<UserVo,User> implements UserManager{
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    public boolean loginCheck(String account,String pwd){
        return userService.loginCheck(account,pwd);
    }

    public boolean register(UserVo userVo){
        User newUser=vo2po(userVo);
        newUser.setIsActive("1");
        if(StringUtils.isBlank(userVo.getRoleId())){
            newUser.setRoleId("8a8181816e209f02016e209f137f0000");//默认为"默认角色"
        }
        return userService.create(newUser);
    }

    public void modifyRole(String userId,String roleId){
        userService.modifyRole(userId,roleId);
    }

    public List<UserVo> findAllActiveUser(){
        return po2vo(userService.findAllActiveUser());
    }

    @Override
    public List<UserVo> po2vo(List<User> users){
        List<UserVo> resultList= super.po2vo(users);

        //添加roleName字段
        for(UserVo userVo:resultList){
            String roleName=roleService.findRoleByRoleId(userVo.getRoleId()).getRoleName();
            if(StringUtils.isNotBlank(roleName)){
                userVo.setRoleName(roleName);
            }
        }
        return resultList;
    }

    public Page<UserVo> pageFind(UserVo userVo, Pageable pageable){
        return po2vo(userService.pageFind(userVo, pageable));
    }

    public void rePwd(String userId){
        userService.rePwd(userId);
    }

    public void deleteUser(String userId){
        userService.deleteUser(userId);
    }

    public int getActiveUserNumber(){
        return userService.getActiveUserNumber();
    }

    public void modify(UserVo userVo){
        userService.modify(vo2po(userVo));
    }

    public UserVo findById(String userId){
        return po2vo(userService.findById(userId));
    }
}
