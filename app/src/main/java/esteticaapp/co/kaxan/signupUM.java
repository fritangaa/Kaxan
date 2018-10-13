 package esteticaapp.co.kaxan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

 public class signupUM extends AppCompatActivity {

     private static final String CERO = "0";
     private static final String BARRA = "/";

     //Calendario para obtener fecha & hora
     public final Calendar c = Calendar.getInstance();

     //Variables para obtener la fecha
     final int mes = c.get(Calendar.MONTH);
     final int dia = c.get(Calendar.DAY_OF_MONTH);
     final int anio = c.get(Calendar.YEAR);


     private MaterialDialog dialog;

     private Button botonAgregarUsuUM;
     private Button botonAgregarEdadUM;

     private EditText textoRUsuarioUM;
     private EditText textoREdadUM;
     private EditText textoRTelefonoUM;
     private EditText textoRContrasenaUM;

     private ImageView imagenRPerfilUM;

     private FirebaseAuth auth;
     private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_um);

        auth = FirebaseAuth.getInstance();
        mStorage= FirebaseStorage.getInstance().getReference();

        imagenRPerfilUM = (ImageView) findViewById(R.id.imagenUMRPerfil);
        botonAgregarUsuUM = (Button) findViewById(R.id.botonUMRGuardar);
        botonAgregarEdadUM  = (Button) findViewById(R.id.botonUMREdad);

        textoRUsuarioUM = (EditText) findViewById(R.id.textoUMRNombre);
        textoRContrasenaUM = (EditText) findViewById(R.id.textoUMRContrasena);
        textoREdadUM = (EditText) findViewById(R.id.textoUMREdad);
        textoRTelefonoUM = (EditText) findViewById(R.id.textoUMRTelefono);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Registrando")
                .content("Espere...")
                .cancelable(false)
                .progress(true, 0);
        dialog = builder.build();

        botonAgregarEdadUM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        botonAgregarUsuUM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });


        imagenRPerfilUM.setOnClickListener(new View.OnClickListener() {
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
         final String password  = textoRContrasenaUM.getText().toString().trim();
         final String nombre = textoRUsuarioUM.getText().toString();
         final String edad = textoREdadUM.getText().toString();
         final String telefono = textoRTelefonoUM.getText().toString();

         //Verificamos que las cajas de texto no esten vacías


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
                 textoREdadUM.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


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
         Intent siguiente = new Intent(signupUM.this, login.class);//vamos a la ventana de la confirmacion
         startActivity(siguiente);
         finish();
     }

 }
