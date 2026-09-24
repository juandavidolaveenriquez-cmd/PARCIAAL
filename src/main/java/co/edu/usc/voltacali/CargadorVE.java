import java.util.ArrayList;
import java.util.List;

// Enums de apoyo (ajusta los valores según tu guía)
enum TipoConector { TIPO_1, TIPO_2, TIPO_3, CCS, CHADEMO, GBT }

enum TipoCargador { PEDESTAL, MURAL, LENTO, SEMIRAPIDO, RAPIDO, ULTRARAPIDO }

enum Ubicacion { CENTRO_COMERCIAL, PARQUEADERO_PUBLICO, ESTACION_SERVICIO, RESIDENCIAL, EMPRESARIAL }

public class CargadorVE {

    public static final double INCREMENTO_DEFECTO = 1.0; // kW

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

    // Bitácora de intentos inválidos (cada objeto tiene la suya)
    private final List<String> bitacora = new ArrayList<>();

    // ===== Parte C: constructores sobrecargados =====

    // Completo: todo menos potenciaActual, que inicia en 0
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

    // Reducido: valores por defecto vía this(...)
    public CargadorVE(String fabricante, int anioInstalacion, double potenciaMaxima) {
        this(fabricante, anioInstalacion, 220, TipoConector.TIPO_2, TipoCargador.PEDESTAL,
                1, 1, potenciaMaxima, Ubicacion.PARQUEADERO_PUBLICO);
    }

    // Copia: duplica características técnicas, potencia en 0 y bitácora nueva y vacía
    public CargadorVE(CargadorVE otro) {
        this(otro.fabricante, otro.anioInstalacion, otro.voltajeNominal, otro.tipoConector,
                otro.tipoCargador, otro.numeroConectores, otro.puestosParqueo,
                otro.potenciaMaxima, otro.ubicacion);
    }

    // ===== Getters y setters =====
    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public int getAnioInstalacion() { return anioInstalacion; }
    public void setAnioInstalacion(int anioInstalacion) { this.anioInstalacion = anioInstalacion; }

    public int getVoltajeNominal() { return voltajeNominal; }
    public void setVoltajeNominal(int voltajeNominal) { this.voltajeNominal = voltajeNominal; }

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

    // ===== Parte B + C: aumentarPotencia =====

    // Aplica un paso; retorna true si fue válido
    private boolean intentarAumento(double incremento) {
        double resultado = potenciaActual + incremento;
        if (resultado > potenciaMaxima || resultado < 0) {
            System.out.println("No se puede aumentar: el resultado (" + resultado
                    + " kW) queda fuera del rango 0 - " + potenciaMaxima + " kW");
            return false;
        }
        potenciaActual = resultado;
        return true;
    }

    public void aumentarPotencia() {
        aumentarPotencia(INCREMENTO_DEFECTO);
    }

    public void aumentarPotencia(double incremento) {
        intentarAumento(incremento);
    }

    // Paso a paso: si un paso no es válido, se detiene
    public void aumentarPotencia(double incremento, int veces) {
        for (int i = 0; i < veces; i++) {
            if (!intentarAumento(incremento)) {
                break;
            }
        }
    }

    public void reducirPotencia(double decremento) {
        double resultado = potenciaActual - decremento;
        if (resultado > potenciaMaxima || resultado < 0) {
            System.out.println("No se puede reducir: el resultado (" + resultado
                    + " kW) queda fuera del rango 0 - " + potenciaMaxima + " kW");
            return;
        }
        potenciaActual = resultado;
    }

    public void cortarCarga() {
        potenciaActual = 0;
    }

    // ===== Parte B + C: tiempoEstimadoCarga =====

    /** Horas = energía (kWh) / potenciaActual (kW); -1 si la potencia es 0. */
    public double tiempoEstimadoCarga(double energiaKWh) {
        if (potenciaActual == 0) {
            System.out.println("No hay carga en curso: la potencia actual es 0 kW");
            return -1;
        }
        return energiaKWh / potenciaActual;
    }

    /** Usa la potencia programada en lugar de la actual. */
    public double tiempoEstimadoCarga(double energiaKWh, double potenciaProgramada) {
        if (potenciaProgramada <= 0) {
            System.out.println("La potencia programada debe ser mayor que 0 kW");
            return -1;
        }
        return energiaKWh / potenciaProgramada;
    }

    /** Tiempo con la potencia actual más el tiempo de las pausas (en horas). */
    public double tiempoEstimadoCarga(double energiaKWh, int pausas, double minutosPorPausa) {
        double horasCarga = tiempoEstimadoCarga(energiaKWh);
        if (horasCarga == -1) {
            return -1;
        }
        return horasCarga + (pausas * minutosPorPausa) / 60.0;
    }

    // ===== Parte C: filtrar (estático) =====

    public static CargadorVE[] filtrar(CargadorVE[] cargadores, TipoConector tipo) {
        int n = 0;
        for (CargadorVE c : cargadores) {
            if (c != null && c.tipoConector == tipo) n++;
        }
        CargadorVE[] resultado = new CargadorVE[n];
        int i = 0;
        for (CargadorVE c : cargadores) {
            if (c != null && c.tipoConector == tipo) resultado[i++] = c;
        }
        return resultado;
    }

    public static CargadorVE[] filtrar(CargadorVE[] cargadores, TipoCargador tipo) {
        int n = 0;
        for (CargadorVE c : cargadores) {
            if (c != null && c.tipoCargador == tipo) n++;
        }
        CargadorVE[] resultado = new CargadorVE[n];
        int i = 0;
        for (CargadorVE c : cargadores) {
            if (c != null && c.tipoCargador == tipo) resultado[i++] = c;
        }
        return resultado;
    }

    public static CargadorVE[] filtrar(CargadorVE[] cargadores, Ubicacion ubicacion) {
        int n = 0;
        for (CargadorVE c : cargadores) {
            if (c != null && c.ubicacion == ubicacion) n++;
        }
        CargadorVE[] resultado = new CargadorVE[n];
        int i = 0;
        for (CargadorVE c : cargadores) {
            if (c != null && c.ubicacion == ubicacion) resultado[i++] = c;
        }
        return resultado;
    }

    // ===== Parte B + C: mostrar =====

    public void mostrar() {
        mostrar(false);
    }

    public void mostrar(boolean detallado) {
        System.out.println("Fabricante: " + fabricante);
        System.out.println("Año de instalación: " + anioInstalacion);
        System.out.println("Voltaje nominal: " + voltajeNominal + " V");
        System.out.println("Tipo de conector: " + tipoConector);
        System.out.println("Tipo de cargador: " + tipoCargador);
        System.out.println("Número de conectores: " + numeroConectores);
        System.out.println("Puestos de parqueo: " + puestosParqueo);
        System.out.println("Potencia máxima: " + potenciaMaxima + " kW");
        System.out.println("Ubicación: " + ubicacion);
        System.out.println("Potencia actual: " + potenciaActual + " kW");
        if (detallado) {
            System.out.println("Bitácora (" + bitacora.size() + " registros):");
            for (String registro : bitacora) {
                System.out.println("  - " + registro);
            }
        }
    }

    // ===== Prueba rápida =====
    public static void main(String[] args) {
        CargadorVE a = new CargadorVE("ABB", 2024, 400, TipoConector.CCS,
                TipoCargador.RAPIDO, 2, 4, 50.0, Ubicacion.CENTRO_COMERCIAL);
        CargadorVE b = new CargadorVE("Enel", 2023, 22.0);   // reducido
        a.setPotenciaActual(30);
        a.setPotenciaActual(80);                              // rechazado y a la bitácora
        CargadorVE copia = new CargadorVE(a);                 // potencia 0, bitácora vacía

        a.aumentarPotencia();                                 // +1 -> 31
        a.aumentarPotencia(5);                                // 36
        a.aumentarPotencia(5, 5);                             // 41, 46, 51 rechazado -> se detiene
        System.out.println("Potencia a: " + a.getPotenciaActual());

        System.out.println("Horas (actual): " + a.tiempoEstimadoCarga(92));
        System.out.println("Horas (programada 23 kW): " + a.tiempoEstimadoCarga(92, 23));
        System.out.println("Horas (2 pausas de 15 min): " + a.tiempoEstimadoCarga(92, 2, 15));

        CargadorVE[] todos = { a, b, copia };
        System.out.println("Con CCS: " + filtrar(todos, TipoConector.CCS).length);
        System.out.println("Pedestal: " + filtrar(todos, TipoCargador.PEDESTAL).length);
        System.out.println("Parqueadero público: " + filtrar(todos, Ubicacion.PARQUEADERO_PUBLICO).length);

        a.mostrar();
        System.out.println("---");
        a.mostrar(true);
        System.out.println("--- copia (bitácora vacía) ---");
        copia.mostrar(true);
    }
}