package esteticaapp.co.kaxan.UM.Menu;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
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

public class ubicacion extends Fragment implements OnMapReadyCallback {

    private DatabaseReference databaseReference;
    private DatabaseReference ubiRef;
    private DatabaseReference nomRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorage;

    private CircularImageView uaimagen;
    private TextView uanombre;
    private TextView bateria;
    private TextView ualugar;
    private RelativeLayout ubica;


    private double latum;
    private double lonum;

    Boolean actualPosition = true;
    JSONObject jso;
    Double longitudOrigen, latitudOrigen;

    private GoogleMap mMap;
    private MapView mMapView;
    private View view;


    private static final int LOCATION_REQUEST_CODE = 1;

    public static ubicacion newInstance() {
        return new ubicacion();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_ubicacion, container, false);

        ubiRef= FirebaseDatabase.getInstance().getReference("/ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2/ubicacion");
        nomRef= FirebaseDatabase.getInstance().getReference("/ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2/datos").child("nombre");

        uaimagen = (CircularImageView)view.findViewById(R.id.ua_imagen);
        uanombre = (TextView) view.findViewById(R.id.ua_nombre);
        bateria = (TextView) view.findViewById(R.id.ua_bateria);
        ualugar = (TextView) view.findViewById(R.id.ua_lugar_ubicacion);
        ubica = (RelativeLayout) view.findViewById(R.id.relative_no_disponible);




       /* mStorage.child("ua/imagenes/perfil/"+firebaseAuth.getUid()+"_perfil.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'photos/profile.png'
                new ubicacion.GetImageToURL().execute(""+uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                new ubicacion.GetImageToURL().execute("https://firebasestorage.googleapis.com/v0/b/kaxan-um.appspot.com/o/um%2Fimagenes%2Fperfil%2Fic_perfil_defecto.png?alt=media&token=8d457d20-5bb2-47ef-af19-cae27dd55e72");
            }
        });*/

        nomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uanombre.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        mMapView = (MapView) view.findViewById(R.id.mapaUbicacion);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        return view;
    }

    //La vista de layout ha sido creada y ya está disponible
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            return;
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

        ubiRef.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                mMap.clear();
                //leeremos un objeto de tipo Estudiante
                GenericTypeIndicator<objUbicacion> t = new GenericTypeIndicator<objUbicacion>() {};
                objUbicacion tprubi = dataSnapshot.getValue(t);

                if(tprubi.getLatitud().equals("0")||tprubi.getLongitud().equals("0")){
                    Toast.makeText(view.getContext(), "No esta permitido", Toast.LENGTH_SHORT).show();

                    mMapView.setVisibility(View.INVISIBLE);
                    uaimagen.setVisibility(View.INVISIBLE);
                    uanombre.setVisibility(View.INVISIBLE);
                    ualugar.setVisibility(View.INVISIBLE);
                    bateria.setVisibility(View.INVISIBLE);

                    ubica.setVisibility(View.VISIBLE);

                }else{

                    ubica.setVisibility(View.INVISIBLE);

                    mMapView.setVisibility(View.VISIBLE);
                    uaimagen.setVisibility(View.VISIBLE);
                    uanombre.setVisibility(View.VISIBLE);
                    ualugar.setVisibility(View.VISIBLE);
                    bateria.setVisibility(View.VISIBLE);

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            } else {
                Toast.makeText(getActivity(), "Error de permisos", Toast.LENGTH_LONG).show();
            }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                    ualugar.setText(DirCalle.getAddressLine(0));
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
            uaimagen.setImageBitmap(myBitMap);
        }
    }
}
