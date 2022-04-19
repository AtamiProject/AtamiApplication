package android.ejemplo.atami.modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Eventos {

    private int id;
    private Date fecha;
    private String nombre;
    private String descripcion;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");

    public Eventos(Date fecha, String nombre, String descripcion) {
        this.fecha = fecha;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "Evento [ fecha=" + dateFormat.format(fecha) + ", nombre=" + nombre + ", descripcion=" + descripcion + " ]";
    }
}
