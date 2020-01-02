package xcj.hs.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xcj.hs.entity.Role;

import java.util.List;

public interface RoleDao extends JpaRepository<Role,String> {
    List<Role> findByIsActiveEquals(String isActive);

    Role findByRoleId(String roleId);

    List<Role> findByIsActive(String isActive);

    @Query(value = "select r from Role r ")
    Page<Role> pageFind( Pageable pageable);

    Role findByRoleName(String roleName);

    @Modifying
    @Query(value = "update role set ROLE_DESC = :roleDesc , IS_ACTIVE = :isActive where ROLE_ID = :roleId",nativeQuery = true)
    void modifyRoleDescAndIsActivebyId(@Param("roleDesc") String roleDesc, @Param("isActive") String isActive , @Param("roleId") String roleId);

}
