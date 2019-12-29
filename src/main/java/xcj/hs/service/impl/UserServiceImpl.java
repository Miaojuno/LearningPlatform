package xcj.hs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xcj.hs.dao.UserDao;
import xcj.hs.entity.User;
import xcj.hs.service.UserService;
import xcj.hs.vo.UserVo;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    public boolean loginCheck(String account,String pwd){
        User user=userDao.findByUserAccount(account);
        if(user!=null && "1".equals(user.getIsActive())){
            if(user.getUserPwd().equals(pwd)){
                return true;
            }
        }
        return false;
    }

    public boolean create(User newUser){
        User user=userDao.findByUserAccount(newUser.getUserAccount());
        if(user==null){
            userDao.save(newUser);
            return true;
        }
        return false;
    }

    public void modifyRole(String userId,String roleId){
        userDao.modifyRoleIdById(roleId,userId);
    }

    public List<User> findAllActiveUser(){
        return userDao.findByIsActiveEquals("1");
    }

    public int getActiveUserNumber(){
        return userDao.countByIsActiveEquals("1");
    }

    public List<User> pageFind(UserVo userVo,Pageable pageable){
        return userDao.findByIsActiveAndUserAccountContainingAndRoleIdContaining("1",userVo.getUserName(),userVo.getRoleId(),pageable).getContent();
    }

    public void rePwd(String userId){
        userDao.modifyUserPwdById("123456",userId);
    }

    public void deleteUser(String userId){
        userDao.modifyIsActiveById("0",userId);
    }
}
