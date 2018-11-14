package esteticaapp.co.kaxan.UA;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import esteticaapp.co.kaxan.R;
import esteticaapp.co.kaxan.UM.menu;
import esteticaapp.co.kaxan.objUbicacion;

public class UAPrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapaFragment.OnFragmentInteractionListener {

    DatabaseReference databaseReference;
    DatabaseReference ref;
    DatabaseReference ref2;

    private ImageView profile_admin;
    private TextView no_miembros;

    private String lat ="";
    private String lon ="";
    private String bateria ="";
    private String visible="";

    private StorageReference mStorage;

    private Integer tmpEje = 5000;

    private TextView mTextMessage;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                new UAPrincipalActivity.Insertar(UAPrincipalActivity.this).execute();
                handler.postDelayed(runnable, 15000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uaprincipal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStorage= FirebaseStorage.getInstance().getReference();



        databaseReference= FirebaseDatabase.getInstance().getReference("/ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2").child("ubicacion");
        ref = FirebaseDatabase.getInstance().getReference("/ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2").child("configuracion").child("visible");
        ref2 = FirebaseDatabase.getInstance().getReference("/ZxdtUxxfUoRrTw9dxoHA6XLAHqJ2").child("um");

        handler.postDelayed(runnable, tmpEje);//empezamos a mandar datos

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = findViewById(R.id.nav_header_ua);


        mStorage.child("um/imagenes/perfil/ic_perfil_defecto.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'photos/profile.png'
                new GetImageToURL().execute(""+uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                new GetImageToURL().execute("https://firebasestorage.googleapis.com/v0/b/kaxan-um.appspot.com/o/um%2Fimagenes%2Fperfil%2Fic_perfil_defecto.png?alt=media&token=8d457d20-5bb2-47ef-af19-cae27dd55e72");
            }
        });

        Fragment fragment = new MapaFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_main,fragment).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        profile_admin = hView.findViewById(R.id.imagen_admin_perfil);
        no_miembros = hView.findViewById(R.id.txtNoMiembros);

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                    no_miembros.setText("Número de miembros: "+String.valueOf(dataSnapshot.getChildrenCount()));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //--------------------segundoplano---------------------------
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        //-----------------------------------------------
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.uaprincipal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.agrega_miembro) {
            Intent intencion = new Intent(getApplication(), leerQR.class);
            startActivity(intencion);
            finish();
        }else if(id == R.id.ver_um){
            Toast.makeText(getApplicationContext(),"Ver usuarios",Toast.LENGTH_LONG).show();
        }else if(id == R.id.ver_preguntas){
            Toast.makeText(getApplicationContext(),"Ver preguntas",Toast.LENGTH_LONG).show();
        }else if(id == R.id.agrega_evento){
            Toast.makeText(getApplicationContext(),"Agregar eventa",Toast.LENGTH_LONG).show();
        }else if(id == R.id.agrega_rutina){
            Toast.makeText(getApplicationContext(),"Agrega rutina",Toast.LENGTH_LONG).show();
        }else if(id == R.id.elimina_grupo){
            Toast.makeText(getApplicationContext(),"Elimina grupo",Toast.LENGTH_LONG).show();
        }else if(id == R.id.salir_cuenta){
/*            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);*/
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment frag = null;
        boolean fragSelected = false;

        if (id == R.id.nav_grupo_ejemplo) {
            frag = new MapaFragment();
            fragSelected = true;
        } else if (id == R.id.nav_premium) {

        } else if (id == R.id.nav_config) {
            startActivity(new Intent(UAPrincipalActivity.this, ConfigActivity.class));
        } else if (id == R.id.nav_help) {

        }

        if (fragSelected){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,frag).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

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
                if (bateria.equals("")){
                    bateria="0";
                }

            }
        };
        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        UAPrincipalActivity.this.registerReceiver(bateriaReciever,batteryFilter);

        //--------------------------------------------------------------------------

        objUbicacion ubicacion = new objUbicacion(lat,lon,bateria,"Baja");
        objUbicacion no_visible = new objUbicacion("0","0","0","Baja");



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                visible = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(visible.equals("1")){
            databaseReference.setValue(ubicacion);
        }else if(visible.equals("0")){
            databaseReference.setValue(no_visible);
        }



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
        UAPrincipalActivity.Localizacion Local = new Localizacion();
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

    public class Localizacion implements android.location.LocationListener {
        UAPrincipalActivity mainActivity;

        public UAPrincipalActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(UAPrincipalActivity mainActivity) {
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
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(UAPrincipalActivity.this, "GPS desactivado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(UAPrincipalActivity.this, "GPS activado", Toast.LENGTH_SHORT).show();
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

    class GetImageToURL extends AsyncTask< String, Void, Bitmap> {



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
            profile_admin.setImageBitmap(myBitMap);
        }
    }

}
