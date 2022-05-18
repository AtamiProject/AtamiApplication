package android.ejemplo.atami.model;

import java.util.Date;

public class Tarjeta {

    private Long numero;
    private Date caducidad;

    public Tarjeta() {
    }

    public Tarjeta(Long id_tarjeta, Long numero, Date caducidad) {
        this.numero = numero;
        this.caducidad = caducidad;
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
