package esteticaapp.co.kaxan.UA.recyclerMiembros;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import esteticaapp.co.kaxan.R;
import esteticaapp.co.kaxan.UA.UM;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ViewHolder>{

    private List<UM> monitoredUsers;
    private int layout;
    private OnItemClickListener itemClickListener;

    public AdapterRecycler(List<UM> monitoredUsers, int layout, OnItemClickListener listener){

        this.monitoredUsers = monitoredUsers;
        this.layout = layout;
        this.itemClickListener = listener;

    }

    public AdapterRecycler(List<UM> monitoredUsers, int layout){

        this.monitoredUsers = monitoredUsers;
        this.layout = layout;


    }

    public AdapterRecycler(){



    }

    public void updateList (List<UM> items, Context context) {


        if (items != null && items.size() > 0) {

            Toast.makeText(context,String.valueOf(items.size()),Toast.LENGTH_LONG).show();
            monitoredUsers.clear();
            monitoredUsers.addAll(items);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(monitoredUsers.get(position),itemClickListener);

    }

    @Override
    public int getItemCount() {
        return monitoredUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageViewUserPhoto;
        public TextView textViewNombre;
        public TextView textViewEstadoBateria;
        public ImageView imgBattery;
        public ImageView imgSignal;
        public TextView textViewSignal;
        public TextView textViewPlace;

        public ViewHolder(View itemView){
            super(itemView);

            imageViewUserPhoto = itemView.findViewById(R.id.imageViewUserPhoto);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewEstadoBateria = itemView.findViewById(R.id.textViewEstadoBateria);
            textViewSignal = itemView.findViewById(R.id.textViewSignal);
            textViewPlace = itemView.findViewById(R.id.textViewPlace);
            imgBattery = itemView.findViewById(R.id.vecBattery);
            imgSignal = itemView.findViewById(R.id.vecSignal);

        }

        public void bind(final UM monitoredUser, final OnItemClickListener listener){

            imageViewUserPhoto.setImageResource(monitoredUser.getFoto());
            textViewNombre.setText(monitoredUser.getNombre());

            if(monitoredUser.getNivel_bateria() == 100){
                imgBattery.setImageResource(R.drawable.batteryfull);
            }else{
                if(monitoredUser.getNivel_bateria()>=90 && monitoredUser.getNivel_bateria()<100){
                    imgBattery.setImageResource(R.drawable.battery90);
                }else{
                    if(monitoredUser.getNivel_bateria()>=80 && monitoredUser.getNivel_bateria()<90){
                        imgBattery.setImageResource(R.drawable.battery80);
                    }else{
                        if (monitoredUser.getNivel_bateria()>=60 && monitoredUser.getNivel_bateria()<80){
                            imgBattery.setImageResource(R.drawable.battery60);
                        }else{
                            if (monitoredUser.getNivel_bateria()>=50 && monitoredUser.getNivel_bateria()<60){
                                imgBattery.setImageResource(R.drawable.battery50);
                            }else{
                                if (monitoredUser.getNivel_bateria()>=30 && monitoredUser.getNivel_bateria()<50){
                                    imgBattery.setImageResource(R.drawable.battery30);
                                }else{
                                    if (monitoredUser.getNivel_bateria()>=20 && monitoredUser.getNivel_bateria()<30){
                                        imgBattery.setImageResource(R.drawable.battery20);
                                    }else{
                                        if (monitoredUser.getNivel_bateria()>=1 && monitoredUser.getNivel_bateria()<20){
                                            imgBattery.setImageResource(R.drawable.batteryalert);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            textViewEstadoBateria.setText(String.valueOf(monitoredUser.getNivel_bateria()) + "%");

            if(monitoredUser.getEstado_signal().equals("Intensa")){
                imgSignal.setImageResource(R.drawable.signal4);
            }else{
                if (monitoredUser.getEstado_signal().equals("Alta")){
                    imgSignal.setImageResource(R.drawable.signal3);
                }else{
                    if (monitoredUser.getEstado_signal().equals("Media")){
                        imgSignal.setImageResource(R.drawable.signal2);
                    }else{
                        if (monitoredUser.getEstado_signal().equals("Baja")){
                            imgSignal.setImageResource(R.drawable.signal1);
                        }else{
                            if (monitoredUser.getEstado_signal().equals("Sin senial")){
                                imgSignal.setImageResource(R.drawable.signal0);
                            }
                        }
                    }
                }
            }
            textViewSignal.setText(monitoredUser.getEstado_signal());
            textViewPlace.setText(monitoredUser.getLugar());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(monitoredUser,getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(UM monitoredUser, int position);
    }

}
