package esteticaapp.co.kaxan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class inicio extends Fragment implements OnMapReadyCallback {

    public static final int NOTIF_ID = 1001;
    public static final String NOTIF_MESSAGE = "NOTIF_MESSAGE";

    private GoogleMap mMap;
    private MapView mMapView;

    private Button auxilio;

    private static final int LOCATION_REQUEST_CODE = 1;

    private View view;

    public static inicio newInstance(){
        return new inicio();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_inicio, container, false);

        sendNotification(view.getContext());

        mMapView = view.findViewById(R.id.mapaUbicacionAdmin);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


        Intent intent = getActivity().getIntent();
        if (intent != null){
            final AlertDialog.Builder mDialog = new AlertDialog.Builder(view.getContext());
            mDialog.setMessage("¿Todo en orden?");
            mDialog.setTitle("Alerta");
            mDialog.setPositiveButton("Necesito ayuda", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            mDialog.setNegativeButton("Todo bien", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog dialog = mDialog.create();
            dialog.show();

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

    private void sendNotification(Context context) {
        //Preparamos la Notificación com un icono, titulo, texto, prioridad, color led, sonido y persistencia
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_alerta_persona)
                        .setContentTitle(getString(R.string.notif_title))
                        .setContentText(getString(R.string.notif_body))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setLights(Color.CYAN, 1, 0)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setOngoing(true);

        // Creamos un Intent explicito el cual disparará el OtherActivity de nuestra app
        Intent resultIntent = new Intent(view.getContext(), menu.class);
        //Agreamos un Extra con un mensaje
        resultIntent.putExtra(NOTIF_MESSAGE, getString(R.string.notif_body_intent));

        // Creamos un Stack ficticio para la Actividad que iniciaremos, de manear que cuando el
        // usuario haga click sobre la notificación, con esto nos aseguramos que una vez que el
        // usuario navegue a la actividad desde la Notificacion y presione el boton back, la app
        // navegue a la pantalla de la app, en vez de salirse de la misma.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Agregamos el  back stack para el Intent, pero no el Intent como tal
        stackBuilder.addParentStack(menu.class);

        // Agregamos el  Intent que inicia el Activity al inicio del stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT );
        mBuilder.setContentIntent(resultPendingIntent);

        //Obtenemos una instancia del NotificationManager
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Usando el método notify del NotificationManager, enviamos la notificacion asociandola
        // a un ID con elCual podamos actualizarla en caso de que sea necesario
        mNotificationManager.notify(NOTIF_ID, mBuilder.build());
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
