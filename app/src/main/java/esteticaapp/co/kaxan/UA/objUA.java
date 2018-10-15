package esteticaapp.co.kaxan.UA;

public class objUA {

    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    private String edad;

    public objUA() {
    }

    public objUA(String nombre, String correo, String telefono, String contrasena, String edad) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }
}
