package esteticaapp.co.kaxan.UM.Menu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import esteticaapp.co.kaxan.R;

public class perfil extends Fragment {

    public static perfil newInstance(){
        return new perfil();
    }

    View view;
    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_perfil, container, false);

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();



        return view;
    }
}
