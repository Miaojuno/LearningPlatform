package xcj.hs.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import xcj.hs.biz.RoleManager;
import xcj.hs.entity.Role;
import xcj.hs.service.RoleService;
import xcj.hs.vo.RoleVo;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Slf4j
public class BaseManagerImpl<VO,PO>  {

    private Class<PO> poClass;

    private Class<VO> voClass;

    public PO vo2po(VO vo){
        poClass=(Class<PO>) ( (ParameterizedType) getClass().getGenericSuperclass() ).getActualTypeArguments()[1];
        try {
            PO po=poClass.newInstance();
            BeanUtils.copyProperties(vo,po);
            return po;
        } catch (Exception e) {
            log.error("vo2po转换出错");
        }
        return null;
    }

    public VO po2vo(PO po){
        voClass=(Class<VO>) ( (ParameterizedType) getClass().getGenericSuperclass() ).getActualTypeArguments()[0];
        try {
            VO vo=voClass.newInstance();
            BeanUtils.copyProperties(po,vo);
            return vo;
        } catch (Exception e) {
            log.error("po2vo转换出错");
        }
        return null;
    }

    public List<PO> vo2po(List<VO> vos){
        List<PO> pos=new ArrayList<>();
        for(VO vo:vos){
            pos.add(vo2po(vo));
        }
        return pos;
    }

    public List<VO> po2vo(List<PO> pos){
        List<VO> vos=new ArrayList<>();
        for(PO po:pos){
            vos.add(po2vo(po));
        }
        return vos;
    }

    public Page<VO> po2vo(Page<PO> pos){
        List<VO> voContents=new ArrayList<>();
        for(PO po:pos.getContent()){
            voContents.add(po2vo(po));
        }
        Page<VO> vos=new PageImpl<>(voContents,pos.getPageable(),pos.getTotalElements());
        return vos;
    }



}
