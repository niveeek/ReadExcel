package neto.sion.venta.dto;

import neto.sion.gen.conn.UtilConn;
import neto.sion.gen.log.Logs;
import neto.sion.tienda.genericos.configuraciones.SION;
import neto.sion.tienda.genericos.utilidades.Modulo;
import neto.sion.venta.ReadExcel;
import neto.sion.venta.bean.operacionRequest;
import neto.sion.venta.bean.operacionResponse;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;
import java.sql.Connection;

import static neto.sion.tienda.genericos.sql.Conexion.obtenerConexion;

public class SP_Implementacion {
    public operacionResponse validateDataExcel(Logs logger) {
        operacionResponse operacionResponse = new operacionResponse();
        operacionRequest operacionRequest = new operacionRequest();
        Connection conn = null;
        OracleConnection oconn;
        OracleCallableStatement cstmt = null;
        String sql;
        try {
            sql = SION.obtenerParametro(Modulo.VENTA,
                    "USRVELIT.PAWSREGISTRADEPOSITOBANCO.SPINSERTTADEPOSITOSBANCOS");
            conn = obtenerConexion();
            if (conn.isWrapperFor(OracleConnection.class)) {
                oconn = conn.unwrap(OracleConnection.class);
            }
            else {
                oconn = (OracleConnection) conn;
            }
            cstmt = (OracleCallableStatement) oconn.prepareCall(sql);
            cstmt.setString(1,operacionRequest.getNumeroCuentaCargo());
            cstmt.setString(2, operacionRequest.getFechaOperacion());
            cstmt.setString(3, operacionRequest.getFechaAplicacion());
            cstmt.setString(4, operacionRequest.getSucursalBanco());
            cstmt.setString(5, operacionRequest.getNumeroMovimiento());
            cstmt.setString(6, operacionRequest.getSaldoInicial());
            cstmt.setString(7, operacionRequest.getDescripcionOperacion());
            cstmt.setString(8, operacionRequest.getImporte());
            cstmt.setString(9, operacionRequest.getConcepto());
            cstmt.setString(10, operacionRequest.getSaldoFinal());
            cstmt.setString(11, operacionRequest.getReferencia());
            cstmt.setString(12, operacionRequest.getNombreBeneficiario());
            cstmt.setString(13, operacionRequest.getCuentaBeneficiario());
            cstmt.setString(14, operacionRequest.getCuentaBeneficiario());
            cstmt.registerOutParameter(4, OracleTypes.NUMBER);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.execute();
            operacionResponse.setPaCdgError(cstmt.getInt(4));
            operacionResponse.setPaDescError(cstmt.getString(5));
        }
        catch (Exception e) {
            logger.log(ReadExcel.getStackTrace(e));
            operacionResponse.setPaCdgError(1);
            operacionResponse.setPaDescError(e.getMessage());
        }
        finally {
            UtilConn.cerrarConexion(conn);
            UtilConn.cerrarRecursos(null, cstmt, null);
        }
        return operacionResponse;
    }
}
