package xcj.hs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xcj.hs.entity.Img;

public interface ImgDao extends JpaRepository<Img, String> {}
