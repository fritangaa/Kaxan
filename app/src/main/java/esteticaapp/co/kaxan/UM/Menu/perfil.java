package esteticaapp.co.kaxan.UM.Menu;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import esteticaapp.co.kaxan.R;

public class perfil extends Fragment {




    public static perfil newInstance(){
        return new perfil();
    }

    View view;
    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    private ImageView imagenPerfil;
    private EditText nombrePerfil;
    private EditText edadPerfil;
    //private EditText contraPerfil;
    private Button guardarPerfil;

    public static TextView mTextView;
    private StorageReference mStorage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_perfil, container, false);

        //inicializamos el objeto firebaseAuth

        mStorage= FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2").child("um").child(firebaseAuth.getUid()).child("datos");


        imagenPerfil = (ImageView) view.findViewById(R.id.imgPUM);
        nombrePerfil = (EditText) view.findViewById(R.id.nombrePUM);
        edadPerfil = (EditText) view.findViewById(R.id.edadPUM);
        mTextView = (TextView) view.findViewById(R.id.contraPUM);
        guardarPerfil = (Button) view.findViewById(R.id.botonPUM);

        mStorage.child("um/imagenes/perfil/"+firebaseAuth.getUid()+"_perfil.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                new perfil.GetImageToURL().execute(""+uri);
            }
        });


        return view;
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
            imagenPerfil.setImageBitmap(myBitMap);
        }
    }

}
