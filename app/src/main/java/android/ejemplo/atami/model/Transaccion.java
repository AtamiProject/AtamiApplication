package android.ejemplo.atami.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaccion {

    private Float cantidad;
    private Date fecha;
    private String categoria;
    private String descripcion;

    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    public Transaccion() {
    }

    public Transaccion(Float cantidad, Date fecha, String categoria, String descripcion) {
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return categoria + ": " + cantidad + "€ - " + formatoFecha.format(fecha);
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() { return fecha; }

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

}
