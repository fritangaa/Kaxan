package esteticaapp.co.kaxan;

public class itemHistorial {

    private String hId;
    private String hDesc;
    private String hTiempo;
    private int hImagen;

    public itemHistorial() {
    }

    public itemHistorial(String mId,String hDesc, String hTiempo, int hImagen) {
        this.hId = mId;
        this.hDesc = hDesc;
        this.hTiempo = hTiempo;
        this.hImagen = hImagen;
    }

    public String gethId() {
        return hId;
    }

    public void sethId(String hId) {
        this.hId = hId;
    }

    public String gethDesc() {
        return hDesc;
    }

    public void sethDesc(String hDesc) {
        this.hDesc = hDesc;
    }

    public String gethTiempo() {
        return hTiempo;
    }

    public void sethTiempo(String hTiempo) {
        this.hTiempo = hTiempo;
    }

    public int gethImagen() {
        return hImagen;
    }

    public void sethImagen(int hImagen) {
        this.hImagen = hImagen;
    }



}
