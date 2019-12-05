package app.chat.registrocloud.Entidades.Firebase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.PriorityQueue;

public class Usuario {

    private String fotoPerfilURL;
    private String nombre;
    private String correo;
    private long fechadenacimiento;
    private String Direccion;
    private String valoracion;
    private String genero;
    private String NombreGrupo;

    public Usuario() {

    }

    public String getFotoPerfilURL() {
        return fotoPerfilURL;
    }

    public void setFotoPerfilURL(String fotoPerfilURL) {
        this.fotoPerfilURL = fotoPerfilURL;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public long getFechadenacimiento() {
        return fechadenacimiento;
    }

    public void setFechadenacimiento(long fechadenacimiento) {
        this.fechadenacimiento = fechadenacimiento;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNombreGrupo() {
        return NombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        NombreGrupo = nombreGrupo;
    }
}
