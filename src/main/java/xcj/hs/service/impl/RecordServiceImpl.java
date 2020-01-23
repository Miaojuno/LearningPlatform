package xcj.hs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xcj.hs.dao.RecordDao;
import xcj.hs.dao.UserDao;
import xcj.hs.entity.Record;
import xcj.hs.entity.User;
import xcj.hs.service.RecordService;
import xcj.hs.service.RoleService;
import xcj.hs.service.UserService;
import xcj.hs.vo.UserVo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RecordServiceImpl extends BaseServiceImpl<Record> implements RecordService {

  @Autowired
  RecordDao recordDao;

  public void save(Record record){
    recordDao.save(record);
  }


}
