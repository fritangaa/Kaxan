package esteticaapp.co.kaxan.UM.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import esteticaapp.co.kaxan.R;


public class listaEventos extends Fragment{

    private View view;

    Button verEvento;

    RecyclerView listadeEventos;

    DatabaseReference databaseReference;

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;

    FirebaseRecyclerAdapter<objEvento,objEventoViewHolder.ViewHolder> adapter;

    public static listaEventos newInstance(){
        return new listaEventos();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_lista_eventos, container, false);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference= FirebaseDatabase.getInstance().getReference("/2bpy1Be1DuNhmWPRuvup379JJW32/um");

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();


        verEvento = view.findViewById(R.id.boton_eventos);
        listadeEventos = view.findViewById(R.id.lista_eventos);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listadeEventos.setLayoutManager(linearLayoutManager);

        adapter=new FirebaseRecyclerAdapter<objEvento, objEventoViewHolder.ViewHolder>(
                objEvento.class,
                R.layout.obj_evento,
                objEventoViewHolder.ViewHolder.class,
                databaseReference.child(firebaseAuth.getUid()).child("evento")
        ) {
            @Override
            protected void populateViewHolder(objEventoViewHolder.ViewHolder viewHolder,
                                              objEvento model, final int position) {
                viewHolder.nombre.setText(model.getNombre());
                //------------------------------------------------------

                //------------------------------------------------------
                viewHolder.dia.setText(model.getDia());
                viewHolder.horaIni.setText(model.getHoraInicio());
                viewHolder.mapa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "Muestra mapa", Toast.LENGTH_SHORT).show();
                        //adapter.getRef(position).removeValue();
                    }
                });
                viewHolder.setItemLongClickListener(new ItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View v, int pos) {
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(view.getContext());
                        dialogo1.setTitle("¡Aviso!");
                        dialogo1.setIcon(R.drawable.ic_alerta_notificacion);
                        dialogo1.setMessage("El evento que seleccionaste se eliminara");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Toast.makeText(view.getContext(), "Evento eliminado", Toast.LENGTH_SHORT).show();
                                adapter.getRef(position).removeValue();
                            }
                        });
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Toast.makeText(view.getContext(), "El evento no se elimino", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogo1.show();

                    }
                });

            }
        };

        listadeEventos.setAdapter(adapter);

        verEvento.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), evento.class);
                startActivity(intent);
            }

        });


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
