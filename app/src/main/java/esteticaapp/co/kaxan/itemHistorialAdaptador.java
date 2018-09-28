package esteticaapp.co.kaxan;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class itemHistorialAdaptador {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        CardView cardView;
        TextView lugar,hora;
        ImageView imagenH;

        ItemLongClickListener itemLongClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardviewHistorial);
            lugar=(TextView) itemView.findViewById(R.id.h_desc);
            hora=(TextView) itemView.findViewById(R.id.h_tiempo);
            imagenH=(ImageView) itemView.findViewById(R.id.h_imagen);

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