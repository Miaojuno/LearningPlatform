package xcj.hs.util;

import java.util.ArrayList;
import java.util.List;


public class PageList<E> {
    /**
     * 按照页数和每页显示数返回list
     * @param list
     * @param pageSize
     * @param pageNumber
     * @return
     */
    public ArrayList<E> getPageList(List<E> list, Integer pageSize, Integer pageNumber ){
        ArrayList<E> resultList=new ArrayList<>();
        int size=pageNumber*pageSize > list.size() ? list.size() : pageNumber*pageSize;
        for(int i=(pageNumber-1)*pageSize;i<size;i++){
            resultList.add(list.get(i));
        }
        return resultList;
    }
}
