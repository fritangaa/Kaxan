package esteticaapp.co.kaxan.UM;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import esteticaapp.co.kaxan.R;
import esteticaapp.co.kaxan.UM.Menu.inicio;
import esteticaapp.co.kaxan.UM.Menu.listaEventos;
import esteticaapp.co.kaxan.UM.Menu.perfil;
import esteticaapp.co.kaxan.UM.Menu.ubicacion;

public class menu extends AppCompatActivity {

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    private String lat;
    private String lon;
    private String bateria;

    private Integer tmpEje = 120000;

    private TextView mTextMessage;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                new Insertar(menu.this).execute();
                handler.postDelayed(runnable, 15000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_ubicacion:
                    loadUbicacion();
                    return true;
                /*case R.id.navigation_historial:
                    loadHistorial();
                    return true;*/
                case R.id.navigation_principal:
                    loadInicio();
                    return true;
                case R.id.navigation_evento:
                    loadEvento();
                    return true;
                case R.id.navigation_perfil:
                    loadPerfil();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        handler.postDelayed(runnable, 10000);//empezamos a mandar datos

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //--------------------segundoplano---------------------------
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        //-----------------------------------------------
        navigation.getMenu().getItem(1).setChecked(true);
        loadInicio();

    }

    private void loadEvento() {

        listaEventos fragment = listaEventos.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    /*private void loadHistorial() {

        historial fragment = historial.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }*/

    private void loadInicio() {

        inicio fragment = inicio.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadUbicacion() {

        ubicacion fragment = ubicacion.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadPerfil() {

        perfil fragment = perfil.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }


    private void loadHistorialCalendario() {

        /*Intent intent = new Intent(menu.this, historial_mapa.class);
        startActivity(intent);
        finish();*/
    }


    //--------------------segundoplano---------------------------


    //Insertamos los datos a nuestra base
    private boolean insertar(){
        //--------------------------bateria------------------------------------

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
                bateria = (""+level);

            }
        };
        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        menu.this.registerReceiver(bateriaReciever,batteryFilter);

        //--------------------------------------------------------------------------

        String idcordenadas = databaseReference.push().getKey();
        String Text = (lat +"," +lon+","+bateria);
        databaseReference.child("miTv2AU2Y2UUKDeYXiooRc71Dhi2").child("um").child(firebaseAuth.getUid()).child("ubicacion").setValue(Text);

        return  true;
    }

    //AsyncTask para insertar Datos
    class Insertar extends AsyncTask<String,String,String> {

        private Activity context;

        Insertar(Activity context){
            this.context=context;
        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if(insertar())
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        lat = "";
                        lon = "";
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Datos no insertado con éxito", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //Apartir de aqui empezamos a obtener la direciones y coordenadas
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (android.location.LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) Local);

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 3000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class Localizacion implements android.location.LocationListener {
        menu mainActivity;

        public menu getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(menu mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            lat = String.valueOf(loc.getLatitude());
            lon = String.valueOf(loc.getLongitude());

            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(menu.this, "GPS desactivado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(menu.this, "GPS activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}
