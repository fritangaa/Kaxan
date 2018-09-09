package esteticaapp.co.kaxan;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class evento extends Fragment implements OnMapReadyCallback {

    private View view;

    private GoogleMap mMap;
    private MapView mMapView;
    private static final int LOCATION_REQUEST_CODE = 1;

    //Dia
    private static final String CERO = "0";
    private static final String BARRA = "/";
    //Hora
    private static final String DOS_PUNTOS = ":";


    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    //Widgets
    TextView etFecha;
    ImageButton ibObtenerFecha;

    //Widgets
    TextView etHora;
    ImageButton ibObtenerHora;

    //Widgets
    TextView etHora2;
    ImageButton ibObtenerHora2;

    //Widgets lugar
    TextView textoLugar;
    ImageButton btnLugar;
    private String direccion;
    private List<Address> address;









    public static evento newInstance(){
        return new evento();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_evento, container, false);

        etFecha = view.findViewById(R.id.texto_dia);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFecha = view.findViewById(R.id.boton_dia);
        //Evento setOnClickListener - clic
        ibObtenerFecha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                obtenerFecha();
            }
        });


        //Widget EditText donde se mostrara la hora obtenida
        etHora = view.findViewById(R.id.texto_hora_inicio);
        //Widget ImageButton del cual usaremos el evento clic para obtener la hora
        ibObtenerHora = view.findViewById(R.id.boton_hora_inicio);
        //Evento setOnClickListener - clic
        ibObtenerHora.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                obtenerHora(etHora);
            }
        });

        //Widget EditText donde se mostrara la hora obtenida
        etHora2 = view.findViewById(R.id.texto_hora_fin);
        //Widget ImageButton del cual usaremos el evento clic para obtener la hora
        ibObtenerHora2 = view.findViewById(R.id.boton_hora_fin);
        //Evento setOnClickListener - clic
        ibObtenerHora2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                obtenerHora(etHora2);
            }
        });



        btnLugar = view.findViewById(R.id.boton_lugar);
        textoLugar = view.findViewById(R.id.texto_lugar);

        btnLugar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                direccion = textoLugar.getText().toString();

                if(direccion.equals("")){
                    toast("No hay dirección para buscar : (");
                }else{
                    toast("Buscando \""+direccion+"\"");
                    Geocoder coder = new Geocoder(view.getContext());

                    try {
                        address = coder.getFromLocationName(direccion, 1);
                        Address location = address.get(0);
                        int lat = (int) (location.getLatitude()*1E6);
                        int lon = (int) (location.getLongitude()*1E6);
                        LatLng devento = new LatLng(lat, lon);
                        mMap.addMarker(new MarkerOptions().position(devento).title("Direccion"));
                        float zoomLevel = 16.0f; //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(devento, zoomLevel));
                    } catch (IOException e) {
                        toast("No se ha encontrado la dirección : (");
                    }
                }

                // Ocultar el teclado
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textoLugar.getWindowToken(), 0);
            }

        });


        mMapView = view.findViewById(R.id.mapaUbicacionEvento);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }



        return view;
    }



    public void toast(String mensaje){
        Toast.makeText(view.getContext(), mensaje, Toast.LENGTH_SHORT).show();
    }


    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }


    private void obtenerHora(final TextView textView){
        TimePickerDialog recogerHora = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                textView.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
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
