package esteticaapp.co.kaxan;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class historial extends Fragment {

    public static historial newInstance(){
        return new historial();
    }

    RecyclerView listadeHistorial;

    DatabaseReference databaseReference;

    FirebaseRecyclerAdapter<itemHistorial,itemHistorialAdaptador.ViewHolder> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.activity_historial, container, false);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        listadeHistorial = view.findViewById(R.id.lista_historial);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listadeHistorial.setLayoutManager(linearLayoutManager);

        adapter=new FirebaseRecyclerAdapter<itemHistorial, itemHistorialAdaptador.ViewHolder>(
                itemHistorial.class,
                R.layout.obj_historial,
                itemHistorialAdaptador.ViewHolder.class,
                databaseReference.child("historial")
        ) {
            @Override
            protected void populateViewHolder(itemHistorialAdaptador.ViewHolder viewHolder,
                                              itemHistorial model, final int position) {
                viewHolder.lugar.setText(model.gethDesc());
                viewHolder.hora.setText(model.gethTiempo());

            }
        };

        listadeHistorial.setAdapter(adapter);

        return view;
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
