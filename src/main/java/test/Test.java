package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.util.HashMap;

public class Test {

    public static void main(String[] args) throws Exception{

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = workbook.createSheet("测试工作表");

        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell cell = hssfRow.createCell(0);
        cell.setCellValue("2222");
        HSSFRow rowRowName = hssfSheet.createRow(1);        //行
        HSSFCell cell1 = rowRowName.createCell(0);          //列
        cell1.setCellValue("2222");


        hssfSheet.addMergedRegion(new CellRangeAddress(0,1,0,0));//起始行 终止行 起始列 终止列

        /**
         * 输出
         */
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/apple/Documents/ddd.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();

    }

}
