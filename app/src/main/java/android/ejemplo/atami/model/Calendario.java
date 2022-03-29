package android.ejemplo.atami.model;

import java.util.ArrayList;
import java.util.Date;

public class Calendario {

    private int anyo;
    private int mes;
    private int dia;
    //private String calendario;
    private ArrayList<Transaccion> eventos;

    public Calendario() {
    }

    public Calendario(int anyo, int mes, int dia, ArrayList<Transaccion> eventos) {
        this.anyo = anyo;
        this.mes = mes;
        this.dia = dia;
        this.eventos = eventos;
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public ArrayList<Transaccion> getEventos() {
        return eventos;
    }

    public void setEventos(ArrayList<Transaccion> eventos) {
        this.eventos = eventos;
    }
}
