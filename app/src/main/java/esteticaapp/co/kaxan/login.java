package esteticaapp.co.kaxan;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
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

import esteticaapp.co.kaxan.UA.UAPrincipalActivity;
import esteticaapp.co.kaxan.UA.leerQR;
import esteticaapp.co.kaxan.UM.menu;

public class login extends AppCompatActivity {

    private MaterialDialog dialog;

    private Button botonEntrar;
    private Button botonRegistrar;
    private Button botonOlvidar;
    private Button boronRecupera;
    private TextView textoRecupera;

    private CheckBox botonRecuerda;
    private boolean isActivateRecuerda;

    private EditText textoUsuario;
    private EditText textoContrasena;

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;

    private LayoutInflater layoutInflater;
    private View popupView;
    private PopupWindow popupWindow;
    private ImageButton btn_um;
    private ImageButton btn_ua;

    private String usuarioAdmin = "UA";
    private String usuarioMonitor = "UM";

    private static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 1;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.BODY_SENSORS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Preferences.obtenerPreferenceBoolean(this,Preferences.PREFERENCE_ESTADO_BUTTON_SESION)){
            if(Preferences.obtenerPreferenceString(this,Preferences.PREFERENCE_USUARIO_LB).equals(usuarioAdmin)){
                Intent intencion = new Intent(getApplication(), UAPrincipalActivity.class);
                startActivity(intencion);
                finish();
            }else if(Preferences.obtenerPreferenceString(this,Preferences.PREFERENCE_USUARIO_LB).equals(usuarioMonitor)){
                Intent intencion = new Intent(getApplication(), menu.class);
                startActivity(intencion);
                finish();
            }
        }


        if (ActivityCompat.checkSelfPermission(login.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(login.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            //Si alguno de los permisos no esta concedido lo solicita
            ActivityCompat.requestPermissions(login.this, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
        } else {
            //Si todos los permisos estan concedidos prosigue con el flujo normal
            permissionGranted();
        }



        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();


        //Referenciamos los views
        textoContrasena = findViewById(R.id.textoContrasena);
        textoUsuario = findViewById(R.id.textoUsuario);

        //Referenciamos los botones
        botonEntrar = findViewById(R.id.botonEntrar);
        botonRegistrar = findViewById(R.id.botonRegistrar);
        botonOlvidar = findViewById(R.id.botonOlvidar);

        botonRecuerda = findViewById(R.id.btnRecuerda);

        botonRecuerda.setOnClickListener(new View.OnClickListener() {
            //ACTIVADO
            @Override
            public void onClick(View v) {
                if(botonRecuerda.isChecked()){
                    isActivateRecuerda = true;
                }else{
                    isActivateRecuerda = false;
                }
            }
        });



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
                irAUsuarioMonPruebas();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Se obtiene el resultado de los permisos con base en la clave
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS_REQUEST_CODE:
                //Verifica si todos los permisos se aceptaron o no
                if (validatePermissions(grantResults)) {
                    //Si todos los permisos fueron aceptados continua con el flujo normal
                    permissionGranted();
                } else {
                    //Si algun permiso fue rechazado no se puede continuar
                    permissionRejected();
                }
                break;
        }
    }

    private boolean validatePermissions(int[] grantResults) {
        boolean allGranted = false;
        //Revisa cada uno de los permisos y si estos fueron aceptados o no
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                //Si todos los permisos fueron aceptados retorna true
                allGranted = true;
            } else {
                //Si algun permiso no fue aceptado retorna false
                allGranted = false;
                break;
            }
        }
        return allGranted;
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
                            Preferences.savePreferenceBoolean(login.this,botonRecuerda.isChecked(),Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                            Preferences.savePreferenceString(login.this,usuarioAdmin,Preferences.PREFERENCE_USUARIO_LB);
                            Toast.makeText(login.this, "Bienvenido: " + textoUsuario.getText(), Toast.LENGTH_LONG).show();
                            Intent intencion = new Intent(getApplication(), UAPrincipalActivity.class);
                            startActivity(intencion);
                            finish();

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

    private void irAUsuarioMonPruebas(){
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
                            finish();

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

    private void permissionGranted() {
        Toast.makeText(login.this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
    }

    private void permissionRejected() {
        Toast.makeText(login.this, getString(R.string.permission_rejected), Toast.LENGTH_SHORT).show();
    }



}
