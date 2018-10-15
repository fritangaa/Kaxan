package esteticaapp.co.kaxan.UA;

public class objUM {

    private String nombre;
    private String telefono;
    private String contrasena;
    private String edad;
    private String correo;

    public objUM() {
    }

    public objUM(String nombre, String telefono, String contrasena, String edad, String correo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.edad = edad;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
