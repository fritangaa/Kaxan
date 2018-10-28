package esteticaapp.co.kaxan.UA;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import esteticaapp.co.kaxan.R;
import esteticaapp.co.kaxan.UA.recyclerMiembros.AdapterRecycler;
import esteticaapp.co.kaxan.UA.recyclerMiembros.MiembroUMRecycler;
import esteticaapp.co.kaxan.UM.Menu.ItemLongClickListener;
import esteticaapp.co.kaxan.UM.Menu.objEvento;
import esteticaapp.co.kaxan.UM.Menu.objEventoViewHolder;

public class MapaFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    GoogleMap mgoogleMap;
    MapView mapView;
    View mview;

    DatabaseReference ref;

    ArrayList<UM> monitoredUsers= new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MapaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MapaFragment newInstance(String param1, String param2) {
        MapaFragment fragment = new MapaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mview = inflater.inflate(R.layout.fragment_mapa, container, false);
        return mview;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ref = FirebaseDatabase.getInstance().getReference("/2bpy1Be1DuNhmWPRuvup379JJW32/um");

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                            monitoredUsers.add(new UM(String.valueOf(dsp.child("datos").child("nombre").getValue()), R.drawable.ic_persona_h,Integer.parseInt(String.valueOf(dsp.child("ubicacion").child("bateria").getValue()))
                              ,"Intensa",Double.parseDouble(String.valueOf(dsp.child("ubicacion").child("latitud").getValue())), Double.parseDouble(String.valueOf(dsp.child("ubicacion").child("longitud").getValue()))));

                        }

                        setLocation();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        mapView = (MapView) mview.findViewById(R.id.mapaMiembros);
        if (mapView != null) {

            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);

        }

        mRecyclerView = mview.findViewById(R.id.recyclerMiembros);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new AdapterRecycler(monitoredUsers, R.layout.recycler_view_item, new AdapterRecycler.OnItemClickListener() {
            @Override
            public void onItemClick(UM monitoredUser, final int position) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(getContext(), mRecyclerView);
                //inflating menu from xml resource
                popup.inflate(R.menu.opciones_miembros);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ver_en_mapa:
                                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(monitoredUsers.get(position).getLatitud(),monitoredUsers.get(position).getLongitud()))
                                        .title("NOWHERE").snippet("HOLA"));

                                CameraPosition liberty = CameraPosition.builder().target(new LatLng(monitoredUsers.get(position).getLatitud(),monitoredUsers.get(position).getLongitud()))
                                        .zoom(16).bearing(0).tilt(0).build();

                                mgoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(liberty));

                                break;
                            case R.id.agrega_evento_user:
                                Toast.makeText(getContext(),"Agrega evento",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.agrega_rutina_user:
                                Toast.makeText(getContext(),"Agrega rutina",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.ver_historial_rutas:
                                Toast.makeText(getContext(),"Ver historial de rutas",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.ver_historial_alertas:
                                Toast.makeText(getContext(),"Ver historial de alertas",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.ver_historial_caja:
                                Toast.makeText(getContext(),"Ver historial de caja negra",Toast.LENGTH_LONG).show();
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mgoogleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(19.257385, -99.577600))
                .title("NOWHERE").snippet("HOLA"));


        CameraPosition liberty = CameraPosition.builder().target(new LatLng(19.257385, -99.577600))
                .zoom(16).bearing(0).tilt(0).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(liberty));
    }

    private List<UM> getAllMonitoredUsers(){
        return new ArrayList<UM>(){{

            add(new UM("Ricardo", R.drawable.ic_persona_h,100
                    ,"Intensa",19.283627, -99.634821));
        }};
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setLocation() {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        for (int cont = 0; cont<monitoredUsers.size();cont++){
            if (monitoredUsers.get(cont).getLatitud() != 0.0 && monitoredUsers.get(cont).getLongitud() != 0.0) {
                try {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> list = geocoder.getFromLocation(
                            monitoredUsers.get(cont).getLatitud(), monitoredUsers.get(cont).getLongitud(), 1);
                    if (!list.isEmpty()) {
                        Address DirCalle = list.get(0);
                        monitoredUsers.get(cont).setLugar(DirCalle.getAddressLine(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
