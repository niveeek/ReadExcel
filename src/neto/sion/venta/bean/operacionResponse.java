package neto.sion.venta.bean;

public class operacionResponse {
    private int paCdgError;
    private String paDescError;

    public operacionResponse(int paCdgError, String paDescError) {
        this.paCdgError = paCdgError;
        this.paDescError = paDescError;
    }

    public operacionResponse() {

    }

    public int getPaCdgError() {
        return paCdgError;
    }

    public void setPaCdgError(int paCdgError) {
        this.paCdgError = paCdgError;
    }

    public String getPaDescError() {
        return paDescError;
    }

    public void setPaDescError(String paDescError) {
        this.paDescError = paDescError;
    }

    @Override
    public String toString() {
        return "operacionResponse{" +
                "paCdgError=" + paCdgError +
                ", paDescError='" + paDescError + '\'' +
                '}';
    }
}
