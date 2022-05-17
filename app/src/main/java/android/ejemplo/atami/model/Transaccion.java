package android.ejemplo.atami.model;

import java.util.Date;

public class Transaccion {

    private Long id_transaccion;
    private Long id_cuenta_bancaria;
    private int cantidad;
    private String tipo;
    private Date fecha;
    private String categoria;
    private String descripcion;
    private int resultadoBalance;

    public Transaccion() {
    }

    public Transaccion(Long id_transaccion, Long id_cuenta_bancaria, int cantidad, String tipo, Date fecha, String categoria, String descripcion, int resultadoBalance) {
        this.id_transaccion = id_transaccion;
        this.id_cuenta_bancaria = id_cuenta_bancaria;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.fecha = fecha;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.resultadoBalance = resultadoBalance;
    }

    public Long getId_transaccion() {
        return id_transaccion;
    }

    public void setId_transaccion(Long id_transaccion) {
        this.id_transaccion = id_transaccion;
    }

    public Long getId_cuenta_bancaria() {
        return id_cuenta_bancaria;
    }

    public void setId_cuenta_bancaria(Long id_cuenta_bancaria) {
        this.id_cuenta_bancaria = id_cuenta_bancaria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getResultadoBalance() {
        return resultadoBalance;
    }

    public void setResultadoBalance(int resultadoBalance) {
        this.resultadoBalance = resultadoBalance;
    }
}
