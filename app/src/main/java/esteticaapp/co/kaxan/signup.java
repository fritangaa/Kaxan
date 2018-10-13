package esteticaapp.co.kaxan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class signup extends AppCompatActivity {

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);


    private MaterialDialog dialog;

    private Button botonAgregarUsu;
    private Button botonAgregarEdad;

    private EditText textoRUsuario;
    private EditText textoRCorreo;
    private EditText textoREdad;
    private EditText textoRTelefono;
    private EditText textoRContrasena;

    private ImageView imagenRPerfil;

    private FirebaseAuth auth;
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        mStorage= FirebaseStorage.getInstance().getReference();

        imagenRPerfil = (ImageView) findViewById(R.id.imagenRPerfil);
        botonAgregarUsu = (Button) findViewById(R.id.botonRGuardar);
        botonAgregarEdad  = (Button) findViewById(R.id.botonREdad);

        textoRUsuario = (EditText) findViewById(R.id.textoRNombre);
        textoRContrasena = (EditText) findViewById(R.id.textoRContrasena);
        textoRCorreo = (EditText) findViewById(R.id.textoRCorreo);
        textoREdad = (EditText) findViewById(R.id.textoREdad);
        textoRTelefono = (EditText) findViewById(R.id.textoRTelefono);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Registrando")
                .content("Espere...")
                .cancelable(false)
                .progress(true, 0);
        dialog = builder.build();

        botonAgregarEdad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        botonAgregarUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });


        imagenRPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen de la galería"), 1);
                }
            }
        });


    }

    private void registrarUsuario(){

        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = textoRCorreo.getText().toString().trim();
        final String password  = textoRContrasena.getText().toString().trim();
        final String nombre = textoRUsuario.getText().toString();
        final String edad = textoREdad.getText().toString();
        final String telefono = textoRTelefono.getText().toString();

        //Verificamos que las cajas de texto no esten vacías
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Se debe ingresar un email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Falta ingresar la contraseña",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(nombre)){
            Toast.makeText(this,"Falta ingresar el nombre",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(telefono)){
            Toast.makeText(this,"Falta ingresar el telefono",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(edad)){
            Toast.makeText(this,"Falta ingresar la fecha de nacimiento",Toast.LENGTH_LONG).show();
            return;
        }


        dialog.show();
        //creating a new user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nombre)
                            .build();
                    FirebaseUser user = auth.getCurrentUser();
                    if (user!=null)
                        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(signup.this,"Se ha registrado el usuario: "+ textoRUsuario.getText(),Toast.LENGTH_LONG).show();
                                }else if (task.getException()!=null){
                                    Toast.makeText(signup.this,"No se pudo registrar el usuario ",Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }
                        });
                }else if (task.getException()!=null){
                    Toast.makeText(signup.this,"Error con el correo",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });


    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                textoREdad.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent siguiente = new Intent(signup.this, login.class);//vamos a la ventana de la confirmacion
        startActivity(siguiente);
        finish();
    }
}
