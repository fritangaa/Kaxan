package esteticaapp.co.kaxan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import esteticaapp.co.kaxan.UM.menu;

public class login extends AppCompatActivity {

    private MaterialDialog dialog;

    private Button botonEntrar;
    private Button botonRegistrar;
    private Button botonOlvidar;
    private Button boronRecupera;
    private TextView textoRecupera;

    private EditText textoUsuario;
    private EditText textoContrasena;

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();


        //Referenciamos los views
        textoContrasena = findViewById(R.id.textoContrasena);
        textoUsuario = findViewById(R.id.textoUsuario);

        //Referenciamos los botones
        botonEntrar = findViewById(R.id.botonEntrar);
        botonRegistrar = findViewById(R.id.botonRegistrar);
        botonOlvidar = findViewById(R.id.botonOlvidar);
        boronRecupera = findViewById(R.id.botonRecupera);
        textoRecupera = findViewById(R.id.textoRecupera);


        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Iniciando Sesión")
                .content("Espere...")
                .cancelable(false)
                .progress(true, 0);
        dialog = builder.build();


        botonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loguearUsuario();
            }
        });

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });

        botonOlvidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                olvidaContra();
            }
        });

        botonOlvidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperaContra();
            }
        });

        textoContrasena.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Mostrar mensaje
                    loguearUsuario();
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

    private void registrarUsuario(){

        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = textoUsuario.getText().toString().trim();
        String password  = textoContrasena.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Se debe ingresar un email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Falta ingresar la contraseña",Toast.LENGTH_LONG).show();
            return;
        }


        dialog.show();
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){

                            Toast.makeText(login.this,"Se ha registrado el usuario con el email: "+ textoUsuario.getText(),Toast.LENGTH_LONG).show();
                        }else{

                            Toast.makeText(login.this,"No se pudo registrar el usuario ",Toast.LENGTH_LONG).show();
                        }
                        //progressDialog.dismiss();
                        dialog.dismiss();
                    }
                });



    }

    private void loguearUsuario() {
        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = textoUsuario.getText().toString().trim();
        String password = textoContrasena.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {//(precio.equals(""))
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }




        dialog.show();

        //loguear usuario
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            int pos = email.indexOf("@");
                            String user = email.substring(0, pos);
                            Toast.makeText(login.this, "Bienvenido: " + textoUsuario.getText(), Toast.LENGTH_LONG).show();
                            Intent intencion = new Intent(getApplication(), menu.class);
                            startActivity(intencion);


                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(login.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(login.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }
                        }
                        dialog.dismiss();
                    }
                });


    }

    private void olvidaContra(){
        textoContrasena.setVisibility(View.INVISIBLE);
        textoRecupera.setVisibility(View.VISIBLE);
        botonRegistrar.setVisibility(View.INVISIBLE);
        botonEntrar.setVisibility(View.INVISIBLE);
    }

    private void recuperaContra(){
        String emailRecupera = textoUsuario.getText().toString().trim();

    }

}
