package esteticaapp.co.kaxan.UA;

public class UsuarioGeneral {

    private String nombre;
    private String apellidos;
    private String fecha_nacimiento;
    private String edad;
    private String telefono;
    private String correo;
    private String contrasena;
    private int foto;

    public UsuarioGeneral(String nombre, String apellidos, String fecha_nacimiento, String edad, String telefono, String correo, String contrasena, int foto) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fecha_nacimiento = fecha_nacimiento;
        this.edad = edad;
        this.telefono = telefono;
        this.correo = correo;
        this.contrasena = contrasena;
        this.foto = foto;
    }

    public UsuarioGeneral(String nombre, String apellidos, String fecha_nacimiento, String telefono, String correo, int foto) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fecha_nacimiento = fecha_nacimiento;
        this.telefono = telefono;
        this.correo = correo;
        this.foto = foto;
    }

    public UsuarioGeneral(String nombre, int foto) {
        this.nombre = nombre;
        this.foto = foto;
    }

    public UsuarioGeneral(String nombre) {
        this.nombre = nombre;
    }

    public UsuarioGeneral() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
