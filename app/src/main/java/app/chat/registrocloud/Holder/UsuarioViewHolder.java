package app.chat.registrocloud.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.chat.registrocloud.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView civFotodeperfil;
    private TextView nombredegrupo;
    private LinearLayout layoutPrincipal;

    public UsuarioViewHolder(@NonNull View itemView) {
        super(itemView);

        civFotodeperfil = itemView.findViewById(R.id.cipfotodeperfil);
        nombredegrupo = itemView.findViewById(R.id.nombregrupo);
        layoutPrincipal =  itemView.findViewById(R.id.layoutPrincipal);
    }

    public CircleImageView getCivFotodeperfil() {
        return civFotodeperfil;
    }

    public void setCivFotodeperfil(CircleImageView civFotodeperfil) {
        this.civFotodeperfil = civFotodeperfil;
    }

    public TextView getNombredegrupo() {
        return nombredegrupo;
    }

    public void setNombredegrupo(TextView nombredegrupo) {
        this.nombredegrupo = nombredegrupo;
    }

    public LinearLayout getLayoutPrincipal() {
        return layoutPrincipal;
    }

    public void setLayoutPrincipal(LinearLayout layoutPrincipal) {
        this.layoutPrincipal = layoutPrincipal;
    }
}
