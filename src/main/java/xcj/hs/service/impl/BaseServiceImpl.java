package xcj.hs.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;


public class BaseServiceImpl<ENDITY> {

    /**
     * 获取完整的实体 用于更新
     * @param rowEndity 通过id查询的数据库内数据
     * @param inEndity 前端传入的数据
     * @return
     */
    public ENDITY getNewEntity(ENDITY rowEndity,ENDITY inEndity){
        BeanUtils.copyProperties(rowEndity, inEndity, getNoNullProperties(inEndity));
        return inEndity;
    }

    private static String[] getNoNullProperties(Object target) {
        BeanWrapper srcBean = new BeanWrapperImpl(target);
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        Set<String> noEmptyName = new HashSet<>();
        for (PropertyDescriptor p : pds) {
            Object value = srcBean.getPropertyValue(p.getName());
            if (value != null) noEmptyName.add(p.getName());
        }
        String[] result = new String[noEmptyName.size()];
        return noEmptyName.toArray(result);
    }


}
