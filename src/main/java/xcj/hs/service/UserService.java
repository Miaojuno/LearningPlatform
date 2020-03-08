package xcj.hs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xcj.hs.entity.User;
import xcj.hs.vo.UserVo;

import java.util.List;

public interface UserService {
    /**
     * 登陆验证
     * @param account
     * @param pwd
     * @return
     */
    boolean loginCheck(String account,String pwd);

    /**
     * 新增用户（当前账户名存在时返回false）
     * @param newUser
     * @return
     */
    boolean create(User newUser);

    void modifyPic(String userAccount, byte[] pic);

    void modifyRole(String userAccount,String roleId);

    List<User> findAllActiveUser();

    Page<User> pageFind(UserVo userVo, Pageable pageable);

    boolean updateSuperior(String subordinateId,String superiorId);

    /**
     * 密码重置为“123456”
     * @param userId
     */
    void rePwd(String userId);


    /**
     * 删除用户（isActive置0）
     * @param userId
     */
    void deleteUser(String userId);

    int getActiveUserNumber();

    void update(User user);

    User findById(String userId);

    User findByUserAccount(String userAccount);
}
