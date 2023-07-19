package neto.sion.venta.tipos.archivos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import neto.sion.tienda.genericos.configuraciones.SION;
import neto.sion.tienda.genericos.utilidades.Modulo;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author 10043042
 */
public class f {
    private static final int NUMERO_FILAS_BLOQUE_EXCEL = Integer.parseInt(
            SION.obtenerParametro(Modulo.VENTA,"NUMERO.FILAS.BLOQUE.EXCEL"));
    public static void ReadFile () {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\10043042\\Documents\\IntelliJProjects\\ReadExcel\\davidOriginal.xls"));
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            int numRow = sheet.getLastRowNum();
            for (int i = 1; i <= NUMERO_FILAS_BLOQUE_EXCEL ; i++) {
                Row row = sheet.getRow(i);
                int numColumn = row.getLastCellNum();
                for (int j = 0; j < numColumn; j++) {
                    Cell cell = row.getCell(j);
                    String s = cell.getCellTypeEnum().toString();
                    if ("NUMERIC".equals(s)) {
                        System.out.print(cell.getNumericCellValue() + " ");
                    } else if ("STRING".equals(s)) {
                        System.out.println(cell.getStringCellValue() + " ");
                    } else if ("FORMULA".equals(s)) {
                        System.out.println(cell.getCellFormula() + " ");
                    } else {
                        System.out.println("Tipo de dato no reconocido.");
                    }
                }
                System.out.println("");
            }

        } catch (FileNotFoundException fileNotFoundException) {
            Logger.getLogger(ReadExcel.class.getName()).log(Level.SEVERE, null, fileNotFoundException);
        } catch (IOException e) {
            Logger.getLogger(ReadExcel.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception ex) {
            Logger.getLogger(ReadExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        ReadFile();
    }
}
