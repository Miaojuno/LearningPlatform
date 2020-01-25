package xcj.hs.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.neo4j.driver.v1.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import xcj.hs.dao.NeoDao;
import xcj.hs.entity.Question;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

@Slf4j
@Repository
public class NeoDaoImpl implements NeoDao {

  private static final Session session =
      GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456")).session();
  private int questionNumber;

  @PostConstruct
  public void test() {
    log.info("Neo4j数据库连接");
    StatementResult result = session.run("MATCH (a:Question) RETURN count(a) AS count");
    while (result.hasNext()) {
      Record record = result.next();
      questionNumber = Integer.valueOf(record.get("count").toString());
      log.info("数据库中题目数:" + record.get("count").toString());
    }
  }

  public Question findQuestionById(String id) {
    StatementResult result =
        session.run("MATCH (a:Question) WHERE a.questionId={id} return a", parameters("id", id));
    Record record = result.next();
    return new Question(
        record.get("a").get("questionId").asString(),
        record.get("a").get("questionDetail").asString(),
        record.get("a").get("score").asString(),
        record.get("a").get("solution").asString(),
        record.get("a").get("typeDistribution").asString(),
        record.get("a").get("difficultyDistribution").asString(),
        "\"\"".equals(record.get("a").get("pic").toString())
            ? null
            : record.get("a").get("pic").asByteArray(),
        "\"\"".equals(record.get("a").get("solutionPic").toString())
            ? null
            : record.get("a").get("solutionPic").asByteArray());
  }

  public Question getRandomQuestion() {
    String randomId = String.valueOf((int) (1 + Math.random() * (questionNumber)));
    return findQuestionById(randomId);
  }

  // 源数据导入
  public String excelUpload(MultipartFile file) {

    Workbook workbook = null;
    try {
      workbook = new XSSFWorkbook(file.getInputStream());
    } catch (IOException e) {
      return e.getMessage();
    }

    // 获取第一张表(知识点表)
    Sheet sheet = workbook.getSheetAt(0);
    String resultMsg = "";

    log.info("开始导入知识点");
    boolean flag1 = true;
    try {
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        for (Cell cell : row) {
          // 读取数据前设置单元格类型
          cell.setCellType(CellType.STRING);
        }

        session.run(
            "CREATE (a:Point {"
                + "pointId: {pointId}, "
                + "pointDetail: {pointDetail}, "
                + "chapter: {chapter}, "
                + "isbn: {isbn}, "
                + "grade: {grade},"
                + "distribution: {distribution}, "
                + "frequency: {frequency}})",
            parameters(
                "pointId",
                getString(row.getCell(1)),
                "pointDetail",
                getString(row.getCell(0)),
                "chapter",
                getString(row.getCell(3)),
                "isbn",
                getString(row.getCell(4)),
                "grade",
                getString(row.getCell(5)),
                "distribution",
                getString(row.getCell(6)),
                "frequency",
                getString(row.getCell(7))));
      }
    } catch (Exception e) {
      resultMsg += "知识点导入失败：知识点id重复---------\n";
      flag1 = false;
    } finally {
      if (flag1) {
        resultMsg += "知识点导入成功---------\n";
      }
    }

    log.info("开始导入知识点之间的关系");
    boolean flag2 = true;
    try {
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        for (Cell cell : row) {
          // 读取数据前设置单元格类型
          cell.setCellType(CellType.STRING);
        }

        if (row.getCell(2) == null
            || row.getCell(1) == null
            || StringUtils.isBlank(row.getCell(2).getStringCellValue())
            || StringUtils.isBlank(row.getCell(1).getStringCellValue())
            || "null".equals(row.getCell(2).getStringCellValue())
            || "null".equals(row.getCell(1).getStringCellValue())) {
          continue;
        }

        StatementResult result =
            session.run(
                "MATCH (a:Point{pointId:{pointId1}}),(b:Point{pointId:{pointId2}}),r=(a)-[]-(b)"
                    + "return count(r)",
                parameters(
                    "pointId1", getString(row.getCell(2)), "pointId2", getString(row.getCell(1))));
        Record record = result.next();

        // 两个知识点之间不存在关系
        if ("0".equals(record.get("count(r)").toString())) {
          session.run(
              "MATCH (a:Point),(b:Point)"
                  + "WHERE a.pointId = {pointId1} AND b.pointId = {pointId2}"
                  + "CREATE (a)-[r:子知识点] -> (b)",
              parameters(
                  "pointId1", getString(row.getCell(2)), "pointId2", getString(row.getCell(1))));
        } else {
          flag2 = false;
        }
      }
    } catch (Exception e) {
      resultMsg += "知识点之间的关系导入失败：" + e.getMessage() + "---------";
      flag2 = false;
    } finally {
      if (flag2) {
        resultMsg += "知识点之间的关系导入成功---------";
      }
    }

    // 获取第二张表(题目表)
    sheet = workbook.getSheetAt(1);

    // 获取图片存入map
    XSSFSheet sheet2 = (XSSFSheet) sheet;
    Map<Integer, byte[]> sheetIndexPicMap = new HashMap<Integer, byte[]>();
    Map<Integer, byte[]> sheetIndexSolutionPicMap = new HashMap<Integer, byte[]>();

    for (POIXMLDocumentPart dr : sheet2.getRelations()) {
      if (dr instanceof XSSFDrawing) {
        XSSFDrawing drawing = (XSSFDrawing) dr;
        List<XSSFShape> shapes = drawing.getShapes();
        for (XSSFShape shape : shapes) {
          XSSFPicture pic = (XSSFPicture) shape;
          XSSFClientAnchor anchor = pic.getPreferredSize();
          CTMarker ctMarker = anchor.getFrom();
          if (ctMarker.getCol() == 7) {
            sheetIndexPicMap.put(ctMarker.getRow(), pic.getPictureData().getData());
          } else if (ctMarker.getCol() == 8) {
            sheetIndexSolutionPicMap.put(ctMarker.getRow(), pic.getPictureData().getData());
          }
        }
      }
    }

    log.info("开始导入题目");
    boolean flag3 = true;
    try {
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        for (Cell cell : row) {
          // 读取数据前设置单元格类型
          cell.setCellType(CellType.STRING);
        }

        session.run(
            "CREATE (a:Question {"
                + "questionId: {questionId}, "
                + "questionDetail: {questionDetail}, "
                + "pic: {pic}, "
                + "solutionPic: {solutionPic}, "
                + "solution: {solution}, "
                + "score: {score}, "
                + "typeDistribution: {typeDistribution}, "
                + "difficultyDistribution: {difficultyDistribution}, "
                + "type: {type}})",
            parameters(
                "questionId",
                getString(row.getCell(0)),
                "questionDetail",
                getString(row.getCell(1)),
                "pic",
                sheetIndexPicMap.get(i) == null ? "" : sheetIndexPicMap.get(i),
                "solutionPic",
                sheetIndexSolutionPicMap.get(i) == null ? "" : sheetIndexSolutionPicMap.get(i),
                "solution",
                getString(row.getCell(2)),
                "score",
                getString(row.getCell(3)),
                "typeDistribution",
                getString(row.getCell(4)),
                "difficultyDistribution",
                getString(row.getCell(5)),
                "type",
                getString(row.getCell(6))));
      }
    } catch (Exception e) {
      resultMsg += "题目导入失败：题目id重复---------\n";
      flag3 = false;
    } finally {
      if (flag3) {
        resultMsg += "题目导入成功---------\n";
      }
    }

    // 获取第三张表(题目知识点对应表)
    sheet = workbook.getSheetAt(2);

    log.info("开始导入题目和知识点的关系");
    boolean flag4 = true;
    try {
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        for (Cell cell : row) {
          // 读取数据前设置单元格类型
          cell.setCellType(CellType.STRING);
        }

        StatementResult result =
            session.run(
                "MATCH (a:Question{questionId:{questionId}}),(b:Point{pointId:{pointId}}),r=(a)-[]-(b)"
                    + "return count(r)",
                parameters(
                    "questionId", getString(row.getCell(0)), "pointId", getString(row.getCell(1))));
        Record record = result.next();

        // 知识点和题目之间不存在关系
        if ("0".equals(record.get("count(r)").toString())) {
          session.run(
              "MATCH (a:Question),(b:Point)"
                  + "WHERE a.questionId = {questionId} AND b.pointId = {pointId}"
                  + "CREATE (a)-[r:属于] -> (b)",
              parameters(
                  "questionId", getString(row.getCell(0)), "pointId", getString(row.getCell(1))));
        } else {
          flag4 = false;
        }
      }
    } catch (Exception e) {
      resultMsg += "题目和知识点的关系导入失败：题目id重复---------\n";
      flag4 = false;
    } finally {
      if (flag4) {
        resultMsg += "题目和知识点的关系导入成功---------\n";
      }
    }
    return resultMsg;
  }

  private static String getString(Cell cell) {
    return cell == null ? "" : cell.getStringCellValue();
  }

  public static String questionUpload(MultipartFile file) throws Exception {

    log.info("开始导入题目");
    return "ok";
  }
}

// 绑定主键代码
//    CREATE CONSTRAINT ON (point:Point) ASSERT point.pointId IS UNIQUE
//    CREATE CONSTRAINT ON (question:Question) ASSERT question.questionId IS UNIQUE

// 清空代码
//  MATCH (n)
//  OPTIONAL MATCH (n)-[r]-()
//  DELETE n,r
