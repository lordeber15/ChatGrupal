package app.chat.registrocloud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.chat.registrocloud.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button registro,entrar;
    EditText correo,pass;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registro = (Button)findViewById(R.id.registrar);
        entrar = (Button)findViewById(R.id.entrar);
        correo = (EditText)findViewById(R.id.lCorreo);
        pass = (EditText)findViewById(R.id.lpass);

        mAuth = FirebaseAuth.getInstance();

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Registro = new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(Registro);

            }
        });

        //UsuarioDAO.getInstance().añadirfotodeperfilalosususarioquenotienefoto();
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String lcorreo = correo.getText().toString();
                final String lpassw = pass.getText().toString();

                login(lcorreo,lpassw);

            }
        });


    }
    private void login(final String lcorreo,final String lpassw){
        mAuth.signInWithEmailAndPassword(lcorreo, lpassw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Bienvenido al Sistema", Toast.LENGTH_SHORT).show();
                            nextActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Error al Logearte", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Toast.makeText(this, "Usuario Logeado", Toast.LENGTH_SHORT).show();
            nextActivity();
        }
    }
    private void nextActivity(){
       /*Intent sistema = new Intent(LoginActivity.this, MensajeriaActivity.class);
        startActivity(sistema);*/

        Intent sistema = new Intent(LoginActivity.this, chatgrupal.class);
        startActivity(sistema);
          finish();
    }
}
