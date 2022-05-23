package android.ejemplo.atami.model;

public class Cuenta_bancaria {

    private Float total;
    private String nombre_cuenta;

    public Cuenta_bancaria() {
    }

    public Cuenta_bancaria(Float total) {
        this.total = total;
    }

    public Cuenta_bancaria(Float total, String nombre_cuenta) {
        this.total = total;
        this.nombre_cuenta = nombre_cuenta;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getNombre_cuenta() {
        return nombre_cuenta;
    }

    public void setNombre_cuenta(String nombre_cuenta) {
        this.nombre_cuenta = nombre_cuenta;
    }
}
