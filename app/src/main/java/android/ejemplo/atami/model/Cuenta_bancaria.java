package android.ejemplo.atami.model;

public class Cuenta_bancaria {

    private Long id_cuenta;
    private int total;
    private String nombre_cuenta;

    public Cuenta_bancaria() {
    }

    public Cuenta_bancaria(Long id_cuenta, int total, String nombre_cuenta) {
        this.id_cuenta = id_cuenta;
        this.total = total;
        this.nombre_cuenta = nombre_cuenta;
    }

    public Long getId_cuenta() {
        return id_cuenta;
    }

    public void setId_cuenta(Long id_cuenta) {
        this.id_cuenta = id_cuenta;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNombre_cuenta() {
        return nombre_cuenta;
    }

    public void setNombre_cuenta(String nombre_cuenta) {
        this.nombre_cuenta = nombre_cuenta;
    }
}
