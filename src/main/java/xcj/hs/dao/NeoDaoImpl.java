package xcj.hs.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.neo4j.driver.v1.*;
import org.springframework.web.multipart.MultipartFile;


import static org.neo4j.driver.v1.Values.parameters;

@Slf4j
public class NeoDaoImpl {

    private final static Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123456" ) );

    public static void test(){
        log.info("Neo4j数据库连接");
    }


    public static void getAllProblem(){
        Session session = driver.session();
        StatementResult result = session.run( "MATCH (a:Person) RETURN a.name AS name, a.title AS title",
                    parameters( "name", "Arthur001" ) );
        while ( result.hasNext() )
        {
            Record record = result.next();
            log.info( record.get( "title" ).asString() + " " + record.get( "name" ).asString() + " " + record.get( "id" ).asString() );
        }
        session.close();
    }

    public static String excelUpload(MultipartFile file) throws Exception {

        Session session = driver.session();

        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        // 获取第一张表(知识点表)
        Sheet sheet = workbook.getSheetAt(0);
        String resultMsg="";

        log.info("开始导入知识点");
        boolean flag1=true;
        try{
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                for (Cell cell : row) {
                    //读取数据前设置单元格类型
                    cell.setCellType(CellType.STRING);
                }

                session.run( "CREATE (a:Point {" +
                                "pointId: {pointId}, " +
                                "pointDetail: {pointDetail}, " +
                                "chapter: {chapter}, " +
                                "isbn: {isbn}, " +
                                "grade: {grade}," +
                                "distribution: {distribution}, " +
                                "frequency: {frequency}})",
                        parameters( "pointId", getString(row.getCell(1)),
                                "pointDetail", getString(row.getCell(0)),
                                "chapter", getString(row.getCell(3)),
                                "isbn", getString(row.getCell(4)),
                                "grade", getString(row.getCell(5)),
                                "distribution", getString(row.getCell(6)),
                                "frequency", getString(row.getCell(7)) ) );

            }
        }catch (Exception e){
            resultMsg+="知识点导入失败：知识点id重复---------\n";
            flag1=false;
        }
        finally {
            if(flag1){
                resultMsg+="知识点导入成功---------\n";
            }
        }


        log.info("开始导入知识点之间的关系");
        boolean flag2=true;
        try{
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                for (Cell cell : row) {
                    //读取数据前设置单元格类型
                    cell.setCellType(CellType.STRING);
                }

                if (row.getCell(2)==null ||
                        row.getCell(1)==null ||
                        StringUtils.isBlank(row.getCell(2).getStringCellValue()) ||
                        StringUtils.isBlank(row.getCell(1).getStringCellValue()) ||
                        "null".equals(row.getCell(2).getStringCellValue()) ||
                        "null".equals(row.getCell(1).getStringCellValue())){
                    continue;
                }

                StatementResult result = session.run( "MATCH (a:Point{pointId:{pointId1}}),(b:Point{pointId:{pointId2}}),r=(a)-[]-(b)" +
                                                        "return count(r)",
                                                parameters( "pointId1", getString(row.getCell(2)),
                                                    "pointId2", getString(row.getCell(1))) );
                Record record = result.next();

                //两个知识点之间不存在关系
                if("0".equals(record.get("count(r)").toString())){
                    session.run( "MATCH (a:Point),(b:Point)" +
                                    "WHERE a.pointId = {pointId1} AND b.pointId = {pointId2}" +
                                    "CREATE (a)-[r:子知识点] -> (b)",
                            parameters( "pointId1", getString(row.getCell(2)),
                                    "pointId2", getString(row.getCell(1))) );
                }
                else{
                    flag2=false;
                }
            }
        }catch (Exception e){
            resultMsg+="知识点之间的关系导入失败："+e.getMessage()+"---------";
            flag2=false;
        }
        finally {
            if(flag2){
                resultMsg+="知识点之间的关系导入成功---------";
            }
        }

        // 获取第二张表(题目表)
        sheet = workbook.getSheetAt(1);

        log.info("开始导入题目");
        boolean flag3=true;
        try{
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                for (Cell cell : row) {
                    //读取数据前设置单元格类型
                    cell.setCellType(CellType.STRING);
                }

                session.run( "CREATE (a:Question {" +
                                "questionId: {questionId}, " +
                                "questionDetail: {questionDetail}, " +
                                "solution: {solution}, " +
                                "score: {score}, " +
                                "typeDistribution: {typeDistribution}, " +
                                "difficultyDistribution: {difficultyDistribution}})",
                        parameters( "questionId", getString(row.getCell(0)),
                                "questionDetail", getString(row.getCell(1)),
                                "solution", getString(row.getCell(2)),
                                "score", getString(row.getCell(3)),
                                "typeDistribution", getString(row.getCell(4)),
                                "difficultyDistribution", getString(row.getCell(5))
                                )
                );

            }
        }catch (Exception e){
            resultMsg+="题目导入失败：题目id重复---------\n";
            flag3=false;
        }
        finally {
            if(flag3){
                resultMsg+="题目导入成功---------\n";
            }
        }


        // 获取第三张表(题目知识点对应表)
        sheet = workbook.getSheetAt(2);

        log.info("开始导入题目和知识点的关系");
        boolean flag4=true;
        try{
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                for (Cell cell : row) {
                    //读取数据前设置单元格类型
                    cell.setCellType(CellType.STRING);
                }

                StatementResult result = session.run( "MATCH (a:Question{questionId:{questionId}}),(b:Point{pointId:{pointId}}),r=(a)-[]-(b)" +
                                "return count(r)",
                        parameters( "questionId", getString(row.getCell(0)),
                                "pointId", getString(row.getCell(1))) );
                Record record = result.next();

                //知识点和题目之间不存在关系
                if("0".equals(record.get("count(r)").toString())){
                    session.run( "MATCH (a:Question),(b:Point)" +
                                    "WHERE a.questionId = {questionId} AND b.pointId = {pointId}" +
                                    "CREATE (a)-[r:属于] -> (b)",
                            parameters( "questionId", getString(row.getCell(0)),
                                    "pointId", getString(row.getCell(1))) );
                }
                else{
                    flag4=false;
                }

            }
        }catch (Exception e){
            resultMsg+="题目和知识点的关系导入失败：题目id重复---------\n";
            flag4=false;
        }
        finally {
            if(flag4){
                resultMsg+="题目和知识点的关系导入成功---------\n";
            }
        }



        session.close();
        if(flag1 && flag2 && flag3 && flag4){
            return resultMsg;
        }
        else{
            throw new Exception(resultMsg);
        }
    }


    private static String getString(Cell cell){
        return cell==null ? "" : cell.getStringCellValue();
    }


    public static String questionUpload(MultipartFile file) throws Exception {

        log.info("开始导入题目");
        return "ok";
    }


    public static void batchImport(){
//        File file = new File("C:\\Users\\lenovo\\Desktop\\xiaoyuantest\\小猿搜题_错题本_20191103160747_json.txt");
//        if(file.isFile() && file.exists()){
//            try {
//                FileInputStream fileInputStream = new FileInputStream(file);
//                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuffer sb = new StringBuffer();
//                String text = null;
//                while((text = bufferedReader.readLine()) != null){
//                    JSONObject object = new JSONObject(text);
//                }
//                log.info(sb.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}

//        try{
//            Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123456" ) );
//            Session session = driver.session();
//
//            session.run( "CREATE (a:Person {name: {name}, title: {title}})",
//                    parameters( "name", "Arthur001", "title", "King001" ) );
//
//            StatementResult result = session.run( "MATCH (a:Person) WHERE a.name = {name} " +
//                            "RETURN a.name AS name, a.title AS title",
//                    parameters( "name", "Arthur001" ) );
//
//            while ( result.hasNext() )
//            {
//                Record record = result.next();
//                System.out.println( record.get( "title" ).asString() + " " + record.get( "name" ).asString() + " " + record.get( "id" ).asString() );
//            }
//
//            session.close();
//            driver.close();
//
//        }catch(Exception e){
//            System.out.println( "-------------"+e );
//        }










//绑定主键代码
//    CREATE CONSTRAINT ON (point:Point) ASSERT point.pointId IS UNIQUE
//    CREATE CONSTRAINT ON (question:Question) ASSERT question.questionId IS UNIQUE