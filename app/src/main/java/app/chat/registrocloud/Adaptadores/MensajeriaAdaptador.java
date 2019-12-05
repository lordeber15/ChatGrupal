package app.chat.registrocloud.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import app.chat.registrocloud.Entidades.Logica.LMensaje;
import app.chat.registrocloud.Entidades.Logica.LUsuario;
import app.chat.registrocloud.Holder.MensajeriaHolder;
import app.chat.registrocloud.Persistencia.UsuarioDAO;
import app.chat.registrocloud.R;

import java.util.ArrayList;
import java.util.List;

public class MensajeriaAdaptador extends RecyclerView.Adapter<MensajeriaHolder> {

    private List<LMensaje> listmensaje = new ArrayList<>();
    private Context c;

    public MensajeriaAdaptador(Context c) {
        this.c = c;
    }

    public int addMensaje(LMensaje lMensaje){

        listmensaje.add(lMensaje);
        int posicion = listmensaje.size()-1;
        notifyItemInserted(listmensaje.size());
        return posicion;
    }

    public void actualizarMensaje(int posicion, LMensaje lMensaje){
        listmensaje.set(posicion,lMensaje);
        notifyItemChanged(posicion);
    }


    @Override
    public MensajeriaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if (viewType==1){
            view = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_emisor,parent,false);
        }else {
            view = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_receptor,parent,false);
        }
        return new MensajeriaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeriaHolder holder, int position) {

        LMensaje lMensaje = listmensaje.get(position);

        LUsuario lUsuario = lMensaje.getlUsuario();

        if (lUsuario!=null){
            holder.getNombre().setText(lUsuario.getUsuario().getNombre());
            Glide.with(c).load(lUsuario.getUsuario().getFotoPerfilURL()).into(holder.getFotomensaje());

        }

        holder.getMensaje().setText(lMensaje.getMensajes().getMensaje());
        if(lMensaje.getMensajes().isContieneFoto()){
            holder.getFotomensajeenviar().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(lMensaje.getMensajes().getUrlFoto()).into(holder.getFotomensajeenviar());
        }else
        {
            holder.getFotomensajeenviar().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }
        //


        holder.getHora().setText(lMensaje.fechadeCreaciondemensaje());
    }

    @Override
    public int getItemCount() {
        return listmensaje.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listmensaje.get(position).getlUsuario()!=null){
            if (listmensaje.get(position).getlUsuario().getKey().equals(UsuarioDAO.getInstance().getKeyUsuario())){
                return 1;
            }else
            {
                return -1;
            }
        }else {
            return -1;
        }


        //return super.getItemViewType(position);
    }
}
