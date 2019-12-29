package xcj.hs;


import org.springframework.stereotype.Component;
import xcj.hs.dao.NeoDaoImpl;

import javax.annotation.PostConstruct;

@Component
public class Neo4jTest {
    @PostConstruct
    public static void test(){
        NeoDaoImpl.test();
    }
}
