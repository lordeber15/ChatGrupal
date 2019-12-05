package app.chat.registrocloud.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import  app.chat.registrocloud.Entidades.Firebase.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import app.chat.registrocloud.Entidades.Logica.LUsuario;
import app.chat.registrocloud.Holder.UsuarioViewHolder;
import app.chat.registrocloud.R;
import app.chat.registrocloud.Utilidades.Constantes;

public class Ver_grupos extends AppCompatActivity {

    private RecyclerView rvGrupo;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_grupos);

        rvGrupo = findViewById(R.id.rvgrupos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvGrupo.setLayoutManager(linearLayoutManager);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(Constantes.CHAT_GRUPAL);


        FirebaseRecyclerOptions<Usuario> options =
                new FirebaseRecyclerOptions.Builder<Usuario>()
                        .setQuery(query, Usuario.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Usuario, UsuarioViewHolder>(options) {
            @Override
            public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_grupo, parent, false);

                return new UsuarioViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UsuarioViewHolder holder, int position, Usuario model) {
                holder.getNombredegrupo().setText(model.getNombreGrupo());

                final LUsuario lUsuario = new LUsuario(getSnapshots().getSnapshot(position).getKey(),model);
                holder.getLayoutPrincipal().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Ver_grupos.this, MensajeriaActivity.class);
                        intent.putExtra("NGrupo",lUsuario.getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        rvGrupo.setAdapter(adapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
