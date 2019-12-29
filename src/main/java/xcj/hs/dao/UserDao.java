package xcj.hs.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xcj.hs.entity.User;


import java.util.List;

public interface UserDao extends JpaRepository<User,String> {

    User findByUserAccount(String account);

    /**
     * 修改角色id
     * @param roleId
     * @param userId
     */
    @Modifying
    @Query(value = "update user set roleId = :roleId where userId = :userId",nativeQuery = true)
    void modifyRoleIdById(@Param("roleId") String roleId, @Param("userId") String userId);

    /**
     * 修改密码
     * @param userPwd
     * @param userId
     */
    @Modifying
    @Query(value = "update user set USER_PWD = :userPwd where USER_ID = :userId",nativeQuery = true)
    void modifyUserPwdById(@Param("userPwd") String userPwd, @Param("userId") String userId);


    @Modifying
    @Query(value = "update user set IS_ACTIVE = :isActive where USER_ID = :userId",nativeQuery = true)
    void modifyIsActiveById(@Param("isActive") String isActive, @Param("userId") String userId);

    List<User> findByIsActiveEquals(String isActive);

    int countByIsActiveEquals(String isActive);

    Page<User> findByIsActiveAndUserAccountContainingAndRoleIdContaining(String isActive, String userName, String roleId, Pageable pageable);

}
