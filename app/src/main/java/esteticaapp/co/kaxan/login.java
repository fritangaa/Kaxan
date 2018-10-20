package esteticaapp.co.kaxan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import esteticaapp.co.kaxan.UA.leerQR;
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

    private LayoutInflater layoutInflater;
    private View popupView;
    private PopupWindow popupWindow;
    private ImageButton btn_um;
    private ImageButton btn_ua;

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

                layoutInflater =(LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.dialog_login, null);
                popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT);

                btn_ua= (ImageButton)popupView.findViewById(R.id.img_ua);
                btn_um= (ImageButton)popupView.findViewById(R.id.img_um);

                btn_ua.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Intent intencion = new Intent(getApplication(), signup.class);
                        startActivity(intencion);
                        finish();

                    }});
                btn_um.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Intent intencion = new Intent(getApplication(), signupUM.class);
                        startActivity(intencion);
                        finish();
                    }});

                popupWindow.showAtLocation(textoUsuario, Gravity.CENTER,0, 0);


            }
        });

        botonOlvidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                olvidaContra();
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
        Intent intencion = new Intent(getApplication(), leerQR.class);
        startActivity(intencion);
    }

    private void recuperaContra(){
        String emailRecupera = textoUsuario.getText().toString().trim();

    }



}
