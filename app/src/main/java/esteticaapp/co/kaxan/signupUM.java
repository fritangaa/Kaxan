 package esteticaapp.co.kaxan;

 import android.app.DatePickerDialog;
 import android.content.Intent;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.net.Uri;
 import android.os.AsyncTask;
 import android.os.Bundle;
 import android.provider.MediaStore;
 import android.support.annotation.NonNull;
 import android.support.v7.app.AppCompatActivity;
 import android.text.TextUtils;
 import android.view.KeyEvent;
 import android.view.View;
 import android.view.inputmethod.EditorInfo;
 import android.view.inputmethod.InputMethodManager;
 import android.widget.Button;
 import android.widget.DatePicker;
 import android.widget.EditText;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.afollestad.materialdialogs.MaterialDialog;
 import com.google.android.gms.tasks.OnCompleteListener;
 import com.google.android.gms.tasks.OnFailureListener;
 import com.google.android.gms.tasks.OnSuccessListener;
 import com.google.android.gms.tasks.Task;
 import com.google.firebase.auth.AuthResult;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;
 import com.google.firebase.auth.UserProfileChangeRequest;
 import com.google.firebase.storage.FirebaseStorage;
 import com.google.firebase.storage.StorageReference;
 import com.google.firebase.storage.UploadTask;
 import com.mikhaellopez.circularimageview.CircularImageView;

 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.util.Calendar;

 public class signupUM extends AppCompatActivity {

     private static final String CERO = "0";
     private static final String BARRA = "/";
     private int PICK_IMAGE_REQUEST = 1;

     //Calendario para obtener fecha & hora
     public final Calendar c = Calendar.getInstance();

     //Variables para obtener la fecha
     final int mes = c.get(Calendar.MONTH);
     final int dia = c.get(Calendar.DAY_OF_MONTH);
     final int anio = c.get(Calendar.YEAR);
     private int anioSeleccion;
     private int diaSeleccionado;
     private int mesSeleccionado;

     private MaterialDialog dialog;

     private Button botonAgregarUsuUM;
     private Button botonAgregarEdadUM;

     private EditText textoRUsuarioUM;
     private EditText textoREdadUM;
     private EditText textoRTelefonoUM;
     private EditText textoRContrasenaUM;
     private EditText textoRCorreoUM;

     private CircularImageView imagenRPerfilUM;

     private FirebaseAuth auth;
     private StorageReference mStorage;

     private Uri uri;
     private String imagenurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_um);

        new signupUM.GetImageToURL().execute("https://firebasestorage.googleapis.com/v0/b/kaxan-um.appspot.com/o/um%2Fimagenes%2Fperfil%2Fic_perfil_defecto.png?alt=media&token=8d457d20-5bb2-47ef-af19-cae27dd55e72");

        auth = FirebaseAuth.getInstance();
        mStorage= FirebaseStorage.getInstance().getReference();

        imagenRPerfilUM = (CircularImageView) findViewById(R.id.imagenUMRPerfil);
        botonAgregarUsuUM = (Button) findViewById(R.id.botonUMRGuardar);
        botonAgregarEdadUM  = (Button) findViewById(R.id.botonUMREdad);

        textoRUsuarioUM = (EditText) findViewById(R.id.textoUMRNombre);
        textoRContrasenaUM = (EditText) findViewById(R.id.textoUMRContrasena);
        textoREdadUM = (EditText) findViewById(R.id.textoUMREdad);
        textoRTelefonoUM = (EditText) findViewById(R.id.textoUMRTelefono);
        textoRCorreoUM = (EditText) findViewById(R.id.textoUMRCorreo);

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
                validaUsuario();

            }
        });


        imagenRPerfilUM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        textoRContrasenaUM.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Mostrar mensaje
                    validaUsuario();
                    // Ocultar teclado virtual
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    procesado = true;
                }
                return procesado;
            }
        });
    }

     private void validaUsuario(){

         //Obtenemos el email y la contraseña desde las cajas de texto
         final String password  = textoRContrasenaUM.getText().toString().trim();
         final String nombre = textoRUsuarioUM.getText().toString();
         final String edad = textoREdadUM.getText().toString();
         final String telefono = textoRTelefonoUM.getText().toString();
         final String correo = textoRCorreoUM.getText().toString();

         //Verificamos que las cajas de texto no esten vacías


         if(TextUtils.isEmpty(password)){
             Toast.makeText(this,"Falta ingresar la contraseña",Toast.LENGTH_SHORT).show();
             return;
         }

         if(TextUtils.isEmpty(nombre)){
             Toast.makeText(this,"Falta ingresar el nombre",Toast.LENGTH_SHORT).show();
             return;
         }

         if(TextUtils.isEmpty(telefono)){
             Toast.makeText(this,"Falta ingresar el telefono",Toast.LENGTH_SHORT).show();
             return;
         }else if(telefono.length()<10){
             Toast.makeText(this,"El telefono no esta completo",Toast.LENGTH_SHORT).show();
             return;
         }

         if(TextUtils.isEmpty(edad)){
             Toast.makeText(this,"Falta ingresar la fecha de nacimiento",Toast.LENGTH_SHORT).show();
             return;
         } else if(anioSeleccion>anio){
             Toast.makeText(this,"La fecha ingresada no es valida",Toast.LENGTH_SHORT).show();
             return;
         }
         if (diaSeleccionado>dia&&mesSeleccionado==mes+1&&anioSeleccion==anio){
             Toast.makeText(this,"La fecha ingresada no es valida",Toast.LENGTH_SHORT).show();
             return;
         }


         if(TextUtils.isEmpty(correo)){
             Toast.makeText(this,"Falta ingresar el correo",Toast.LENGTH_SHORT).show();
             return;
         }

         dialog.show();
         //creating a new user
         auth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

                                     Intent siguiente = new Intent(signupUM.this, signupUMqr.class);//vamos a la ventana de la confirmacion
                                     siguiente.putExtra("telefono",textoRTelefonoUM.getText().toString());
                                     siguiente.putExtra("nombre",textoRUsuarioUM.getText().toString());
                                     siguiente.putExtra("edad",textoREdadUM.getText().toString());
                                     siguiente.putExtra("contrasena",textoRContrasenaUM.getText().toString());
                                     siguiente.putExtra("correo",textoRCorreoUM.getText().toString());
                                     siguiente.putExtra("codigo",auth.getUid());

                                     subirImagen(uri, auth.getUid());

                                     dialog.dismiss();
                                     startActivity(siguiente);
                                     finish();

                                 }else if (task.getException()!=null){
                                     Toast.makeText(signupUM.this,"No se pudo registrar el usuario ",Toast.LENGTH_LONG).show();
                                 }
                                 dialog.dismiss();
                             }
                         });
                 }else if (task.getException()!=null){
                     Toast.makeText(signupUM.this,"Error en correo o contraseña",Toast.LENGTH_LONG).show();
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
                 textoREdadUM.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                 anioSeleccion = year;
                 mesSeleccionado = Integer.parseInt(mesFormateado);
                 diaSeleccionado = Integer.parseInt(diaFormateado);

             }
             //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
             /**
              *También puede cargar los valores que usted desee
              */
         },anio, mes, dia);
         //Muestro el widget
         recogerFecha.show();

     }

     private class GetImageToURL extends AsyncTask< String, Void, Bitmap > {

         @Override
         protected Bitmap doInBackground(String...params) {
             try {
                 URL url = new URL(params[0]);
                 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                 connection.setDoInput(true);
                 connection.connect();
                 InputStream input = connection.getInputStream();
                 Bitmap myBitmap = BitmapFactory.decodeStream(input);
                 return myBitmap;
             } catch (IOException e) {
                 // Log exception
                 return null;
             }
         }

         @Override
         protected void onPostExecute(Bitmap myBitMap) {
             imagenRPerfilUM.setImageBitmap(myBitMap);
         }
     }


     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
             uri = data.getData();
             try {
                 Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                 imagenRPerfilUM.setImageBitmap(bitmap);
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

     }

     public void subirImagen(Uri uriPerf, String id){
         Uri uri = uriPerf;
         Bitmap bitmap = null;
         try {
             bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
         } catch (IOException e) {
             e.printStackTrace();
         }
         // Creamos una referencia a la carpeta y el nombre de la imagen donde se guardara
         StorageReference mountainImagesRef = mStorage.child("um/imagenes/perfil/"+id+"_perfil.jpeg");
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
         byte[] datas = baos.toByteArray();

         // Empezamos con la subida a Firebase
         UploadTask uploadTask = mountainImagesRef.putBytes(datas);
         uploadTask.addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception exception) {
                 Toast.makeText(getBaseContext(),"Hubo un error",Toast.LENGTH_LONG);
             }
         }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 Toast.makeText(getBaseContext(),"Subida con exito",Toast.LENGTH_LONG);

             }
         });
     }


     @Override
     public void onBackPressed() {
         super.onBackPressed();
         Intent siguiente = new Intent(signupUM.this, login.class);//vamos a la ventana de la confirmacion
         startActivity(siguiente);
         finish();
     }

 }
