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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.PolyUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import esteticaapp.co.kaxan.R;
import esteticaapp.co.kaxan.objUbicacion;

public class inicio extends Fragment implements OnMapReadyCallback {
    private DatabaseReference databaseReference;
    private DatabaseReference ubiRef;
    private DatabaseReference numRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorage;

    public static final int NOTIF_ID = 1001;
    public static final String NOTIF_MESSAGE = "NOTIF_MESSAGE";

    private GoogleMap mMap;
    private MapView mMapView;

    private Button auxilio;

    private static final int LOCATION_REQUEST_CODE = 1;

    private View view;

    private TextView bateria;
    private TextView umnombre;
    private TextView umlugar;
    private CircularImageView umimagen;

    private double latum;
    private double lonum;
    private String numUA;

    public static inicio newInstance(){
        return new inicio();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_inicio, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("/ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2/um").child(firebaseAuth.getUid()).child("datos").child("nombre");
        ubiRef= FirebaseDatabase.getInstance().getReference("/ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2/um").child(firebaseAuth.getUid()).child("ubicacion");
        numRef= FirebaseDatabase.getInstance().getReference("/ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2/datos").child("telefono");


        bateria = (TextView) view.findViewById(R.id.textBateriaUM);

        umnombre = (TextView) view.findViewById(R.id.um_nombre);

        umlugar= (TextView)view.findViewById(R.id.um_lugar_ubicacion);

        umimagen = (CircularImageView)view.findViewById(R.id.um_imagen);

        mStorage= FirebaseStorage.getInstance().getReference();

        mStorage.child("um/imagenes/perfil/"+firebaseAuth.getUid()+"_perfil.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'photos/profile.png'
                new inicio.GetImageToURL().execute(""+uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                new inicio.GetImageToURL().execute("https://firebasestorage.googleapis.com/v0/b/kaxan-um.appspot.com/o/um%2Fimagenes%2Fperfil%2Fic_perfil_defecto.png?alt=media&token=8d457d20-5bb2-47ef-af19-cae27dd55e72");
            }
        });

        // Attach a listener to read the data at our posts reference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                umnombre.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //---------------------------------numero de telefono------------------------

        // Attach a listener to read the data at our posts reference
        numRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numUA = String.valueOf(dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //bateria
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
                enviarMensaje("+52"+numUA,"Necesito ayuda, utiliza mi ubicación");

            }
        });



        return view;
    }

 


    JSONObject jso;

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


        ubiRef.addValueEventListener(new ValueEventListener() {



            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                mMap.clear();
                //leeremos un objeto de tipo Estudiante
                GenericTypeIndicator<objUbicacion> t = new GenericTypeIndicator<objUbicacion>() {};
                objUbicacion tprubi = dataSnapshot.getValue(t);

                if(tprubi.getLatitud().equals("")||tprubi.getLongitud().equals("")){
                    Toast.makeText(view.getContext(), "Un momento...", Toast.LENGTH_SHORT).show();
                }else{
                    latum = Double.parseDouble(tprubi.getLatitud());
                    lonum = Double.parseDouble(tprubi.getLongitud());
                    if (tprubi.getBateria().isEmpty()){
                    }else{
                        bateria.setText(tprubi.getBateria()+"%");
                    }

                    LatLng tec = new LatLng(latum, lonum);
                    mMap.addMarker(new MarkerOptions()
                            .position(tec)
                            .title("Mi ubicacion")
                            .snippet(latum+","+lonum)
                    );

                    //----------------------------------------------------------------------

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latum,lonum))      // Sets the center of the map to Mountain View
                            .zoom(17)
                            .bearing(90)// Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    setLocation();

                    //"https://maps.googleapis.com/maps/api/distancematrix/json?origins="+latitudOrigen+","+longitudOrigen+"&destinations=2.9435667,-75.2458577&key=AIzaSyDWgFl93THQ8OUHNfZzvmkN16cH03u3GVI"
                    //viejo---https://maps.googleapis.com/maps/api/directions/json?origin="+latitudOrigen+","+longitudOrigen+"&destination=2.9435667,-75.2458577
                    String url ="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+latum+","+lonum+"&destinations=19.258083,%20-99.580768&key=AIzaSyDWgFl93THQ8OUHNfZzvmkN16cH03u3GVI";
                    RequestQueue queue = Volley.newRequestQueue(view.getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                jso = new JSONObject(response);
                                trazarRuta(jso);
                                Log.i("jsonRuta: ",""+response);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    queue.add(stringRequest);


                }

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }

    private void trazarRuta(JSONObject jso) {

        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            jRoutes = jso.getJSONArray("routes");
            for (int i=0; i<jRoutes.length();i++){

                jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");

                for (int j=0; j<jLegs.length();j++){

                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k<jSteps.length();k++){


                        String polyline = ""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end",""+polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);
                        mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.GRAY).width(5));



                    }



                }



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setLocation() {
        //Obtener la direccion de la calle a partir de la latitud y la longitud

            if (latum != 0.0 && lonum != 0.0) {
                try {
                    Geocoder geocoder = new Geocoder(view.getContext(), Locale.getDefault());
                    List<Address> list = geocoder.getFromLocation(
                            latum, lonum, 1);
                    if (!list.isEmpty()) {
                        Address DirCalle = list.get(0);
                        umlugar.setText(DirCalle.getAddressLine(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }

    private class GetImageToURL extends AsyncTask< String, Void, Bitmap > {

        @Override
        protected Bitmap doInBackground(String...params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap myBitMap) {
            umimagen.setImageBitmap(myBitMap);
        }
    }


}
