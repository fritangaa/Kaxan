package esteticaapp.co.kaxan.UM.Menu;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import esteticaapp.co.kaxan.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class listaEventos extends Fragment implements OnMapReadyCallback {

    private View view;

    Button verEvento;

    RecyclerView listadeEventos;

    DatabaseReference databaseReference;

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;

    private GoogleMap mMap;
    private MapView mMapView;
    private static final int LOCATION_REQUEST_CODE = 1;

    FirebaseRecyclerAdapter<objEvento,objEventoViewHolder.ViewHolder> adapter;

    private LayoutInflater layoutInflater;
    private View popupView;
    private PopupWindow popupWindow;

    private LinearLayout layout;

    private String ubiEventoUM;
    public static listaEventos newInstance(){
        return new listaEventos();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_lista_eventos, container, false);

        layout = new LinearLayout(view.getContext());

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference= FirebaseDatabase.getInstance().getReference("/ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2/um");

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();


        verEvento = view.findViewById(R.id.boton_eventos);
        listadeEventos = view.findViewById(R.id.lista_eventos);

        mMapView = view.findViewById(R.id.mapaEveUM);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


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
            protected void populateViewHolder(final objEventoViewHolder.ViewHolder viewHolder,
                                              final objEvento model, final int position) {
                viewHolder.nombre.setText(model.getNombre());
                //------------------------------------------------------

                //------------------------------------------------------
                viewHolder.dia.setText(model.getDia());
                viewHolder.horaIni.setText(model.getHoraInicio());
                viewHolder.mapa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ubiEventoUM = model.getUbicacion();
                        Toast.makeText(view.getContext(), ubiEventoUM, Toast.LENGTH_SHORT).show();
                        mostrarMapa();
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

    private void mostrarMapa(){

        layoutInflater =(LayoutInflater)getActivity().getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.dialog_mapa, null);

        popupWindow  = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(R.style.popup_window_animation_phone);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);


        popupWindow.showAtLocation(layout, Gravity.CENTER,0, 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            view.getContext(), R.raw.mapstyle));

            if (!success) {
                Log.e("ubicacion", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("ubicacion", "Can't find style. Error: ", e);
        }

        // Controles UI
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        String[] parts = ubiEventoUM.split(",");
        Double lat = Double.parseDouble(parts[0]);
        Double lon = Double.parseDouble(parts[1]);

        LatLng buscar = new LatLng(lat,lon);
        mMap.addMarker(new MarkerOptions().position(buscar).title("Marker UPV"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(buscar));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(buscar)      // Sets the center of the map to Mountain View
                .zoom(17)
                .bearing(90)// Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


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
