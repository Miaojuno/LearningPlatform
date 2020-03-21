package xcj.hs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class NameValueVo implements Serializable {

  private String name;

  private String value;
}
