package neto.sion.venta;

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
    private static final int NUMERO_FILAS_BLOQUE_EXCEL = Integer.parseInt(
            SION.obtenerParametro(Modulo.VENTA,"NUMERO.FILAS.BLOQUE.EXCEL"));
    private static Row firstRow;

    public ReadExcel(String pathExcel) {
        pathExcel = pathExcel.trim();
        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(pathExcel));
            hssfSheet = hssfWorkbook.getSheetAt(0);
            firstRow = hssfSheet.getRow(0);
            validateProperty();
            getInfoSheet();
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

    public static ArrayList<String> getValuesExcel() {
        ArrayList<String> rowString = new ArrayList<String>();
        try {
            for (int rows = 1; rows <= NUMERO_FILAS_BLOQUE_EXCEL; rows++) {
                Row row = hssfSheet.getRow(rows);
                for (int cells = 0; cells < getSheetCells(firstRow); cells++) {
                    Cell cell = row.getCell(cells);
                    String cellType = cell.getCellTypeEnum().toString();
                    if (getCellsNames(firstRow).get(cells) != null) {
                        if ("STRING".equals(cellType)) {
                            rowString.add(cell.getStringCellValue());
                        } else {
                            SION.log(Modulo.VENTA, "Tipo de dato no reconocido.", Level.INFO);
                        }
                    } else {
                        SION.log(Modulo.VENTA, "La columna " + cells + " no tiene nombre.", Level.SEVERE);
                    }
                }
            }
        } catch (NumberFormatException numberFormatException) {
            SION.logearExcepcion(Modulo.VENTA, numberFormatException, "Número no válido.");
        } catch (Exception exception) {
            SION.logearExcepcion(Modulo.VENTA, exception, getStackTrace(exception));
        }
        return rowString;
    }

    public static String getStackTrace(Throwable aThrowable) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    public int getSheetRows(HSSFSheet hssfSheet) {
        return hssfSheet.getLastRowNum() + 1;
    }

    public static int getSheetCells(Row firstRow) {
        return firstRow.getLastCellNum();
    }

    public static ArrayList<String> getCellsNames(Row firstRow) {
        ArrayList<String> cellNamesList = new ArrayList<String>();
        for (int z = 0; z < firstRow.getLastCellNum(); z++) {
            Cell cellName = firstRow.getCell(z);
            cellNamesList.add(cellName.toString());
        }
        return cellNamesList;
    }

    public String getSheetName(HSSFSheet hssfSheet) {
        return hssfSheet.getSheetName();
    }

    public void validateProperty(){
        if (ReadExcel.NUMERO_FILAS_BLOQUE_EXCEL == 0){
            SION.log(Modulo.VENTA, "NUMERO_FILAS_BLOQUE_EXCEL = " +
                    ReadExcel.NUMERO_FILAS_BLOQUE_EXCEL +
                    ", entonces el arreglo se imprime vacío.",Level.INFO);
        }
    }

    public void getInfoSheet(){
        SION.log(Modulo.VENTA, getSheetName(hssfSheet) + " { totalRows: " +
                getSheetRows(hssfSheet) + ", totalCells: " + getSheetCells(firstRow) +
                ", NUMERO_FILAS_BLOQUE_EXCEL = " + NUMERO_FILAS_BLOQUE_EXCEL + " }", Level.INFO);
    }

    public ArrayList<ArrayList<String>> getSubLists(ArrayList<String> arrayExcel) {
        ArrayList<ArrayList<String>> subLists = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < arrayExcel.size(); i += getSheetCells(firstRow)) {
            int endIndex = Math.min(i + getSheetCells(firstRow), arrayExcel.size());
            ArrayList<String> newSubList = new ArrayList<String>(arrayExcel.subList(i, endIndex));
            subLists.add(newSubList);
        }
        return subLists;
    }

    public void getElementFromSublist(ArrayList<ArrayList<String>> sublist, int indexSubList, int indexElement) {
        if (indexSubList > sublist.size()) {
            SION.log(Modulo.VENTA, "Fuera del rango de subList.", Level.INFO);
            if (indexElement > getSheetCells(firstRow)) {
                SION.log(Modulo.VENTA, "Fuera del rango de elementos.", Level.INFO);
            }
        } else {
            SION.log(Modulo.VENTA, String.valueOf(sublist.get(indexSubList).get(indexElement)), Level.INFO);
        }
    }

    public void printSublist(ArrayList<String> subList) {
        for (String element : subList) {
            SION.log(Modulo.VENTA, element, Level.INFO);
        }
        SION.log(Modulo.VENTA, "\n Second subList \n", Level.INFO);
    }

    public static void main(String[] args) {
        ReadExcel readExcel = new ReadExcel ("C:\\Users\\10043042\\Documents\\IntelliJProjects\\ReadExcel\\davidOriginal.xls");
        System.out.println(readExcel.getSubLists(getValuesExcel()));
    }
}
