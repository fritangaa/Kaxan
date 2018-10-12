package esteticaapp.co.kaxan.UM.Menu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import esteticaapp.co.kaxan.R;

public class calendario extends Fragment {

    public static calendario newInstance(){
        return new calendario();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_calendario, container, false);
    }

}
