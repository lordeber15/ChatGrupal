package app.chat.registrocloud.Persistencia;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import app.chat.registrocloud.Entidades.Firebase.Usuario;
import app.chat.registrocloud.Entidades.Logica.LUsuario;
import app.chat.registrocloud.Utilidades.Constantes;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UsuarioDAO {


    public interface IDevolverUsuario{
        public void devolverUsuario(LUsuario lUsuario);
        public void devolverError(String error);
    }

    public interface IDevolverUrlFoto{
        public void devolverUrlString(String url);
    }
    private static UsuarioDAO usuarioDAO;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference referenceUsuario;
    private StorageReference referenceFotodePerfil;

    public static UsuarioDAO getInstance(){
        if(usuarioDAO==null) usuarioDAO = new UsuarioDAO();
        return usuarioDAO;
    }
    private UsuarioDAO(){
        database = FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();
        referenceUsuario = database.getReference(Constantes.MODO_USUARIO);
        referenceFotodePerfil = storage.getReference("Fotos/FotoPerfil/"+getKeyUsuario());
    }

    public boolean isUsuarioLogeado (){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser!=null;
    }

    public String getKeyUsuario(){
        return FirebaseAuth.getInstance().getUid();
    }
    public long fechadeCreacionLong(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
    }
    public long obtenerFechadeUltimavezqueselogeoLog(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }

    public void obtenerInformaciondeUsuarioPorLLave(final String key, final IDevolverUsuario iDevolverUsuario){
        referenceUsuario.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                LUsuario lUsuario = new LUsuario(key,usuario);
                iDevolverUsuario.devolverUsuario(lUsuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iDevolverUsuario.devolverError(databaseError.getMessage());

            }
        });
    }


    public void añadirfotodeperfilalosususarioquenotienefoto(){
        referenceUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<LUsuario> lUsuariosLista = new ArrayList<>();
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                    Usuario usuario = childDataSnapshot.getValue(Usuario.class);
                    LUsuario lUsuario = new LUsuario(childDataSnapshot.getKey(),usuario);
                    lUsuariosLista.add(lUsuario);
                }

                for (LUsuario lUsuario : lUsuariosLista){
                    if (lUsuario.getUsuario().getFotoPerfilURL()==null){
                        referenceUsuario.child(lUsuario.getKey()).child("fotoPerfilURL").setValue(Constantes.URL_FOTO_FOR_DEFECTO_USUARIO);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void subirFotoUri(Uri uri, final IDevolverUrlFoto iDevolverUrlFoto ){
        String nombrefoto= "";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("SSS.ss-mm-hh-dd-MM-yyyy", Locale.getDefault());
        nombrefoto = simpleDateFormat.format(date);
        final StorageReference fotoReferencia = referenceFotodePerfil.child(nombrefoto);

        fotoReferencia.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    iDevolverUrlFoto.devolverUrlString(uri.toString());
                }
            }
        });

    }

}
