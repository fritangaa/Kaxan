package esteticaapp.co.kaxan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class registroTarjeta extends AppCompatActivity {

    private EditText numeroTarjeta;
    private EditText mmaa;
    private EditText cvc;
    private ImageView visa,master,american;
    Button botonTarjeta;
    String valorVisa = "4";
    String valorMaster = "5";
    String valorAmerican = "6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tarjeta);

        numeroTarjeta=(EditText) findViewById(R.id.editNumerotarjeta);
        mmaa=(EditText) findViewById(R.id.editMMAA);
        cvc=(EditText) findViewById(R.id.editCVC);
        botonTarjeta=(Button) findViewById(R.id.botonNumerotarjeta);
        visa=(ImageView) findViewById(R.id.imageVisa);
        master=(ImageView) findViewById(R.id.imageMastercard);
        american=(ImageView) findViewById(R.id.imageAmerican);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.9),(int)(height*.35));//tamaño de la ventana
        //getWindow().setLayout((int)(width*.8),(int)(height*.4));//tamaño de la ventana

        botonTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent siguiente = new Intent(registroTarjeta.this, signupUA.class);//vamos a la ventana de la confirmacion
                startActivity(siguiente);
                finish();
            }
        });

        numeroTarjeta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==1){
                    if(numeroTarjeta.getText().toString().equals(valorVisa.toString())){
                        visa.setVisibility(View.VISIBLE);
                        master.setVisibility(View.INVISIBLE);
                        american.setVisibility(View.INVISIBLE);
                    } else if(numeroTarjeta.getText().toString().equals(valorMaster.toString())){
                        master.setVisibility(View.VISIBLE);
                        visa.setVisibility(View.INVISIBLE);
                        american.setVisibility(View.INVISIBLE);
                    } else if (numeroTarjeta.getText().toString().equals(valorAmerican.toString())){
                        master.setVisibility(View.INVISIBLE);
                        visa.setVisibility(View.INVISIBLE);
                        american.setVisibility(View.VISIBLE);
                    } else{
                        numeroTarjeta.setError( "No es una tarjeta valida" );
                    }
                }else if (s.length()<1){
                    master.setVisibility(View.VISIBLE);
                    visa.setVisibility(View.VISIBLE);
                    american.setVisibility(View.VISIBLE);
                }

                if(s.length()==16){
                    mmaa.setVisibility(View.VISIBLE);
                    cvc.setVisibility(View.VISIBLE);
                }
            }
        });

        mmaa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==2) {
                    String mmaaprevio;
                    mmaaprevio = (""+mmaa.getText());
                    mmaa.setText(mmaaprevio+"/");
                    mmaa.setSelection(3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cvc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==3){
                    botonTarjeta.setEnabled(true);
                }
            }
        });

        cvc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Mostrar mensaje
                    onBackPressed();

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
    public void onBackPressed() {
        super.onBackPressed();

    }
}
