package xcj.hs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcj.hs.dao.ImgDao;
import xcj.hs.entity.Img;
import xcj.hs.service.ImgService;

@Service
public class ImgServiceImpl extends BaseServiceImpl<Img> implements ImgService {

  @Autowired ImgDao imgDao;

  public String save(byte[] imgContent) {
    Img img = new Img();
    img.setImgContent(imgContent);
    return imgDao.saveAndFlush(img).getImgId();
  }

  public Img findById(String id) {
    return imgDao.findById(id).get();
  }
}
