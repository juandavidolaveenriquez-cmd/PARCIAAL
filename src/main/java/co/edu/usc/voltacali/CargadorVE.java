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
    public void setVoltajeNominal(int voltajeNominal) { this.voltajeNominal = voltajeNominal; }