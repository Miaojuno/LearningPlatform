package xcj.hs.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xcj.hs.dao.UserDao;
import xcj.hs.entity.User;
import xcj.hs.service.UserService;
import xcj.hs.vo.UserVo;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
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

    public Page<User> pageFind(UserVo userVo, Pageable pageable){
        return userDao.findByIsActiveAndUserAccountContainingAndRoleIdContaining("1",userVo.getUserName(),userVo.getRoleId(),pageable);
    }

    public boolean updateSuperior(String subordinateId,String superiorId){
        User user=findById(subordinateId);
        if(user!=null){
            user.setSuperiorId(superiorId);
            userDao.save(user);
            return true;
        }
        else {
            return false;
        }
    }

    public void rePwd(String userId){
        userDao.modifyUserPwdById("123456",userId);
    }

    public void deleteUser(String userId){
        userDao.modifyIsActiveById("0",userId);
    }

    public void update(User user){
        userDao.save(getNewEntity(findById(user.getUserId()),user));
    }

    public User findById(String userId){
        if(userDao.findById(userId).isPresent()){
            return userDao.findById(userId).get();
        }
        return null;
    }

    public User findByUserAccount(String userAccount){
        return userDao.findByUserAccount(userAccount);
    }

}
