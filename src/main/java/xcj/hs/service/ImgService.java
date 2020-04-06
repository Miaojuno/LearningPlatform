package xcj.hs.service;

import xcj.hs.entity.Img;

public interface ImgService {
  String save(byte[] imgContent);

  Img findById(String id);

  void deleteById(String id);
}
