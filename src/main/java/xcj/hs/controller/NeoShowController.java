package xcj.hs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xcj.hs.util.Neo4jUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("neo4jShow")
public class NeoShowController {

  @Autowired private Neo4jUtil neo4jUtil;

  @GetMapping("/index")
  public String index(Model model) {
    return "main/neo4j";
  }

  @GetMapping("getOnePoint")
  @ResponseBody
  public Map<String, Object> getOnePoint() {
    Map<String, Object> retMap = new HashMap<>();
    // cql语句
    String cql = "match (m:Point{pointId: \"100\"}) return m";
    Set<Map<String, Object>> nodeList = new HashSet<>();
    neo4jUtil.getList(cql, nodeList);
    retMap.put("nodeList", nodeList);
    return retMap;
  }

  @GetMapping("getPointPath")
  @ResponseBody
  public Map<String, Object> getPointPath(String id) {
    Map<String, Object> retMap = new HashMap<>();
    // cql语句  ID()可以获取节点自动生成的id
    String cql = "match l=(m)-[:子知识点]-(n) where ID(m)=" + id + " return l";
    // 待返回的值，与cql return后的值顺序对应
    Set<Map<String, Object>> nodeList = new HashSet<>();
    Set<Map<String, Object>> edgeList = new HashSet<>();
    neo4jUtil.getPathList(cql, nodeList, edgeList);
    retMap.put("nodeList", nodeList);
    retMap.put("edgeList", edgeList);
    return retMap;
  }

  @GetMapping("getShortPath")
  public Map<String, Object> getShortPath() {
    Map<String, Object> retMap = new HashMap<>();
    // cql语句
    String cql =
        "match l=shortestPath(({name:'Keanu Reeves'})-[*]-({title:\"Jerry Maguire\"})) return l";
    // 待返回的值，与cql return后的值顺序对应
    Set<Map<String, Object>> nodeList = new HashSet<>();
    Set<Map<String, Object>> edgeList = new HashSet<>();
    neo4jUtil.getPathList(cql, nodeList, edgeList);
    retMap.put("nodeList", nodeList);
    retMap.put("edgeList", edgeList);
    return retMap;
  }

  @GetMapping("getFields")
  public Map<String, Object> getFields() {
    Map<String, Object> retMap = new HashMap<>();
    // cql语句
    // String cql = "match (n:Person{name:\"Anthony Edwards\"}) return n.name as name,n.born as
    // born";
    String cql = "match (n:Person) return count(n) as cou";
    retMap.put("fieldList", neo4jUtil.getFields(cql));
    return retMap;
  }

  @GetMapping("add")
  public void add() {
    // 创建单个节点
    // String cql = "create (:Person{name:\"康康\"})";
    // 创建多个节点
    // String cql = "create (:Person{name:\"李雷\"}) create (:Person{name:\"小明\"})";
    // 根据已有节点创建关系
    // String cql = "match (n:Person{name:\"李雷\"}),(m:Person{name:\"小明\"}) create
    // (n)-[r:friendRelation]->(m)";
    // 同时创建节点和关系
    String cql = "create (:Person{name:\"张三\"})-[r:friendRelation]->(:Person{name:\"王五\"})";
    neo4jUtil.add(cql);
  }
}
