package esteticaapp.co.kaxan.UA.recyclerMiembros;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import esteticaapp.co.kaxan.R;
import esteticaapp.co.kaxan.UM.Menu.ItemLongClickListener;

public class MiembroUMRecycler {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        CardView cardView;
        public ImageView imageViewUserPhoto;
        public TextView textViewNombre;
        public TextView textViewEstadoBateria;
        public ImageView imgBattery;
        public ImageView imgSignal;
        public TextView textViewSignal;
        public TextView textViewPlace;

        ItemLongClickListener itemLongClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardviewMiembro);
            imageViewUserPhoto = itemView.findViewById(R.id.imageViewUserPhoto);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewEstadoBateria = itemView.findViewById(R.id.textViewEstadoBateria);
            textViewSignal = itemView.findViewById(R.id.textViewSignal);
            textViewPlace = itemView.findViewById(R.id.textViewPlace);
            imgBattery = itemView.findViewById(R.id.vecBattery);
            imgSignal = itemView.findViewById(R.id.vecSignal);

            itemView.setOnLongClickListener(this);
        }

        public void setItemLongClickListener(ItemLongClickListener ic)
        {
            this.itemLongClickListener=ic;
        }

        @Override
        public boolean onLongClick(View v) {
            this.itemLongClickListener.onItemLongClick(v,getLayoutPosition());
            return false;
        }
    }
}
