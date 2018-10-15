package esteticaapp.co.kaxan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class signupUMqr extends AppCompatActivity {

    private ImageView qrum;
    private ImageButton cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_umqr2);

        qrum = (ImageView) findViewById(R.id.UMQR);
        cancelar = (ImageButton) findViewById(R.id.btnCancelarqr);
        Intent intent = getIntent();

        new GetImageToURL().execute("https://zxing.org/w/chart?cht=qr&chs=700x700&chld=L&choe=UTF-8&chl="+intent.getStringExtra("telefono")+"%2C"+intent.getStringExtra("nombre")+"%2C"+intent.getStringExtra("edad")+"%2C"+intent.getStringExtra("contrasena")+"%2C"+intent.getStringExtra("correo")+"%2C"+intent.getStringExtra("codigo"));

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registrarUsuario();
                Intent siguiente = new Intent(signupUMqr.this, signupUM.class);//vamos a la ventana de la confirmacion
                startActivity(siguiente);
                finish();

            }
        });

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
            qrum.setImageBitmap(myBitMap);
        }
    }
}
