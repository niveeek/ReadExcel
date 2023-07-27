package neto.sion.ws.reportes;

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
public final class ReadExcel {
    private HSSFSheet hssfSheet;
    private static final int NUMERO_FILAS_BLOQUE_EXCEL = Integer.parseInt(
            SION.obtenerParametro(Modulo.VENTA,"NUMERO.FILAS.BLOQUE.EXCEL"));
    private Row firstRow;
    private int newSubListSize;
    private final ArrayList<ArrayList<ArrayList<String>>> groupedLists = new ArrayList<ArrayList<ArrayList<String>>>();

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

    public ArrayList<String> getValuesExcel() {
        ArrayList<String> rowString = new ArrayList<String>();
        try {
            for (int rows = 1; rows <= 12; rows++) {
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

    public int getSheetCells(Row firstRow) {
        return firstRow.getLastCellNum();
    }

    public ArrayList<String> getCellsNames(Row firstRow) {
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
                ", NUMERO_FILAS_BLOQUE_EXCEL = " + NUMERO_FILAS_BLOQUE_EXCEL +
                ", ENCABEZADOS: " + getCellsNames(firstRow) + " }", Level.INFO);
    }

    public ArrayList<ArrayList<String>> getSubLists(ArrayList<String> arrayExcel) {
        ArrayList<ArrayList<String>> subLists = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < arrayExcel.size(); i += getSheetCells(firstRow)) {
            int endIndex = Math.min(i + getSheetCells(firstRow), arrayExcel.size());
            ArrayList<String> newSubList = new ArrayList<String>(arrayExcel.subList(i, endIndex));
            subLists.add(newSubList);
            newSubListSize = newSubList.size();
        }
        return subLists;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getBlockSubLists(ArrayList<ArrayList<String>> subLists) {
        for (int i = 0; i < subLists.size(); i += NUMERO_FILAS_BLOQUE_EXCEL) {
            int endIndex = Math.min(i + NUMERO_FILAS_BLOQUE_EXCEL, subLists.size());
            ArrayList<ArrayList<String>> newGroup = new ArrayList<ArrayList<String>>(subLists.subList(i, endIndex));
            groupedLists.add(newGroup);
        }
        return groupedLists;
    }

    public void getElementFromSublist(ArrayList<ArrayList<ArrayList<String>>> blockSubLists, int indexBlock, int indexSubList, int indexElement) {
        indexSubList--;
        indexElement--;
        indexBlock--;
        if (indexBlock >= groupedLists.size()) {
            SION.log(Modulo.VENTA, "indexBlock = " + indexBlock + " Fuera del rango de bloques.", Level.INFO);
        } else if (indexSubList >= blockSubLists.size()) {
            SION.log(Modulo.VENTA, "indexSubList = " + indexSubList + " Fuera del rango de subList.", Level.INFO);
        } else if (indexElement >= newSubListSize) {
            SION.log(Modulo.VENTA, "indexElement = " + indexElement + " Fuera del rango de elementos.", Level.INFO);
        }  else {
            SION.log(Modulo.VENTA, String.valueOf(blockSubLists.get(indexBlock).get(indexSubList).get(indexElement)), Level.INFO);
        }
    }

    public void printBlocks() {
        int counterBlock = 1;
        for (ArrayList<ArrayList<String>> group : getBlockSubLists(getSubLists(getValuesExcel()))) {
            SION.log(Modulo.VENTA, "Block #" + counterBlock, Level.INFO);
            for (ArrayList<String> subList : group) {
                SION.log(Modulo.VENTA, String.valueOf(subList), Level.INFO);
            }
            counterBlock++;
        }
    }

    public static void main(String[] args) {
        ReadExcel readExcel = new ReadExcel ("C:\\Users\\10043042\\Documents\\IntelliJProjects\\ReadExcel\\davidOriginal.xls");
        //System.out.println(readExcel.blockSubLists(readExcel.getSubLists(readExcel.getValuesExcel())));
        readExcel.printBlocks();
        readExcel.getElementFromSublist(readExcel.getBlockSubLists(readExcel.getSubLists(readExcel.getValuesExcel())), 1, 4, 8);
    }
}
