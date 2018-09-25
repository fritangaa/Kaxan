package esteticaapp.co.kaxan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class listaEventos extends AppCompatActivity {

    RecyclerView listadeEventos;

    DatabaseReference databaseReference;

    FirebaseRecyclerAdapter<objEvento,objEventoViewHolder.ViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        listadeEventos = findViewById(R.id.lista_eventos);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listadeEventos.setLayoutManager(linearLayoutManager);

        adapter=new FirebaseRecyclerAdapter<objEvento, objEventoViewHolder.ViewHolder>(
                objEvento.class,
                R.layout.obj_evento,
                objEventoViewHolder.ViewHolder.class,
                databaseReference.child("evento")
        ) {
            @Override
            protected void populateViewHolder(objEventoViewHolder.ViewHolder viewHolder,
                                              objEvento model, final int position) {
                viewHolder.lugar.setText(model.getLugar());
                viewHolder.dia.setText(model.getDia());
                viewHolder.horaIni.setText(model.getHoraInicio());
                viewHolder.horaFin.setText(model.getHoraFin());
                viewHolder.eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Evvento eliminado", Toast.LENGTH_SHORT).show();
                        adapter.getRef(position).removeValue();
                    }
                });

            }
        };

        listadeEventos.setAdapter(adapter);


    }
}
