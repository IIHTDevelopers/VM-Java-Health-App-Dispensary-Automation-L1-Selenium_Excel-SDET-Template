package coreUtilities.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;

public class FileOperations 
{
	public JSONParser jsonParser;
	public JSONObject jsonObject;
	public Fillo fillo;
	public Connection connection;
	public Properties properties;
	
	/**
     * This method is useful to read the excel sheet based on the Filename and sheet name. It'll return the values for the respective
     * sheet in {@link Map} where the first column name as a key and the value as per the value entered in econd column.
     * @param excelFilePath - {@link String} excel sheet location
     * @param sheetName - {@link String} Sheet name to read the excel
     * @return {@link Map}
     * @throws Exception
     */
	public Map<String, String> readExcelPOI(String excelFilePath, String sheetName) throws FilloException
	{
	    HashMap<String, String> dataMap = new HashMap<>();

	    try (FileInputStream fis = new FileInputStream(excelFilePath);
	         Workbook workbook = new XSSFWorkbook(fis)) {

	        // Get the first sheet
	        //Sheet sheet = workbook.getSheetAt(0);
	        Sheet sheet = workbook.getSheet(sheetName);

	        // Iterate through each row in the sheet
	        Iterator<Row> rowIterator = sheet.iterator();
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();

	            // Get the first and second cell from each row
	            Cell keyCell = row.getCell(0);
	            Cell valueCell = row.getCell(1);

	            // Convert cells to string and put them in the HashMap
	            String key = getCellValueAsString(keyCell);
	            String value = getCellValueAsString(valueCell);

	            dataMap.put(key, value);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return dataMap;
	}
	private static String getCellValueAsString(Cell cell) {
	    if (cell == null) {
	        return "";
	    }

	    switch (cell.getCellType()) {
	        case STRING:
	            return cell.getStringCellValue();
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	                return cell.getDateCellValue().toString();
	            } else {
	                return String.valueOf((int)cell.getNumericCellValue());
	            }
	        case BOOLEAN:
	            return String.valueOf(cell.getBooleanCellValue());
	        case FORMULA:
	            return cell.getCellFormula();
	        default:
	            return "";
	    }
	}
	
}
