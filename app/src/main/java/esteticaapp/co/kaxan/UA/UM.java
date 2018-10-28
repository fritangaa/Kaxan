package esteticaapp.co.kaxan.UA;

public class UM extends UsuarioGeneral{

    private int nivel_bateria;
    private String estado_signal;
    private double latitud;
    private double longitud;
    private String lugar;

    public UM(String nombre, String apellidos, String fecha_nacimiento, int edad, String telefono, String correo, String contrasena, int foto, int nivel_bateria, String estado_signal, double latitud, double longitud, String lugar) {
        super(nombre, apellidos, fecha_nacimiento, edad, telefono, correo, contrasena, foto);
        this.nivel_bateria = nivel_bateria;
        this.estado_signal = estado_signal;
        this.latitud = latitud;
        this.longitud = longitud;
        this.lugar = lugar;
    }

    public UM(String nombre, int foto, int nivel_bateria, String estado_signal, double latitud, double longitud) {
        super(nombre, foto);
        this.nivel_bateria = nivel_bateria;
        this.estado_signal = estado_signal;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getNivel_bateria() {
        return nivel_bateria;
    }

    public void setNivel_bateria(int nivel_bateria) {
        this.nivel_bateria = nivel_bateria;
    }

    public String getEstado_signal() {
        return estado_signal;
    }

    public void setEstado_signal(String estado_signal) {
        this.estado_signal = estado_signal;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
