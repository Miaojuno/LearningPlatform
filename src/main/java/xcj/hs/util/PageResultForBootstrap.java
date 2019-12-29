package xcj.hs.util;

import xcj.hs.vo.UserVo;

import java.util.List;

public class PageResultForBootstrap {
    private int total;

    private List<UserVo> rows;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<UserVo> getRows() {
        return rows;
    }

    public void setRows(List<UserVo> rows) {
        this.rows = rows;
    }
}
