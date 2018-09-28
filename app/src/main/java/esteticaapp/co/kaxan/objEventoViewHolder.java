package esteticaapp.co.kaxan;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class objEventoViewHolder {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        CardView cardView;
        TextView nombre,dia,horaIni;
        Button mapa;

        ItemLongClickListener itemLongClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardview);
            nombre=(TextView) itemView.findViewById(R.id.obj_etiqueta);
            dia=(TextView) itemView.findViewById(R.id.obj_dia);
            horaIni=(TextView) itemView.findViewById(R.id.obj_hora);
            mapa=(Button) itemView.findViewById(R.id.obj_lugar);

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
