package app.chat.registrocloud.Persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import app.chat.registrocloud.Entidades.Firebase.Mensajes;
import app.chat.registrocloud.Entidades.Firebase.Usuario;
import app.chat.registrocloud.Utilidades.Constantes;

public class MensajeriaDAO {

    private static MensajeriaDAO mensajeriaDAO;

    private FirebaseDatabase database;
    private DatabaseReference databaseMensajeria;


    public static MensajeriaDAO getInstance(){
        if(mensajeriaDAO==null) mensajeriaDAO =  new MensajeriaDAO();
        return mensajeriaDAO;
    }

    private MensajeriaDAO(){

        database = FirebaseDatabase.getInstance();
        databaseMensajeria = database.getReference(Constantes.CHAT_GRUPAL);
        //storage= FirebaseStorage.getInstance();
        //referenceUsuario = database.getReference(Constantes.MODO_USUARIO);
        //referenceFotodePerfil = storage.getReference("Fotos/FotoPerfil/"+getKeyUsuario());
    }

    public void nuuevoMensaje(String keyEmisor, Mensajes mensajes){

        DatabaseReference referenceEmisor = databaseMensajeria.child(keyEmisor);
        referenceEmisor.push().setValue(mensajes);
    }

}
