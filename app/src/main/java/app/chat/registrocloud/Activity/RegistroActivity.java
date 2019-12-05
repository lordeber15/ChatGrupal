package app.chat.registrocloud.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import app.chat.registrocloud.Entidades.Firebase.Usuario;
import app.chat.registrocloud.Persistencia.UsuarioDAO;
import app.chat.registrocloud.Utilidades.Constantes;

import app.chat.registrocloud.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroActivity extends AppCompatActivity {

    //DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    private CircleImageView fotoPerfil;
    private Button registrarse;
    private EditText rusuario;
    private EditText rcorreo;
    private EditText rdireccion;
    private EditText rpassword;
    private RatingBar rvaloracion;
    private EditText txtfechanacimiento;
    private RadioButton rdHombre;
    private RadioButton rdMujer;

    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;

    private Uri fotoPerfilUri;

    private String pickerPath;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private long fechadeNacimiento;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registro);
        fotoPerfil =  findViewById(R.id.fotoPerfilregistro);
        registrarse = findViewById(R.id.Rregistrar);
        rusuario =  findViewById(R.id.Rusuario);
        rcorreo =  findViewById(R.id.RCorreo);
        rdireccion=  findViewById(R.id.Rdireccion);
        rpassword=  findViewById(R.id.RPassword);
        rvaloracion =  findViewById(R.id.valoracion);
        txtfechanacimiento =  findViewById(R.id.txtfnacimiento);
        rdHombre = findViewById(R.id.rdhombre);
        rdMujer = findViewById(R.id.rdmujer);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        imagePicker = new ImagePicker(this);
        cameraPicker = new CameraImagePicker(this);

        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);

        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){
                String path = list.get(0).getOriginalPath();
                fotoPerfilUri= Uri.parse(path);
                fotoPerfil.setImageURI(fotoPerfilUri);
                }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(RegistroActivity.this, "Error: "+ s, Toast.LENGTH_SHORT).show();
            }
        });

        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                String path = list.get(0).getOriginalPath();
                fotoPerfilUri= Uri.fromFile(new File(path));
                fotoPerfil.setImageURI(fotoPerfilUri);
            }

            @Override
            public void onError(String s) {
                Toast.makeText(RegistroActivity.this, "Error: "+ s, Toast.LENGTH_SHORT).show();
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
                AlertDialog.Builder dialog = new AlertDialog.Builder(RegistroActivity.this);
                dialog.setTitle("Foto de Perfil");

                String[] item = {"Galeria","Camara"};

                dialog.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //Galeria
                                imagePicker.pickImage();
                                break;
                            case 1:
                                //Camara
                                pickerPath = cameraPicker.pickImage();
                                break;
                        }
                    }
                });

                AlertDialog dialogConstruido = dialog.create();
                dialogConstruido.show();

            }
        });

        txtfechanacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int mes, int dia) {
                    Calendar calendarResultado = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,mes);
                    calendar.set(Calendar.DAY_OF_MONTH,dia);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date = calendarResultado.getTime();
                    String fechadeNacimientotexto = simpleDateFormat.format(date);
                    fechadeNacimiento = date.getTime();
                    txtfechanacimiento.setText(fechadeNacimientotexto);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usua = rusuario.getText().toString();
                final String correo = rcorreo.getText().toString();
                final String contraseña = rpassword.getText().toString();
                final String direc = rdireccion.getText().toString();
                final String val = String.valueOf(rvaloracion.getRating());
                if (isValidEmail(correo) && validarNombre(usua)){
                    mAuth.createUserWithEmailAndPassword(correo, contraseña)
                            .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        final String genero;
                                        if (rdHombre.isChecked()){
                                            genero = "Hombre";
                                        }else{
                                            genero = "Mujer";
                                        }

                                        if (fotoPerfilUri!=null){

                                            UsuarioDAO.getInstance().subirFotoUri(fotoPerfilUri, new UsuarioDAO.IDevolverUrlFoto() {
                                                @Override
                                                public void devolverUrlString(String url) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Toast.makeText(RegistroActivity.this, "Se Registro Correctamente", Toast.LENGTH_SHORT).show();
                                                    Usuario usuario = new Usuario();
                                                    usuario.setNombre(usua);
                                                    usuario.setCorreo(correo);
                                                    usuario.setDireccion(direc);
                                                    usuario.setValoracion(val);
                                                    usuario.setFechadenacimiento(fechadeNacimiento);
                                                    usuario.setGenero(genero);
                                                    usuario.setFotoPerfilURL(url);
                                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                                    DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
                                                    reference.setValue(usuario);
                                                    finish();
                                                }
                                            });

                                        }else {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(RegistroActivity.this, "Se Registro Correctamente", Toast.LENGTH_SHORT).show();
                                            Usuario usuario = new Usuario();
                                            usuario.setNombre(usua);
                                            usuario.setCorreo(correo);
                                            usuario.setDireccion(direc);
                                            usuario.setValoracion(val);
                                            usuario.setFechadenacimiento(fechadeNacimiento);
                                            usuario.setGenero(genero);
                                            usuario.setFotoPerfilURL(Constantes.URL_FOTO_FOR_DEFECTO_USUARIO);
                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
                                            reference.setValue(usuario);
                                            finish();
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegistroActivity.this, "Error al Registrarse", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
                else {
                    Toast.makeText(RegistroActivity.this, "Validacion de Correo Funcionando", Toast.LENGTH_SHORT).show();
                }

            }

        });


        Glide.with(this).load(Constantes.URL_FOTO_FOR_DEFECTO_USUARIO).into(fotoPerfil);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK){
            imagePicker.submit(data);
        }else if (requestCode == Picker.PICK_IMAGE_CAMERA && resultCode == RESULT_OK){
            cameraPicker.reinitialize(pickerPath);
            cameraPicker.submit(data);
        }
    }

    public boolean validarNombre(String nombre){
        return !nombre.isEmpty();
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }




}
