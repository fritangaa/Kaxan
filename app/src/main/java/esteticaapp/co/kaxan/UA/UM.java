package esteticaapp.co.kaxan.UA;

public class UM extends UsuarioGeneral{

    private String bateria;
    private String estado_signal;
    private String latitud;
    private String longitud;
    private String lugar;

    public UM(String nombre, String apellidos, String fecha_nacimiento, String edad, String telefono, String correo, String contrasena, int foto, String bateria, String estado_signal, String latitud, String longitud, String lugar) {
        super(nombre, apellidos, fecha_nacimiento, edad, telefono, correo, contrasena, foto);
        this.bateria = bateria;
        this.estado_signal = estado_signal;
        this.latitud = latitud;
        this.longitud = longitud;
        this.lugar = lugar;
    }

    public UM(String nombre, int foto, String bateria, String estado_signal, String latitud, String longitud) {
        super(nombre, foto);
        this.bateria = bateria;
        this.estado_signal = estado_signal;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public UM(String nombre, int foto, String bateria, String estado_signal, String latitud, String longitud, String lugar) {
        super(nombre, foto);
        this.bateria = bateria;
        this.estado_signal = estado_signal;
        this.latitud = latitud;
        this.longitud = longitud;
        this.lugar = lugar;
    }

    public UM(String nombre, String bateria, String estado_signal, String latitud, String longitud, String lugar) {
        super(nombre);
        this.bateria = bateria;
        this.estado_signal = estado_signal;
        this.latitud = latitud;
        this.longitud = longitud;
        this.lugar = lugar;
    }

    public UM(String nombre, String bateria, String estado_signal, String latitud, String longitud) {
        super(nombre);
        this.bateria = bateria;
        this.estado_signal = estado_signal;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public UM(String nombre, String bateria, String latitud, String longitud) {
        super(nombre);
        this.bateria = bateria;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public UM() {

    }

    public String getBateria() {
        return bateria;
    }

    public void setBateria(String bateria) {
        this.bateria = bateria;
    }

    public String getEstado_signal() {
        return estado_signal;
    }

    public void setEstado_signal(String estado_signal) {
        this.estado_signal = estado_signal;
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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
