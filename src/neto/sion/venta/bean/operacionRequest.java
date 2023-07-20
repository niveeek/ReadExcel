package neto.sion.venta.bean;

import java.util.ArrayList;

public class operacionRequest {
    private String numeroCuentaCargo;
    private String fechaOperacion;
    private String fechaAplicacion;
    private String sucursalBanco;
    private String numeroMovimiento;
    private String saldoInicial;
    private String descripcionOperacion;
    private String importe;
    private String concepto;
    private String saldoFinal;
    private String referencia;
    private String nombreBeneficiario;
    private String cuentaBeneficiario;
    private String bancoBeneficiario;

    public operacionRequest(String numeroCuentaCargo, String fechaOperacion, String fechaAplicacion, String sucursalBanco, String numeroMovimiento, String saldoInicial, String descripcionOperacion, String importe, String concepto, String saldoFinal, String referencia, String nombreBeneficiario, String cuentaBeneficiario, String bancoBeneficiario) {
        this.numeroCuentaCargo = numeroCuentaCargo;
        this.fechaOperacion = fechaOperacion;
        this.fechaAplicacion = fechaAplicacion;
        this.sucursalBanco = sucursalBanco;
        this.numeroMovimiento = numeroMovimiento;
        this.saldoInicial = saldoInicial;
        this.descripcionOperacion = descripcionOperacion;
        this.importe = importe;
        this.concepto = concepto;
        this.saldoFinal = saldoFinal;
        this.referencia = referencia;
        this.nombreBeneficiario = nombreBeneficiario;
        this.cuentaBeneficiario = cuentaBeneficiario;
        this.bancoBeneficiario = bancoBeneficiario;
    }

    public operacionRequest() {

    }

    public operacionRequest(ArrayList<ArrayList<String>> getDataExcel) {

        for (ArrayList<String> s: getDataExcel) {

        }
    }

    public String getNumeroCuentaCargo() {
        return numeroCuentaCargo;
    }

    public void setNumeroCuentaCargo(String numeroCuentaCargo) {
        this.numeroCuentaCargo = numeroCuentaCargo;
    }

    public String getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getSucursalBanco() {
        return sucursalBanco;
    }

    public void setSucursalBanco(String sucursalBanco) {
        this.sucursalBanco = sucursalBanco;
    }

    public String getNumeroMovimiento() {
        return numeroMovimiento;
    }

    public void setNumeroMovimiento(String numeroMovimiento) {
        this.numeroMovimiento = numeroMovimiento;
    }

    public String getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(String saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getDescripcionOperacion() {
        return descripcionOperacion;
    }

    public void setDescripcionOperacion(String descripcionOperacion) {
        this.descripcionOperacion = descripcionOperacion;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(String saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNombreBeneficiario() {
        return nombreBeneficiario;
    }

    public void setNombreBeneficiario(String nombreBeneficiario) {
        this.nombreBeneficiario = nombreBeneficiario;
    }

    public String getCuentaBeneficiario() {
        return cuentaBeneficiario;
    }

    public void setCuentaBeneficiario(String cuentaBeneficiario) {
        this.cuentaBeneficiario = cuentaBeneficiario;
    }

    public String getBancoBeneficiario() {
        return bancoBeneficiario;
    }

    public void setBancoBeneficiario(String bancoBeneficiario) {
        this.bancoBeneficiario = bancoBeneficiario;
    }

    @Override
    public String toString() {
        return "operacionRequest{" +
                "numeroCuentaCargo='" + numeroCuentaCargo + '\'' +
                ", fechaOperacion='" + fechaOperacion + '\'' +
                ", fechaAplicacion='" + fechaAplicacion + '\'' +
                ", sucursalBanco='" + sucursalBanco + '\'' +
                ", numeroMovimiento='" + numeroMovimiento + '\'' +
                ", saldoInicial='" + saldoInicial + '\'' +
                ", descripcionOperacion='" + descripcionOperacion + '\'' +
                ", importe='" + importe + '\'' +
                ", concepto='" + concepto + '\'' +
                ", saldoFinal='" + saldoFinal + '\'' +
                ", referencia='" + referencia + '\'' +
                ", nombreBeneficiario='" + nombreBeneficiario + '\'' +
                ", cuentaBeneficiario='" + cuentaBeneficiario + '\'' +
                ", bancoBeneficiario='" + bancoBeneficiario + '\'' +
                '}';
    }
}
