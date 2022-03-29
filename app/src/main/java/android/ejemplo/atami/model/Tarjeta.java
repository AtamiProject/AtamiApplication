package android.ejemplo.atami.model;

import java.util.Date;

public class Tarjeta {

    private Long id_tarjeta;
    private Long numero;
    private Date caducidad;

    public Tarjeta() {
    }

    public Tarjeta(Long id_tarjeta, Long numero, Date caducidad) {
        this.id_tarjeta = id_tarjeta;
        this.numero = numero;
        this.caducidad = caducidad;
    }

    public Long getId_tarjeta() {
        return id_tarjeta;
    }

    public void setId_tarjeta(Long id_tarjeta) {
        this.id_tarjeta = id_tarjeta;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Date getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }
}
