package esteticaapp.co.kaxan;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class objEventoViewHolder {

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView lugar,dia,horaIni,horaFin;
        Button eliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardview);
            lugar=(TextView) itemView.findViewById(R.id.obj_lugar);
            dia=(TextView) itemView.findViewById(R.id.obj_dia);
            horaIni=(TextView) itemView.findViewById(R.id.obj_horaI);
            horaFin=(TextView) itemView.findViewById(R.id.obj_horaF);
            eliminar=(Button) itemView.findViewById(R.id.obj_eliminar);
        }
    }
}
