package xcj.hs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xcj.hs.entity.FriendShip;

import java.util.List;

public interface FriendShipDao extends JpaRepository<FriendShip, String> {

    FriendShip findByFsUser1AndFsUser2(String fsUser1,String fsUser2);

    List<FriendShip> findByFsUser1OrFsUser2(String fsUser1,String fsUser2);
}
