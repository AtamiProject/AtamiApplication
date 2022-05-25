package android.ejemplo.atami.model;

import java.util.ArrayList;

public class Usuario {
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private boolean premium;

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String correo, String password, boolean premium) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.password = password;
        this.premium = premium;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }

    public boolean getTipo() {
        return premium;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
