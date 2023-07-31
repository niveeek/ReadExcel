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
    private Row firstRow;
    private static final int NUMERO_FILAS_BLOQUE_EXCEL = Integer.parseInt(
            SION.obtenerParametro(Modulo.VENTA,"NUMERO.FILAS.BLOQUE.EXCEL"));

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
            for (int rows = 1; rows <= 5; rows++) {
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

    public String getStackTrace(Throwable aThrowable) {
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
            for (int j = 0; j < newSubList.size(); j++) {
                String element = newSubList.get(j);
                element = element.trim();
                element = element.replace("$", "");
                element = element.replace(",", "");
                element = element.replace("-", "");
                newSubList.set(j, element);
            }
            subLists.add(newSubList);
        }
        return subLists;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getBlockSubLists(ArrayList<ArrayList<String>> subLists) {
        ArrayList<ArrayList<ArrayList<String>>> groupedLists = new ArrayList<ArrayList<ArrayList<String>>>();
        for (int i = 0; i < subLists.size(); i += NUMERO_FILAS_BLOQUE_EXCEL) {
            int endIndex = Math.min(i + NUMERO_FILAS_BLOQUE_EXCEL, subLists.size());
            ArrayList<ArrayList<String>> newGroup = new ArrayList<ArrayList<String>>(subLists.subList(i, endIndex));
            groupedLists.add(newGroup);
        }
        return groupedLists;
    }

    private int getElementsSize(ArrayList<String> subArray) {
        return subArray.size();
    }

    private int getBlockSize(ArrayList<ArrayList<ArrayList<String>>> block) {
        return block.size();
    }

    public String getElementFromSubArray(ArrayList<ArrayList<ArrayList<String>>> blockSubArray, int indexBlock, int indexSubArray, int indexElement) {
        try {
            int blockSize = getBlockSize(blockSubArray);
            int elementsSubArray = getElementsSize(blockSubArray.get(indexBlock).get(indexSubArray));
            SION.log(Modulo.VENTA, "DimensionsExcel{ totalBlocks = " + blockSize + ", totalSubArrays = " + NUMERO_FILAS_BLOQUE_EXCEL + ", totalElementsSubArray = " + elementsSubArray + "}", Level.INFO);
            SION.log(Modulo.VENTA, "getElementFromSublist{ indexBlock = " + indexBlock + ", indexSubArray = " + indexSubArray + ", indexElement = " + indexElement + "}", Level.INFO);
            if (indexBlock >= blockSize) {
                SION.log(Modulo.VENTA, "indexBlock = " + indexBlock + " Fuera del rango de bloques.", Level.INFO);
                return null;
            } else if (indexSubArray >= NUMERO_FILAS_BLOQUE_EXCEL) {
                SION.log(Modulo.VENTA, "indexSubArray = " + indexSubArray + " Fuera del rango de subList.", Level.INFO);
                return null;
            } else if (indexElement >= elementsSubArray) {
                SION.log(Modulo.VENTA, "indexElement = " + indexElement + " Fuera del rango de elementos.", Level.INFO);
                return null;
            } else {
                return blockSubArray.get(indexBlock).get(indexSubArray).get(indexElement);
            }
        } catch (IndexOutOfBoundsException e) {
            SION.logearExcepcion(Modulo.VENTA, e, "Fuera del rango.");
            return null;
        } catch (NumberFormatException e) {
            SION.logearExcepcion(Modulo.VENTA, e, "Número no válido.");
            return null;
        } catch (Exception e) {
            SION.logearExcepcion(Modulo.VENTA, e, getStackTrace(e));
            return null;
        }
    }

    public void printBlocks(ArrayList<ArrayList<ArrayList<String>>> excelData) {
        int counterBlock = 0;
        int counterSubArray = 0;
        for (ArrayList<ArrayList<String>> group : excelData) {
            for (ArrayList<String> subList : group) {
                SION.log(Modulo.VENTA, "Block #" + counterBlock + ", SubArray #" + counterSubArray, Level.INFO);
                SION.log(Modulo.VENTA, String.valueOf(subList), Level.INFO);
                counterSubArray++;
            }
            counterSubArray = 0;
            counterBlock++;
        }
    }

    public ArrayList<ArrayList<String>> getDataFailed(ArrayList<ArrayList<ArrayList<String>>> excelData) {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        for (int indexBlock = 0; indexBlock < excelData.size(); indexBlock++) {
            ArrayList<ArrayList<String>> block = excelData.get(indexBlock);
            for (int indexSubArray = 0; indexSubArray < block.size(); indexSubArray++) {
                ArrayList<String> subList = block.get(indexSubArray);
                for (int indexElement = 0; indexElement < subList.size(); indexElement++) {
                    String element = subList.get(indexElement);
                    if (element.trim().isEmpty()) {
                        ArrayList<String> indexesList = new ArrayList<String>();
                        indexesList.add(String.valueOf(indexBlock));
                        indexesList.add(String.valueOf(indexSubArray));
                        indexesList.add(String.valueOf(indexElement));
                        result.add(indexesList);
                    }
                }
            }
        }
        return result;
    }

    public int getTotalDataFailed() {
        return getDataFailed(getBlockSubLists(getSubLists(getValuesExcel()))).size();
    }

    public ArrayList<ArrayList<String>> getDataPassed(ArrayList<ArrayList<ArrayList<String>>> excelData) {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        for (int indexBlock = 0; indexBlock < excelData.size(); indexBlock++) {
            ArrayList<ArrayList<String>> block = excelData.get(indexBlock);
            for (int indexSubArray = 0; indexSubArray < block.size(); indexSubArray++) {
                ArrayList<String> subList = block.get(indexSubArray);
                for (int indexElement = 0; indexElement < subList.size(); indexElement++) {
                    String element = subList.get(indexElement);
                    if (element != null && !element.trim().isEmpty()) {
                        ArrayList<String> indexesList = new ArrayList<String>();
                        indexesList.add(String.valueOf(indexBlock));
                        indexesList.add(String.valueOf(indexSubArray));
                        indexesList.add(String.valueOf(indexElement));
                        result.add(indexesList);
                    }
                }
            }
        }
        return result;
    }

    public int getTotalDataPassed() {
        return getDataPassed(getBlockSubLists(getSubLists(getValuesExcel()))).size();
    }

    public void getInfoDataFilter(ArrayList<ArrayList<ArrayList<String>>> excelData) {
        SION.log(Modulo.VENTA, "DataFailed [indexBlock, indexSubArray, indexElement]: " + getDataFailed(excelData), Level.INFO);
        SION.log(Modulo.VENTA, "TotalDataFailed: " + getTotalDataFailed(), Level.INFO);
        SION.log(Modulo.VENTA, "DataPassed [indexBlock, indexSubArray, indexElement]: " + getDataPassed(excelData), Level.INFO);
        SION.log(Modulo.VENTA, "TotalDataPassed: " + getTotalDataPassed(), Level.INFO);
    }

    public static void main(String[] args) {
        ReadExcel readExcel = new ReadExcel ("C:\\Users\\10043042\\Documents\\IntelliJProjects\\ReadExcel\\davidOriginal.xls");

        //readExcel.printBlocks(readExcel.getBlockSubLists(readExcel.getSubLists(readExcel.getValuesExcel())));
        //System.out.println(readExcel.getBlockSubLists(readExcel.getSubLists(readExcel.getValuesExcel())));
        //System.out.println(readExcel.getElementFromSubArray(readExcel.getBlockSubLists(readExcel.getSubLists(readExcel.getValuesExcel())), 1, 1, 13));
        readExcel.getInfoDataFilter(readExcel.getBlockSubLists(readExcel.getSubLists(readExcel.getValuesExcel())));
    }
}
