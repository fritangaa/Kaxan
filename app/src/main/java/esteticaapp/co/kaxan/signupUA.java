package esteticaapp.co.kaxan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class signupUA extends AppCompatActivity{

    private Button comprar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_ua);

        comprar = findViewById(R.id.boton1);

        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent siguiente = new Intent(signupUA.this, registroTarjeta.class);//vamos a la ventana de la confirmacion
                startActivity(siguiente);
                finish();
            }
        });

    }

}
