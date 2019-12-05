package app.chat.registrocloud.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.chat.registrocloud.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajeriaHolder extends RecyclerView.ViewHolder {

    private TextView nombre;
    private TextView mensaje;
    private TextView hora;
    private CircleImageView fotomensaje;
    private ImageView fotomensajeenviar;

    public MensajeriaHolder(@NonNull View itemView) {
        super(itemView);
        nombre = (TextView) itemView.findViewById(R.id.nombremensaje);
        mensaje = (TextView) itemView.findViewById(R.id.mensajemensaje);
        hora = (TextView) itemView.findViewById(R.id.horamensaje);
        fotomensaje = (CircleImageView) itemView.findViewById(R.id.fotoperfilmensaje);
        fotomensajeenviar = (ImageView) itemView.findViewById(R.id.mensajefoto);
    }

    public ImageView getFotomensajeenviar() {
        return fotomensajeenviar;
    }

    public void setFotomensajeenviar(ImageView fotomensajeenviar) {
        this.fotomensajeenviar = fotomensajeenviar;
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public void setMensaje(TextView mensaje) {
        this.mensaje = mensaje;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public CircleImageView getFotomensaje() {
        return fotomensaje;
    }

    public void setFotomensaje(CircleImageView fotomensaje) {
        this.fotomensaje = fotomensaje;
    }
}
