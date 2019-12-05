package app.chat.registrocloud.Entidades.Logica;

import app.chat.registrocloud.Entidades.Firebase.Usuario;
import app.chat.registrocloud.Persistencia.UsuarioDAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LUsuario {
    private String key;
    private Usuario usuario;

    public LUsuario(String key, Usuario usuario) {
        this.key = key;
        this.usuario = usuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String obtenerFechadeCreacion(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date= new Date(UsuarioDAO.getInstance().fechadeCreacionLong());
        return simpleDateFormat.format(date);
    }

    public String obtenerFechadeUltimavezqueselogeo(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date= new Date(UsuarioDAO.getInstance().obtenerFechadeUltimavezqueselogeoLog());
        return simpleDateFormat.format(date);
    }

}
