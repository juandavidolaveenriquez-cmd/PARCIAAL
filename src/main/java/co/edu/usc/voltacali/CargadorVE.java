import java.util.Vector;

public class CargadorVE {

    // ===== D-a) Enums anidados (en el orden exacto pedido) =====
    public enum TipoConector { TIPO_1, TIPO_2, CCS2, CHADEMO, GBT }

    public enum TipoCargador { MURAL, PEDESTAL, RAPIDO_DC, ULTRARRAPIDO, PORTATIL, BIDIRECCIONAL_V2G }

    public enum Ubicacion { CENTRO_COMERCIAL, UNIVERSIDAD, ESTACION_SERVICIO, PARQUEADERO_PUBLICO,
                            RESIDENCIAL, HOTEL, TERMINAL, FLOTA_CORPORATIVA }

    // ===== D-e) Miembros estáticos =====
    private static int totalCargadores = 0;
    private static int contadorRegistros = 0;
    public static final double LIMITE_RED = 50.0;
    public static final double INCREMENTO_DEFECTO = 5.0;

    // ===== Atributos (Parte A) =====
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

    // ===== D-c) Bitácora: cada cargador tiene su Vector<RegistroSesion> =====
    private final Vector<RegistroSesion> bitacora = new Vector<>();

    // ===== D-b) Clase interna NO estática =====
    public class RegistroSesion {
        private final int numero;
        private final String evento;
        private final boolean valido;
        private final String fabricante;
        private final int anioInstalacion;
        private final double potencia;

        public RegistroSesion(String evento, boolean valido) {
            this.evento = evento;
            this.valido = valido;
            // Captura del objeto externo, sin recibirlos por parámetro
            this.fabricante = CargadorVE.this.fabricante;
            this.anioInstalacion = CargadorVE.this.anioInstalacion;
            this.potencia = CargadorVE.this.potenciaActual;
            // Número consecutivo global
            this.numero = ++contadorRegistros;
        }

        public boolean isValido() { return valido; }

        public double getPotencia() { return potencia; }

        public String describir() {
            return "#" + numero + " [" + (valido ? "VÁLIDO" : "INVÁLIDO") + "] " + evento
                    + " | " + fabricante + " (" + anioInstalacion + ")"
                    + " | potencia: " + potencia + " kW";
        }
    }

    private void registrar(String evento, boolean valido) {
        bitacora.add(new RegistroSesion(evento, valido));
    }

    // ===== Parte C: constructores =====

    // Completo (único que incrementa totalCargadores; los demás delegan con this(...))
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
        totalCargadores++;
    }

    // Reducido
    public CargadorVE(String fabricante, int anioInstalacion, double potenciaMaxima) {
        this(fabricante, anioInstalacion, 220, TipoConector.TIPO_2, TipoCargador.PEDESTAL,
                1, 1, potenciaMaxima, Ubicacion.PARQUEADERO_PUBLICO);
    }

    // Copia: potencia en 0 y bitácora nueva y vacía
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

    /** Rechaza negativos o mayores que potenciaMaxima: mensaje, sin cambio de estado y evento inválido. */
    public void setPotenciaActual(double potenciaActual) {
        if (potenciaActual < 0 || potenciaActual > potenciaMaxima) {
            mensaje("Potencia inválida: " + potenciaActual
                    + " kW (debe estar entre 0 y " + potenciaMaxima + " kW)");
            registrar("setPotenciaActual rechazado: " + potenciaActual + " kW", false);
            return;
        }
        this.potenciaActual = potenciaActual;
        registrar("setPotenciaActual: " + potenciaActual + " kW", true);
    }

    public Vector<RegistroSesion> getBitacora() {
        return new Vector<>(bitacora); // copia para no exponer la interna
    }

    // ===== aumentarPotencia (familia) =====

    // Aplica un paso; registra el evento; retorna true si fue válido
    private boolean intentarAumento(double incremento) {
        double resultado = potenciaActual + incremento;
        if (resultado > potenciaMaxima || resultado < 0) {
            mensaje("No se puede aumentar: el resultado (" + resultado
                    + " kW) queda fuera del rango 0 - " + potenciaMaxima + " kW");
            registrar("aumentarPotencia rechazado: +" + incremento + " kW", false);
            return false;
        }
        potenciaActual = resultado;
        registrar("aumentarPotencia: +" + incremento + " kW", true);
        return true;
    }

    public void aumentarPotencia() {
        aumentarPotencia(INCREMENTO_DEFECTO);
    }

    public void aumentarPotencia(double incremento) {
        intentarAumento(incremento);
    }

    // Paso a paso; si un paso no es válido, se detiene
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
            mensaje("No se puede reducir: el resultado (" + resultado
                    + " kW) queda fuera del rango 0 - " + potenciaMaxima + " kW");
            registrar("reducirPotencia rechazado: -" + decremento + " kW", false);
            return;
        }
        potenciaActual = resultado;
        registrar("reducirPotencia: -" + decremento + " kW", true);
    }

    public void cortarCarga() {
        potenciaActual = 0;
        registrar("cortarCarga", true);
    }

    // ===== tiempoEstimadoCarga (familia; no genera registros) =====

    public double tiempoEstimadoCarga(double energiaKWh) {
        if (potenciaActual == 0) {
            mensaje("No hay carga en curso: la potencia actual es 0 kW");
            return -1;
        }
        return energiaKWh / potenciaActual;
    }

    public double tiempoEstimadoCarga(double energiaKWh, double potenciaProgramada) {
        if (potenciaProgramada <= 0) {
            mensaje("La potencia programada debe ser mayor que 0 kW");
            return -1;
        }
        return energiaKWh / potenciaProgramada;
    }

    public double tiempoEstimadoCarga(double energiaKWh, int pausas, double minutosPorPausa) {
        double horasCarga = tiempoEstimadoCarga(energiaKWh);
        if (horasCarga == -1) {
            return -1;
        }
        return horasCarga + (pausas * minutosPorPausa) / 60.0;
    }

    // ===== filtrar (estático; ignora null y arreglo null) =====

    public static CargadorVE[] filtrar(CargadorVE[] cargadores, TipoConector tipo) {
        if (cargadores == null) return new CargadorVE[0];
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
        if (cargadores == null) return new CargadorVE[0];
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
        if (cargadores == null) return new CargadorVE[0];
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

    // ===== D-d) y D-e) Métodos estáticos con arreglos =====

    public static int getTotalCargadores() {
        return totalCargadores;
    }

    public static int getContadorRegistros() {
        return contadorRegistros;
    }

    // Prefijo opcional (por ejemplo "[P02] ") que App antepone a cada línea de salida
    private static String prefijoSalida = "";

    public static void setPrefijoSalida(String prefijo) {
        prefijoSalida = (prefijo == null) ? "" : prefijo;
    }

    private static void mensaje(String texto) {
        System.out.println(prefijoSalida + texto);
    }

    /** Conteo por tipo de cargador, indexado con TipoCargador.ordinal(). */
    public static int[] contarPorTipo(CargadorVE[] cargadores) {
        int[] conteo = new int[TipoCargador.values().length];
        if (cargadores == null) return conteo;
        for (CargadorVE c : cargadores) {
            if (c != null && c.tipoCargador != null) {
                conteo[c.tipoCargador.ordinal()]++;
            }
        }
        return conteo;
    }

    /** Cargador con mayor potenciaActual; null si no hay cargadores. Ignora posiciones null. */
    public static CargadorVE mayorPotencia(CargadorVE[] cargadores) {
        if (cargadores == null) return null;
        CargadorVE mayor = null;
        for (CargadorVE c : cargadores) {
            if (c != null && (mayor == null || c.potenciaActual > mayor.potenciaActual)) {
                mayor = c;
            }
        }
        return mayor;
    }

    /** Promedio de potenciaActual (kW); 0 si no hay cargadores. */
    public static double promedioPotencia(CargadorVE[] cargadores) {
        if (cargadores == null) return 0;
        double suma = 0;
        int n = 0;
        for (CargadorVE c : cargadores) {
            if (c != null) {
                suma += c.potenciaActual;
                n++;
            }
        }
        return n == 0 ? 0 : suma / n;
    }

    /** Registros VÁLIDOS de todas las bitácoras cuya potencia supera LIMITE_RED. */
    public static int excesosDePotenciaContratada(CargadorVE[] cargadores) {
        int excesos = 0;
        if (cargadores == null) return excesos;
        for (CargadorVE c : cargadores) {
            if (c == null) continue;
            for (RegistroSesion r : c.bitacora) {
                if (r.valido && r.potencia > LIMITE_RED) {
                    excesos++;
                }
            }
        }
        return excesos;
    }

    // ===== Ruta individual según la cédula =====

    /** Nuevo arreglo con los cargadores que tienen exactamente 'conectores' conectores. */
    public static CargadorVE[] cargadoresPorConectores(CargadorVE[] flota, int conectores) {
        if (flota == null) return new CargadorVE[0];
        int n = 0;
        for (CargadorVE c : flota) {
            if (c != null && c.numeroConectores == conectores) n++;
        }
        CargadorVE[] resultado = new CargadorVE[n];
        int i = 0;
        for (CargadorVE c : flota) {
            if (c != null && c.numeroConectores == conectores) resultado[i++] = c;
        }
        return resultado;
    }

    /** Promedio del voltaje nominal (V) de los cargadores con ese conector; -1 si no hay ninguno. */
    public static double promedioVoltajePorConector(CargadorVE[] flota, TipoConector conector) {
        if (flota == null) return -1;
        double suma = 0;
        int n = 0;
        for (CargadorVE c : flota) {
            if (c != null && c.tipoConector == conector) {
                suma += c.voltajeNominal;
                n++;
            }
        }
        return n == 0 ? -1 : suma / n;
    }

    /** Tipos de cargador con más unidades (todos los empatados); arreglo vacío si no hay cargadores. */
    public static TipoCargador[] tipoMasFrecuente(CargadorVE[] flota) {
        int[] conteo = contarPorTipo(flota);
        int max = 0;
        for (int v : conteo) {
            if (v > max) max = v;
        }
        if (max == 0) return new TipoCargador[0];
        int n = 0;
        for (int v : conteo) {
            if (v == max) n++;
        }
        TipoCargador[] tipos = new TipoCargador[n];
        int i = 0;
        for (TipoCargador t : TipoCargador.values()) {
            if (conteo[t.ordinal()] == max) tipos[i++] = t;
        }
        return tipos;
    }

    /** Muestra los registros válidos cuya potencia supera LIMITE_RED; retorna cuántos hubo. */
    public static int sesionesSobreLimiteRed(CargadorVE[] flota) {
        if (flota == null) return 0;
        int total = 0;
        for (CargadorVE c : flota) {
            if (c == null) continue;
            for (RegistroSesion r : c.bitacora) {
                if (r.valido && r.potencia > LIMITE_RED) {
                    mensaje(r.describir());
                    total++;
                }
            }
        }
        return total;
    }

    // ===== mostrar =====

    public void mostrar() {
        mostrar(false);
    }

    public void mostrar(boolean detallado) {
        mensaje("Fabricante: " + fabricante);
        mensaje("Año de instalación: " + anioInstalacion);
        mensaje("Voltaje nominal: " + voltajeNominal + " V");
        mensaje("Tipo de conector: " + tipoConector);
        mensaje("Tipo de cargador: " + tipoCargador);
        mensaje("Número de conectores: " + numeroConectores);
        mensaje("Puestos de parqueo: " + puestosParqueo);
        mensaje("Potencia máxima: " + potenciaMaxima + " kW");
        mensaje("Ubicación: " + ubicacion);
        mensaje("Potencia actual: " + potenciaActual + " kW");
        if (detallado) {
            mensaje("Bitácora (" + bitacora.size() + " registros):");
            for (RegistroSesion registro : bitacora) {
                mensaje("  " + registro.describir());
            }
        }
    }
}