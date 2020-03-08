package xcj.hs.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xcj.hs.entity.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, String> {

  User findByUserAccount(String account);

  /**
   * 修改角色id
   *
   * @param roleId
   * @param userAccount
   */
  @Modifying
  @Query(
      value = "update user set ROLE_ID = :roleId where USER_ACCOUNT = :userAccount",
      nativeQuery = true)
  void modifyRoleIdByUserAccount(
      @Param("roleId") String roleId, @Param("userAccount") String userAccount);

  @Modifying
  @Query(value = "update user set PIC = :pic where USER_ACCOUNT = :userAccount", nativeQuery = true)
  void modifyPicByUserAccount(@Param("userAccount") String userAccount, @Param("pic") byte[] pic);

  /**
   * 修改密码
   *
   * @param userPwd
   * @param userId
   */
  @Modifying
  @Query(value = "update user set USER_PWD = :userPwd where USER_ID = :userId", nativeQuery = true)
  void modifyUserPwdById(@Param("userPwd") String userPwd, @Param("userId") String userId);

  @Modifying
  @Query(
      value = "update user set IS_ACTIVE = :isActive where USER_ID = :userId",
      nativeQuery = true)
  void modifyIsActiveById(@Param("isActive") String isActive, @Param("userId") String userId);

  //    @Modifying
  //    @Query(value = "update user set USER_NAME = :userName , ROLE_ID = :roleId , USER_DESC = :
  // userDesc where USER_ID = :userId",nativeQuery = true)
  //    void modifyById(@Param("userName") String userName, @Param("roleId") String roleId ,
  // @Param("userDesc") String userDesc , @Param("userId") String userId);

  List<User> findByIsActiveEquals(String isActive);

  int countByIsActiveEquals(String isActive);

  Page<User> findByIsActiveAndUserNameContainingAndRoleIdContaining(
      String isActive, String userName, String roleId, Pageable pageable);

  Page<User> findByIsActiveAndUserNameContainingAndSuperiorIdInAndRoleIdContaining(
      String isActive, String userName, List<Object> superiorIds, String roleId, Pageable pageable);

  int countByIsActiveAndUserAccountContainingAndRoleIdContaining(
      String isActive, String userName, String roleId);
}
