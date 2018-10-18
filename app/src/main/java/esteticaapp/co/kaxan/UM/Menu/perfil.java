package esteticaapp.co.kaxan.UM.Menu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import esteticaapp.co.kaxan.R;
import esteticaapp.co.kaxan.UA.objUM;

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
    private EditText contraPerfil;
    private Button guardarPerfil;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_perfil, container, false);

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("miTv2AU2Y2UUKDeYXiooRc71Dhi2").child("um").child(firebaseAuth.getUid()).child("datos");


        imagenPerfil = (ImageView) view.findViewById(R.id.imgPUM);
        nombrePerfil = (EditText) view.findViewById(R.id.nombrePUM);
        edadPerfil = (EditText) view.findViewById(R.id.edadPUM);
        contraPerfil = (EditText) view.findViewById(R.id.contraPUM);
        guardarPerfil = (Button) view.findViewById(R.id.botonPUM);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nombre = (String) dataSnapshot.getValue().toString();
                if (nombre.equals(dataSnapshot.child("nombre"))) {
                    //Haz algo
                    nombrePerfil.setText(nombre);
                    //Haz otra cosa
                }else {
                    if (nombre.equals("")){
                    }else {
                        nombrePerfil.setText("");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        guardarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objUM nuevoUM =new objUM(nombrePerfil.getText().toString(),"",contraPerfil.getText().toString(),edadPerfil.getText().toString(),"");

                mDatabase.setValue(nuevoUM);
            }
        });

        return view;
    }
}
