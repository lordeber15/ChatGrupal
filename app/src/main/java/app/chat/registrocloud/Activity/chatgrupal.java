package app.chat.registrocloud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.chat.registrocloud.Entidades.Firebase.Usuario;
import app.chat.registrocloud.Persistencia.MensajeriaDAO;
import app.chat.registrocloud.Persistencia.UsuarioDAO;
import app.chat.registrocloud.R;
import app.chat.registrocloud.Utilidades.Constantes;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class chatgrupal extends AppCompatActivity {


    private Button CrearGrupo;
    private Button VerGrupo;
    private Button cierrasesion;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatgrupal);

        CrearGrupo = findViewById(R.id.CrearGrupo);
        VerGrupo = findViewById(R.id.VerGrupos);
        cierrasesion = findViewById(R.id.cierrasesion);
        database = FirebaseDatabase.getInstance();

        CrearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestNuevoGrupo();
            }
        });

        VerGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chatgrupal.this, Ver_grupos.class);
                startActivity(intent);
            }
        });

        cierrasesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                returnlogin();
            }
        });



    }
    private void RequestNuevoGrupo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(chatgrupal.this);
        builder.setTitle("Ingrese Nombre de Grupo: ");

        final EditText nombredegrupo = new EditText(chatgrupal.this);
        nombredegrupo.setHint("Nombre de Grupo");
        builder.setView(nombredegrupo);

        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombregrupos = nombredegrupo.getText().toString();
                if(TextUtils.isEmpty(nombregrupos)){
                    Toast.makeText(chatgrupal.this, "Escribe el nombre del Grupo...", Toast.LENGTH_SHORT).show();
                }else
                {
                    CrearnuevoGrupo(nombregrupos);
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();

    }
    private void CrearnuevoGrupo(final String nombre){


        Usuario usuario = new Usuario();
        usuario.setNombreGrupo(nombre);
        //databaseReference = database.getReference(Constantes.CHAT_GRUPAL +"/"+nombre);//nombre de los mensajes

        DatabaseReference reference = database.getReference(Constantes.CHAT_GRUPAL).child(nombre);//nombre de los mensajes
        reference.setValue(usuario);
        Intent intent = new Intent(chatgrupal.this,Ver_grupos.class);
        startActivity(intent);



    }

    private void returnlogin(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UsuarioDAO.getInstance().isUsuarioLogeado()){
            //el usuario esla logeado

        }else{
            returnlogin();
        }
    }
}
