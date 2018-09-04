package esteticaapp.co.kaxan;

import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class inicio extends Fragment {

    public static final int NOTIF_ID = 1001;
    public static final String NOTIF_MESSAGE = "NOTIF_MESSAGE";



    public static inicio newInstance(){
        return new inicio();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inicio, container, false);

        sendNotification(view.getContext());

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
        Intent resultIntent = new Intent(context, historial.class);
        //Agreamos un Extra con un mensaje
        resultIntent.putExtra(NOTIF_MESSAGE, getString(R.string.notif_body_intent));

        // Creamos un Stack ficticio para la Actividad que iniciaremos, de manear que cuando el
        // usuario haga click sobre la notificación, con esto nos aseguramos que una vez que el
        // usuario navegue a la actividad desde la Notificacion y presione el boton back, la app
        // navegue a la pantalla de la app, en vez de salirse de la misma.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Agregamos el  back stack para el Intent, pero no el Intent como tal
        stackBuilder.addParentStack(historial.class);

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
