package esteticaapp.co.kaxan.UM.Menu;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import esteticaapp.co.kaxan.R;

public class inicio extends Fragment implements OnMapReadyCallback {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public static final int NOTIF_ID = 1001;
    public static final String NOTIF_MESSAGE = "NOTIF_MESSAGE";

    private GoogleMap mMap;
    private MapView mMapView;

    private Button auxilio;

    private static final int LOCATION_REQUEST_CODE = 1;

    private View view;

    private TextView bateria;
    private TextView umnombre;

    public static inicio newInstance(){
        return new inicio();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_inicio, container, false);

        firebaseAuth = FirebaseAuth.getInstance();



        bateria = (TextView) view.findViewById(R.id.textBateriaUM);

        umnombre = (TextView) view.findViewById(R.id.um_nombre);

        umnombre.setText(firebaseAuth.getUid());


        BroadcastReceiver bateriaReciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                context.unregisterReceiver(this);
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
                int level = -1;
                if (currentLevel >=0 && scale > 0){
                    level= (currentLevel * 100)/scale;
                }
                bateria.setText(level+"%");

            }
        };
        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(bateriaReciever,batteryFilter);


        mMapView = view.findViewById(R.id.mapaUbicacionAdmin);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        auxilio = view.findViewById(R.id.botonAuxilio);

        if(ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(
                view.getContext(),Manifest
                        .permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]
                    { Manifest.permission.SEND_SMS,},1000);
        }else{
        };
        auxilio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enviarMensaje("+527221164007","Necesito ayuda, utiliza mi ubicación");
            }
        });


        return view;
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
            mMap.setMyLocationEnabled(true);
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

        // Add a marker in Sydney and move the camera
        LatLng tec = new LatLng(19.256953, -99.577957);
        mMap.addMarker(new MarkerOptions().position(tec).title("Administrador"));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tec, zoomLevel));


    }

    private void enviarMensaje (String numero, String mensaje){
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numero,null,mensaje,null,null);
            Toast.makeText(view.getContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(view.getContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
