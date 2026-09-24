import java.util.ArrayList;
import java.util.List;

enum Tipoconector {
    TIPO1, TIPO2,  tipo3, CCS, CHAdeMO, GBT
}
enum Tipocargador {
    RAPIDO, LENTO, SEMIRAPIDO, ULTRARAPIDO
}
enum Ubicacion {
    CENTRO_COMERCIAL, PARQUEADERO_PUBLICO, ESTACION_SERVICIO, RESIDENCIAL, EMPRESARIAL

}
public class CargadorVE {
 
    // Atributos (todos private)
    private String fabricante;
    private int anioInstalacion;
    private int voltajeNominal;
    private TipoConector tipoConector;
    private TipoCargador tipoCargador;
    private int numeroConectores;
    private int puestosParqueo;
    private double potenciaMaxima;
    private Ubicacion ubicacion;
    private double potenciaActual;
 
    // Bitácora de intentos inválidos
    private final List<String> bitacora = new ArrayList<>();
 
    public CargadorVE(String fabricante, int anioInstalacion, int voltajeNominal,
                      TipoConector tipoConector, TipoCargador tipoCargador,
                      int numeroConectores, int puestosParqueo,
                      double potenciaMaxima, Ubicacion ubicacion) {
        this.fabricante = fabricante;
        this.anioInstalacion = anioInstalacion;
        this.voltajeNominal = voltajeNominal;
        this.tipoConector = tipoConector;
        this.tipoCargador = tipoCargador;
        this.numeroConectores = numeroConectores;
        this.puestosParqueo = puestosParqueo;
        this.potenciaMaxima = potenciaMaxima;
        this.ubicacion = ubicacion;
        this.potenciaActual = 0.0;
    }
   public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
 
    public int getAnioInstalacion() { return anioInstalacion; }
    public void setAnioInstalacion(int anioInstalacion) { this.anioInstalacion = anioInstalacion; }
 
    public int getVoltajeNominal() { return voltajeNominal; }
    public void setVoltajeNominal(int voltajeNominal) { this.voltajeNominal = voltajeNominal; } //olaA
     public TipoConector getTipoConector() { return tipoConector; }
    public void setTipoConector(TipoConector tipoConector) { this.tipoConector = tipoConector; }
 
    public TipoCargador getTipoCargador() { return tipoCargador; }
    public void setTipoCargador(TipoCargador tipoCargador) { this.tipoCargador = tipoCargador; }
 
    public int getNumeroConectores() { return numeroConectores; }
    public void setNumeroConectores(int numeroConectores) { this.numeroConectores = numeroConectores; }
 
    public int getPuestosParqueo() { return puestosParqueo; }
    public void setPuestosParqueo(int puestosParqueo) { this.puestosParqueo = puestosParqueo; }
 
    public double getPotenciaMaxima() { return potenciaMaxima; }
    public void setPotenciaMaxima(double potenciaMaxima) { this.potenciaMaxima = potenciaMaxima; }
    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }
 
    public double getPotenciaActual() { return potenciaActual; }
 
    /**
     * Rechaza valores negativos o mayores que potenciaMaxima:
     * muestra un mensaje, no cambia el estado y registra el intento en la bitácora.
     */
    public void setPotenciaActual(double potenciaActual) {
        if (potenciaActual < 0 || potenciaActual > potenciaMaxima) {
            String mensaje = "Potencia inválida: " + potenciaActual
                    + " kW (debe estar entre 0 y " + potenciaMaxima + " kW)";
            System.out.println(mensaje);
            bitacora.add(mensaje);
            return;
        }
        this.potenciaActual = potenciaActual;
    }
 
    public List<String> getBitacora() {
        return new ArrayList<>(bitacora); // copia para no exponer la lista interna
    }
 
    // Prueba rápida
    public static void main(String[] args) {
        CargadorVE c = new CargadorVE("ABB", 2024, 400, TipoConector.CCS,
                TipoCargador.RAPIDO, 2, 4, 50.0, Ubicacion.CENTRO_COMERCIAL);
        c.setPotenciaActual(30);   // válido
        c.setPotenciaActual(-5);   // rechazado
        c.setPotenciaActual(80);   // rechazado
        System.out.println("Potencia actual: " + c.getPotenciaActual());
        System.out.println("Bitácora: " + c.getBitacora());
    }
}