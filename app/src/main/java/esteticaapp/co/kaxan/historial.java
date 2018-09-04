package esteticaapp.co.kaxan;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class historial extends Fragment {

    public static historial newInstance(){
        return new historial();
    }

    private ListView lista_historial = null;
    private ArrayList<itemHistorial> arrayItem = null;
    private itemHistorialAdaptador adapter = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.activity_historial, container, false);

        lista_historial = view.findViewById(R.id.listaHistorial);
        arrayItem = new ArrayList<>();

        cargarHistorial(view.getContext());

        return view;
    }

    private void cargarHistorial(Context context){

        arrayItem.clear();

        arrayItem.add(new itemHistorial("1","Galerias metepec", "2:15 PM", R.drawable.ic_persona_h));
        arrayItem.add(new itemHistorial("2","Tecnológico de Toluca",  "7:45 PM",R.drawable.ic_persona_h));
        arrayItem.add(new itemHistorial("3","Casa", "8:00 PM", R.drawable.ic_persona_h));


        adapter = new itemHistorialAdaptador(arrayItem, context);
        lista_historial.setAdapter(adapter);

    }

    //El fragment se ha adjuntado al Activity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    //El Fragment ha sido creado
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    //La vista de layout ha sido creada y ya está disponible
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //La vista ha sido creada y cualquier configuración guardada está cargada
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    //El Activity que contiene el Fragment ha terminado su creación
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //El Fragment ha sido quitado de su Activity y ya no está disponible
    @Override
    public void onDetach() {
        super.onDetach();
    }

}
