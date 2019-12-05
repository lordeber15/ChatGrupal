package app.chat.registrocloud.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import app.chat.registrocloud.Adaptadores.MensajeriaAdaptador;
import app.chat.registrocloud.Entidades.Firebase.Mensajes;
import app.chat.registrocloud.Entidades.Firebase.Usuario;
import app.chat.registrocloud.Entidades.Logica.LMensaje;
import app.chat.registrocloud.Entidades.Logica.LUsuario;
import app.chat.registrocloud.Persistencia.MensajeriaDAO;
import app.chat.registrocloud.Persistencia.UsuarioDAO;

import app.chat.registrocloud.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import app.chat.registrocloud.Utilidades.Constantes;
import de.hdodenhof.circleimageview.CircleImageView;

public class MensajeriaActivity extends AppCompatActivity {
    private CircleImageView fotoperfil;
    private TextView nombrechat;
    private RecyclerView rvmensajes;
    private EditText txtmensaje;
    private Button btnenviar;
    private ImageButton btnEnviarfoto;
    private static final int PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL=2;
    private String fotoperfilcadena;

    private MensajeriaAdaptador adapter;


    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private String NOMBRE_USUARIO;

    private String NomGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            NomGrupo= bundle.getString("NGrupo");
        }else
        {
            finish();
        }

        fotoperfil = (CircleImageView) findViewById(R.id.fotoperfil);
        nombrechat = (TextView) findViewById(R.id.nombrechat);
        rvmensajes = (RecyclerView) findViewById(R.id.Rvmensajes);
        txtmensaje = (EditText) findViewById(R.id.txtMensaje);
        btnenviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviarfoto = (ImageButton) findViewById(R.id.Enviarfoto);



        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        adapter = new MensajeriaAdaptador(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvmensajes.setLayoutManager(l);
        rvmensajes.setAdapter(adapter);
        fotoperfilcadena = "";




        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensajeEnviar = txtmensaje.getText().toString();
                if(!mensajeEnviar.isEmpty())
                {

                    Mensajes mensajes = new Mensajes();
                    mensajes.setMensaje(mensajeEnviar);
                    mensajes.setContieneFoto(false);
                    mensajes.setKeyEmisor(UsuarioDAO.getInstance().getKeyUsuario());
                    MensajeriaDAO.getInstance().nuuevoMensaje(NomGrupo,mensajes);
                    txtmensaje.setText("");
                }

            }
        });


        btnEnviarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_SEND);
            }
        });

        fotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_PERFIL);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        FirebaseDatabase.getInstance().
                getReference(Constantes.CHAT_GRUPAL).child(NomGrupo).
                child(UsuarioDAO.getInstance().getKeyUsuario())
                .addChildEventListener(new ChildEventListener() {

            //traer info de usuario
            //guardar info en lista temporal
            //obtener ls info guardade de la llave
            Map<String, LUsuario> mapUsuariosTemporales = new HashMap<>();


            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            final Mensajes mensaje = dataSnapshot.getValue(Mensajes.class);

            final LMensaje lMensaje = new LMensaje(dataSnapshot.getKey(),mensaje);
            //Log.d("info:",dataSnapshot.getKey());
            final int posicion = adapter.addMensaje(lMensaje);
                if (mapUsuariosTemporales.get(mensaje.getKeyEmisor())!=null){
                    lMensaje.setlUsuario(mapUsuariosTemporales.get(mensaje.getKeyEmisor()));

                    adapter.actualizarMensaje(posicion,lMensaje);
                }else
                {
                    UsuarioDAO.getInstance().obtenerInformaciondeUsuarioPorLLave(mensaje.getKeyEmisor(), new UsuarioDAO.IDevolverUsuario() {
                        @Override
                        public void devolverUsuario(LUsuario lUsuario) {
                            mapUsuariosTemporales.put(mensaje.getKeyEmisor(),lUsuario);
                            lMensaje.setlUsuario(lUsuario);

                            adapter.actualizarMensaje(posicion,lMensaje);
                        }

                        @Override
                        public void devolverError(String error) {
                            Toast.makeText(MensajeriaActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        verifyStoragePermissions(this);
    }
    private void setScrollbar(){
        rvmensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    public static boolean verifyStoragePermissions(Activity activity) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());

            fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        Mensajes mensajes = new Mensajes();
                        mensajes.setMensaje("El usuario te a enviado una foto");
                        mensajes.setUrlFoto(uri.toString());
                        mensajes.setContieneFoto(true);
                        mensajes.setKeyEmisor(UsuarioDAO.getInstance().getKeyUsuario());
                        MensajeriaDAO.getInstance().nuuevoMensaje(NomGrupo,mensajes);


                    }
                }
            });
        }
        /*else if(requestCode == PHOTO_PERFIL && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("foto de perfil");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());

            fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        fotoperfilcadena = uri.toString();
                        MensajesEnviar m = new MensajesEnviar(NOMBRE_USUARIO+" a actualizado su foto de perfil",uri.toString(),NOMBRE_USUARIO,fotoperfilcadena,"2",ServerValue.TIMESTAMP);
                        databaseReference.push().setValue(m);
                        Glide.with(MensajeriaActivity.this).load(uri.toString()).into(fotoperfil);
                    }
                }
            });
        }*/

    }


}
