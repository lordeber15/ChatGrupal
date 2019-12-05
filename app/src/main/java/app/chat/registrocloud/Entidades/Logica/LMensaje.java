package app.chat.registrocloud.Entidades.Logica;

import app.chat.registrocloud.Entidades.Firebase.Mensajes;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

public class LMensaje {
    private String key;
    private Mensajes mensajes;
    private LUsuario lUsuario;

    public LMensaje(String key, Mensajes mensajes) {
        this.key = key;
        this.mensajes = mensajes;
    }



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Mensajes getMensajes() {
        return mensajes;
    }

    public void setMensajes(Mensajes mensajes) {
        this.mensajes = mensajes;
    }

    public long getcreateTimestampLog(){
        return (long) mensajes.getCreatedTimestamp();
    }

    public LUsuario getlUsuario() {
        return lUsuario;
    }

    public void setlUsuario(LUsuario lUsuario) {
        this.lUsuario = lUsuario;
    }

    public String fechadeCreaciondemensaje(){

        Date date = new Date(getcreateTimestampLog());
        PrettyTime prettyTime = new PrettyTime(new Date(),Locale.getDefault());
        return prettyTime.format(date);
        /*Date date = new Date(getcreateTimestampLog());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        return sdf.format(date);*/
    }
}
