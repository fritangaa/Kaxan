package esteticaapp.co.kaxan.UA;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import esteticaapp.co.kaxan.R;

public class UAPrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapaFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uaprincipal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Fragment fragment = new MapaFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_main,fragment).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            //startActivity(new Intent(UAPrincipalActivity.this, SettingsActivity.class));
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
}
