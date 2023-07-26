package neto.sion.ws.reportes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author 10043042
 */
public class Productos {
    public static void ReadFile () {
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\10043042\\Documents\\IntelliJProjects\\ReadExcel\\productos.xlsx"));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            for (int i = 0; i <= sheet.getLastRowNum() ; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    String s = cell.getCellTypeEnum().toString();
                    if ("NUMERIC".equals(s)) {
                        System.out.print(cell.getNumericCellValue() + " ");
                    } else if ("STRING".equals(s)) {
                        System.out.println(cell.getStringCellValue() + " ");
                    } else {
                        System.out.println("Tipo de dato no reconocido.");
                    }
                }
                System.out.println("");
            }

        } catch (FileNotFoundException fileNotFoundException) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, fileNotFoundException);
        } catch (IOException e) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception ex) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        //ReadFile();
        System.out.println("GUILLERMO GUILLERMO GUILLERMO HERNANDEZ FERNANDEZ HERNANDEZ".length());
    }
}

