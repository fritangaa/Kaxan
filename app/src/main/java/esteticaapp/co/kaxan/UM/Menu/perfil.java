package esteticaapp.co.kaxan.UM.Menu;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
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

    private int PICK_IMAGE_REQUEST = 1;


    private CircularImageView imagenPerfil;
    private EditText nombrePerfil;
    private EditText edadPerfil;
    //private EditText contraPerfil;
    private Button guardarPerfil;

    public static TextView mTextView;
    private StorageReference mStorage;

    private Uri uri;

    // a static variable to get a reference of our application context
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_perfil, container, false);

        contextOfApplication = view.getContext();

        //inicializamos el objeto firebaseAuth

        mStorage= FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2").child("um").child(firebaseAuth.getUid()).child("datos");


        imagenPerfil = (CircularImageView) view.findViewById(R.id.imgPUM);
        nombrePerfil = (EditText) view.findViewById(R.id.nombrePUM);
        edadPerfil = (EditText) view.findViewById(R.id.edadPUM);
        mTextView = (TextView) view.findViewById(R.id.contraPUM);
        guardarPerfil = (Button) view.findViewById(R.id.botonPUM);

        imagenPerfil.setOnClickListener(new View.OnClickListener() {
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

        mStorage.child("um/imagenes/perfil/"+firebaseAuth.getUid()+"_perfil.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'photos/profile.png'
                new perfil.GetImageToURL().execute(""+uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                new perfil.GetImageToURL().execute("https://firebasestorage.googleapis.com/v0/b/kaxan-um.appspot.com/o/um%2Fimagenes%2Fperfil%2Fic_perfil_defecto.png?alt=media&token=8d457d20-5bb2-47ef-af19-cae27dd55e72");
            }
        });

        guardarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirImagen(uri,firebaseAuth.getUid());
                Toast.makeText(view.getContext(),"Axtualizado con exito",Toast.LENGTH_LONG);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Context applicationContext = perfil.getContextOfApplication();

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), uri);

                imagenPerfil.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void subirImagen(Uri uriPerf, String id){

        Context applicationContext = perfil.getContextOfApplication();

        Uri uri = uriPerf;
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), uri);
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
                Toast.makeText(view.getContext(),"Hubo un error",Toast.LENGTH_LONG);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(view.getContext(),"Subida con exito",Toast.LENGTH_LONG);

            }
        });
    }


}
