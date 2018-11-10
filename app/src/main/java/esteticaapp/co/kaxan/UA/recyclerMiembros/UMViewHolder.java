package esteticaapp.co.kaxan.UA.recyclerMiembros;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import esteticaapp.co.kaxan.R;

public class UMViewHolder {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mview;

        CardView cardView;
        public TextView nombre,bateria,senial,direccion;
        public ImageView imgBateria, imgSenial;
        public CircularImageView imgFoto;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardviewMiembro);
            imgFoto=itemView.findViewById(R.id.imageViewUserPhoto);
            nombre= itemView.findViewById(R.id.textViewNombre);
            imgBateria=itemView.findViewById(R.id.vecBattery);
            bateria=itemView.findViewById(R.id.textViewEstadoBateria);
            imgSenial=itemView.findViewById(R.id.vecSignal);
            senial=itemView.findViewById(R.id.textViewSignal);
            direccion=itemView.findViewById(R.id.textViewPlace);

            mview=itemView;

        }



    }

}
