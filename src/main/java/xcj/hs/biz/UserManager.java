package xcj.hs.biz;

import org.springframework.data.domain.Pageable;
import xcj.hs.entity.User;
import xcj.hs.vo.UserVo;

import java.util.List;

public interface UserManager extends BaseManager<UserVo,User> {
    /**
     * 登陆验证
     * @param account
     * @param pwd
     * @return
     */
    boolean loginCheck(String account,String pwd);


    /**
     * 注册（当前账户名存在时返回false）
     * @param userVo
     * @return
     */
    boolean register(UserVo userVo);


    void modifyRole(String userId,String roleId);

    List<UserVo> findAllActiveUser();

    List<UserVo> pageFind(UserVo userVo,Pageable pageable);

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
}
