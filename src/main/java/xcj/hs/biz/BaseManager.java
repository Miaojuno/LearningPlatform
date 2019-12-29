package xcj.hs.biz;


import java.util.List;

public interface BaseManager<VO,PO> {
    public PO vo2po(VO vo);

    public VO po2vo(PO po);

    public List<PO> vo2po(List<VO> vos);

    public List<VO> po2vo(List<PO> pos);
}
