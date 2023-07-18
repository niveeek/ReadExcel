package neto.sion.venta.tipos.archivos;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import neto.sion.tienda.genericos.configuraciones.SION;
import neto.sion.tienda.genericos.utilidades.Modulo;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author 10043042
 */
public class ReadExcel {
    private static HSSFSheet hssfSheet;
    private static ArrayList<Integer> rowInteger;
    private static ArrayList<String> rowString;
    private static int rowY1;
    private static int rowY2;
    private static int cellX1;
    private static int cellX2;
    private static Row firstRow;

    public static void FileXLS(String pathExcel, int rowY1, int rowY2, int cellX1, int cellX2) {
        pathExcel = pathExcel.trim();
        ReadExcel.rowY1 = rowY1;
        ReadExcel.rowY2 = rowY2;
        ReadExcel.cellX1 = cellX1;
        ReadExcel.cellX2 = cellX2;
        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(pathExcel));
            hssfSheet = hssfWorkbook.getSheetAt(0);
            firstRow = hssfSheet.getRow(0);
            validateParameters(rowY1, rowY2, cellX1, cellX2);
            getInfoSheet();
            rowString = new ArrayList<String>();
            rowInteger = new ArrayList<Integer>();
            getValuesSelected();
            SION.log(Modulo.VENTA, String.valueOf(rowString), Level.INFO);
        } catch (FileNotFoundException fileNotFoundException) {
            SION.logearExcepcion(Modulo.VENTA, fileNotFoundException, "No existe el archivo.");
        } catch (IOException ioException) {
            SION.logearExcepcion(Modulo.VENTA, ioException, "Excepción en la entrada de datos.");
        } catch (Exception exception) {
            if ("".equals(pathExcel)) {
                SION.log(Modulo.VENTA, pathExcel + " no es una cadena de texto", Level.SEVERE);
                System.exit(0);
            } else {
                SION.logearExcepcion(Modulo.VENTA, exception, getStackTrace(exception));
            }
        }
    }

    private static void setValuesSelected(String cellType, Cell cell) {
        if ("NUMERIC".equals(cellType)) {
            rowInteger.add(Integer.parseInt(cell.getStringCellValue()));
        } else if ("STRING".equals(cellType)) {
            rowString.add(cell.getStringCellValue());
        } else if ("FORMULA".equals(cellType)) {
            rowString.add(cell.getStringCellValue());
        } else {
            SION.log(Modulo.VENTA, "Tipo de dato no reconocido.", Level.INFO);
        }
    }

    private static void getValuesSelected() {
        for (int rows = (rowY1 - 1); rows <= (rowY2 - 1); rows++) {
            Row row = hssfSheet.getRow(rows);
            for (int cells = (cellX1 - 1); cells <= (cellX2 - 1); cells++) {
                Cell cell = row.getCell(cells);
                String cellType = cell.getCellTypeEnum().toString();
                if(getCellsNames(firstRow).get(cells)!=null){
                    setValuesSelected(cellType, cell);
                } else {
                    SION.log(Modulo.VENTA, "La columna " + cells + " no tiene nombre.", Level.SEVERE);
                }
            }
        }
    }

    private static String getStackTrace(Throwable aThrowable) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    private static int getSheetRows(HSSFSheet hssfSheet) {
        ReadExcel.hssfSheet = hssfSheet;
        return hssfSheet.getLastRowNum() + 1;
    }

    private static int getSheetCells(Row firstRow) {
        ReadExcel.firstRow = firstRow;
        return firstRow.getLastCellNum();
    }

    private static ArrayList<String> getCellsNames(Row firstRow) {
        ReadExcel.firstRow = firstRow;
        ArrayList<String> cellNamesList = new ArrayList<String>();
        for (int z = 0; z < firstRow.getLastCellNum(); z++) {
            Cell cellName = firstRow.getCell(z);
            cellNamesList.add(cellName.toString());
        }
        return cellNamesList;
    }

    private static String getSheetName(HSSFSheet hssfSheet) {
        return hssfSheet.getSheetName();
    }

    private static void validateParameters(int rowY1, int rowY2, int cellX1, int cellX2) {
        if ((rowY1 == 0 || rowY1 == 1)) {
            SION.log(Modulo.VENTA, "rowY1 = " + rowY1 + " pertenece a los encabezados.", Level.SEVERE);
            System.exit(0);
        } else if (rowY1 > getSheetRows(hssfSheet)) {
            SION.log(Modulo.VENTA, "rowY1 = " + rowY1 + " está fuera del rango de filas", Level.SEVERE);
            System.exit(0);
        } else if (rowY1 > getSheetCells(firstRow)) {
            SION.log(Modulo.VENTA, "rowY1 = " + rowY1 + " está fuera del rango de columnas", Level.SEVERE);
            System.exit(0);
        } else if (rowY2 > getSheetRows(hssfSheet)) {
            SION.log(Modulo.VENTA, "rowY2 = " + rowY2 + " está fuera del rango de filas", Level.SEVERE);
            System.exit(0);
        } else if (cellX1 > getSheetCells(firstRow)) {
            SION.log(Modulo.VENTA, "cellX1 = " + cellX1 + " está fuera del rango de columnas", Level.SEVERE);
            System.exit(0);
        } else if (cellX2 > getSheetCells(firstRow)) {
            SION.log(Modulo.VENTA, "cellX2 = " + cellX2 + " está fuera del rango de columnas", Level.SEVERE);
            System.exit(0);
        } else {
            SION.log(Modulo.VENTA, "Parámetros validados correctamente", Level.INFO);
        }
    }

    private static void getInfoSheet(){
        SION.log(Modulo.VENTA, getSheetName(hssfSheet) + "{ SheetRows: " + getSheetRows(hssfSheet) +
                ", SheetCells: " + getSheetCells(firstRow) + ", SelectedRows: " + ((rowY2-rowY1) + 1) +
                ", SelectedCells: " + ((cellX2-cellX1) + 1) + " }", Level.INFO);
    }

    public static void main(String[] args) {
        ReadExcel.FileXLS("C:\\Users\\10043042\\Documents\\IntelliJProjects\\ReadExcel\\davidOriginal.xls", 2, 3, 6, 8);
    }
}
