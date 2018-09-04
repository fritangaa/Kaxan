package esteticaapp.co.kaxan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class itemHistorialAdaptador extends BaseAdapter {

    private ArrayList<itemHistorial> arrayListItem;
    private Context context;
    private LayoutInflater layoutInflater;

    public itemHistorialAdaptador(ArrayList<itemHistorial> arrayListItem, Context context) {
        this.arrayListItem = arrayListItem;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vistaItem = layoutInflater.inflate(R.layout.obj_historial, parent, false);

        ImageView himagen = (ImageView) vistaItem.findViewById(R.id.h_imagen);
        TextView hdesc = (TextView) vistaItem.findViewById(R.id.h_desc);
        TextView htiempo = (TextView) vistaItem.findViewById(R.id.h_tiempo);

        himagen.setImageResource(arrayListItem.get(position).gethImagen());
        hdesc.setText(arrayListItem.get(position).gethDesc());
        htiempo.setText(arrayListItem.get(position).gethTiempo());


        return vistaItem;
    }
}