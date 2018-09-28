package esteticaapp.co.kaxan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class listaEventos extends AppCompatActivity {

    RecyclerView listadeEventos;

    DatabaseReference databaseReference;

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;

    FirebaseRecyclerAdapter<objEvento,objEventoViewHolder.ViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();


        listadeEventos = findViewById(R.id.lista_eventos);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
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
                viewHolder.dia.setText(model.getDia());
                viewHolder.horaIni.setText(model.getHoraInicio());
                viewHolder.mapa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Muestra mapa", Toast.LENGTH_SHORT).show();
                        //adapter.getRef(position).removeValue();
                    }
                });
                viewHolder.setItemLongClickListener(new ItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View v, int pos) {
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(listaEventos.this);
                        dialogo1.setTitle("¡Aviso!");
                        dialogo1.setIcon(R.drawable.ic_alerta_notificacion);
                        dialogo1.setMessage("El evento que seleccionaste se eliminara");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Toast.makeText(getApplicationContext(), "Evento eliminado", Toast.LENGTH_SHORT).show();
                                adapter.getRef(position).removeValue();
                            }
                        });
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Toast.makeText(getApplicationContext(), "El evento no se elimino", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogo1.show();

                    }
                });

            }
        };

        listadeEventos.setAdapter(adapter);


    }
}
