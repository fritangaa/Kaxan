package esteticaapp.co.kaxan;

public class objUbicacion {

    private String latitud;
    private String longitud;
    private String bateria;

    public objUbicacion() {
    }

    public objUbicacion(String latitud, String longitud, String bateria) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.bateria = bateria;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getBateria() {
        return bateria;
    }

    public void setBateria(String bateria) {
        this.bateria = bateria;
    }
}
