package xcj.hs.biz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xcj.hs.entity.User;
import xcj.hs.vo.UserVo;

import java.util.List;

public interface UserManager {
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


    void modifyRole(String userAccount,String roleId);

    boolean updateSuperior(String subordinateId,String superiorId);

    void modify(UserVo userVo);

    List<UserVo> findAllActiveUser();

    Page<UserVo> pageFind(UserVo userVo, Pageable pageable);

    Page<UserVo> superiorPageFind(UserVo userVo,String subordinateId, Pageable pageable);

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

    UserVo findById(String userId);

    /**
     * 根据用户账号获得角色名
     * @param userAccount
     * @return
     */
    String getRoleName(String userAccount);
}
