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
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        // 获取第一张表(知识点表)
        Sheet sheet = workbook.getSheetAt(0);

        Session session = driver.session();
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
                        parameters( "pointId",row.getCell(1)==null ? "" : row.getCell(1).getStringCellValue(),
                                "pointDetail",row.getCell(0)==null ? "" : row.getCell(0).getStringCellValue(),
                                "chapter",row.getCell(3)==null ? "" : row.getCell(3).getStringCellValue(),
                                "isbn",row.getCell(4)==null ? "" : row.getCell(4).getStringCellValue(),
                                "grade",row.getCell(5)==null ? "" : row.getCell(5).getStringCellValue(),
                                "distribution",row.getCell(6)==null ? "" : row.getCell(6).getStringCellValue(),
                                "frequency",row.getCell(7)==null ? "" : row.getCell(7).getStringCellValue() ) );

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
                                                parameters( "pointId1",row.getCell(2).getStringCellValue(),
                                                    "pointId2",row.getCell(1).getStringCellValue()) );
                Record record = result.next();

                //两个知识点之间不存在关系
                if("0".equals(record.get("count(r)").toString())){
                    session.run( "MATCH (a:Point),(b:Point)" +
                                    "WHERE a.pointId = {pointId1} AND b.pointId = {pointId2}" +
                                    "CREATE (a)-[r:子知识点] -> (b)",
                            parameters( "pointId1",row.getCell(2).getStringCellValue(),
                                    "pointId2",row.getCell(1).getStringCellValue()) );
                }
                else{
                    throw new Exception("知识点关系重复");
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









        session.close();
        if(flag1 && flag2){
            return resultMsg;
        }
        else{
            throw new Exception(resultMsg);
        }
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