package xcj.hs.biz;

import org.springframework.data.domain.Pageable;
import xcj.hs.entity.Role;
import xcj.hs.vo.RoleVo;
import java.util.List;

public interface RoleManager extends BaseManager<RoleVo,Role>{

    /**
     * 获取所有可用的role
     * @return
     */
    public List<RoleVo> getAllActiveRole();


    List<RoleVo> pageFind(RoleVo userVo,Pageable pageable);

    int getRoleNumber();
}
