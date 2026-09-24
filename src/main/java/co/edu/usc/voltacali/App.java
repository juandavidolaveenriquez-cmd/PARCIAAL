import java.util.Arrays;
import java.util.Locale;

public class App {

    // Dos últimos dígitos de la cédula, con ceros a la izquierda (terminada en 07 -> 7).
    // CAMBIA ESTE VALOR por el tuyo (también se puede pasar como argumento al ejecutar).
    private static final int ULTIMOS_DOS_DIGITOS = 47;

    // Imprime una línea con su código entre corchetes
    private static void sal(String codigo, String texto) {
        System.out.println("[" + codigo + "] " + texto);
    }

    // Hace que los mensajes internos de CargadorVE también salgan con el código
    private static void codigo(String codigo) {
        CargadorVE.setPrefijoSalida("[" + codigo + "] ");
    }

    private static void potencia(String codigo, String id, CargadorVE c) {
        sal(codigo, "Potencia " + id + ": " + c.getPotenciaActual() + " kW");
    }

    private static String dos(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    private static String fabricantes(CargadorVE[] arreglo) {
        if (arreglo.length == 0) return "ninguno";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arreglo.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arreglo[i].getFabricante());
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        // ===== Paso 1: crear la flota =====
        CargadorVE c1 = new CargadorVE("ABB", 2023, 400, CargadorVE.TipoConector.CCS2,
                CargadorVE.TipoCargador.RAPIDO_DC, 2, 2, 60, CargadorVE.Ubicacion.UNIVERSIDAD);
        CargadorVE c2 = new CargadorVE("Siemens", 2022, 220, CargadorVE.TipoConector.TIPO_2,
                CargadorVE.TipoCargador.MURAL, 1, 1, 22, CargadorVE.Ubicacion.CENTRO_COMERCIAL);
        CargadorVE c3 = new CargadorVE("Delta", 2024, 800, CargadorVE.TipoConector.CCS2,
                CargadorVE.TipoCargador.ULTRARRAPIDO, 2, 2, 150, CargadorVE.Ubicacion.ESTACION_SERVICIO);
        CargadorVE c4 = new CargadorVE("Wallbox", 2021, 220, CargadorVE.TipoConector.TIPO_2,
                CargadorVE.TipoCargador.MURAL, 1, 1, 11, CargadorVE.Ubicacion.RESIDENCIAL);
        CargadorVE c5 = new CargadorVE("Enel X", 2025, 22);   // constructor reducido
        CargadorVE[] flota = { c1, c2, c3, c4, c5 };

        // ===== Paso 2: sesión de carga sobre C1 =====
        codigo("P01"); c1.setPotenciaActual(40);      potencia("P01", "C1", c1);
        codigo("P02"); c1.aumentarPotencia(15);       potencia("P02", "C1", c1);
        codigo("P03");
        sal("P03", "Tiempo estimado (66 kWh): " + dos(c1.tiempoEstimadoCarga(66)) + " h");
        codigo("P04"); c1.aumentarPotencia(10);       potencia("P04", "C1", c1);   // rechazado
        codigo("P05"); c1.reducirPotencia(30);        potencia("P05", "C1", c1);
        codigo("P06");
        sal("P06", "Tiempo estimado (50 kWh, 2 pausas de 15 min): " + dos(c1.tiempoEstimadoCarga(50, 2, 15)) + " h");
        codigo("P07");
        sal("P07", "Tiempo estimado (50 kWh a 40.0 kW programados): " + dos(c1.tiempoEstimadoCarga(50, 40.0)) + " h");
        codigo("P08"); c1.aumentarPotencia();         potencia("P08", "C1", c1);
        codigo("P09"); c1.aumentarPotencia(5, 3);     potencia("P09", "C1", c1);
        codigo("P10"); c1.reducirPotencia(50);        potencia("P10", "C1", c1);   // rechazado
        codigo("P11"); c1.cortarCarga();              potencia("P11", "C1", c1);
        codigo("P12");
        sal("P12", "Tiempo estimado (10 kWh): " + dos(c1.tiempoEstimadoCarga(10)) + " h");

        // ===== Paso 3: resto de la flota =====
        codigo("P13");
        c2.setPotenciaActual(22);
        c3.setPotenciaActual(120);
        c4.aumentarPotencia(7.4);
        c5.aumentarPotencia(30);                      // rechazado (máx. 22)
        potencia("P13", "C2", c2);
        potencia("P13", "C3", c3);
        potencia("P13", "C4", c4);
        potencia("P13", "C5", c5);

        // ===== Paso 4: estadísticas y validaciones =====
        codigo("P14");
        int[] conteo = CargadorVE.contarPorTipo(flota);
        for (CargadorVE.TipoCargador t : CargadorVE.TipoCargador.values()) {
            sal("P14", t + ": " + conteo[t.ordinal()]);
        }

        sal("P15", "Promedio de potencia: " + dos(CargadorVE.promedioPotencia(flota)) + " kW");

        CargadorVE mayor = CargadorVE.mayorPotencia(flota);
        if (mayor == null) {
            sal("P16", "No hay cargadores");
        } else {
            sal("P16", "Mayor potencia: " + mayor.getFabricante() + " con " + mayor.getPotenciaActual() + " kW");
        }

        sal("P17", "Excesos de potencia contratada (registros válidos > " + CargadorVE.LIMITE_RED
                + " kW): " + CargadorVE.excesosDePotenciaContratada(flota));

        CargadorVE[] porConector = CargadorVE.filtrar(flota, CargadorVE.TipoConector.TIPO_2);
        CargadorVE[] porTipo = CargadorVE.filtrar(flota, CargadorVE.TipoCargador.MURAL);
        CargadorVE[] porUbicacion = CargadorVE.filtrar(flota, CargadorVE.Ubicacion.UNIVERSIDAD);
        sal("P18", "filtrar TIPO_2: " + porConector.length + " -> " + fabricantes(porConector));
        sal("P18", "filtrar MURAL: " + porTipo.length + " -> " + fabricantes(porTipo));
        sal("P18", "filtrar UNIVERSIDAD: " + porUbicacion.length + " -> " + fabricantes(porUbicacion));

        codigo("P19"); c5.mostrar(false);

        CargadorVE copia = new CargadorVE(c3);
        sal("P20", "Copia -> fabricante: " + copia.getFabricante()
                + ", potencia actual: " + copia.getPotenciaActual() + " kW"
                + ", tamaño de bitácora: " + copia.getBitacora().size());
        sal("P20", "getTotalCargadores() = " + CargadorVE.getTotalCargadores());

        codigo("P21"); c1.mostrar(true);

        sal("P22", "contadorRegistros = " + CargadorVE.getContadorRegistros());

        CargadorVE[] parcial = new CargadorVE[] { c1, null, c3 };
        sal("P23", "promedioPotencia({c1, null, c3}) = " + dos(CargadorVE.promedioPotencia(parcial)) + " kW");
        sal("P23", "contarPorTipo(null) = " + Arrays.toString(CargadorVE.contarPorTipo(null)));

        // ===== Ruta individual según la cédula =====
        CargadorVE.setPrefijoSalida("");
        int n = (args.length > 0) ? Integer.parseInt(args[0]) : ULTIMOS_DOS_DIGITOS;
        int r = n % 4;
        sal("R", "N = " + n + ", r = " + r);
        codigo("R");

        switch (r) {
            case 0: {
                int buscados = n % 3 + 1;
                CargadorVE[] res = CargadorVE.cargadoresPorConectores(flota, buscados);
                if (res.length == 0) {
                    sal("R", "No hay cargadores con " + buscados + " conector(es)");
                } else {
                    sal("R", "Cargadores con " + buscados + " conector(es): " + res.length
                            + " -> " + fabricantes(res));
                }
                break;
            }
            case 1: {
                CargadorVE.TipoConector conector = CargadorVE.TipoConector.values()[n % 5];
                double prom = CargadorVE.promedioVoltajePorConector(flota, conector);
                if (prom < 0) {
                    sal("R", "No hay cargadores con conector " + conector);
                } else {
                    sal("R", "Promedio de voltaje con conector " + conector + ": " + dos(prom) + " V");
                }
                break;
            }
            case 2: {
                CargadorVE.TipoCargador[] tipos = CargadorVE.tipoMasFrecuente(flota);
                if (tipos.length == 0) {
                    sal("R", "No hay cargadores en la flota");
                } else if (tipos.length == 1) {
                    sal("R", "Tipo más frecuente: " + tipos[0]);
                } else {
                    sal("R", "Empate entre " + tipos.length + " tipos: " + Arrays.toString(tipos));
                }
                break;
            }
            default: {
                int total = CargadorVE.sesionesSobreLimiteRed(flota);
                if (total == 0) {
                    sal("R", "No hay sesiones válidas sobre el límite de red");
                } else {
                    sal("R", "Total de sesiones sobre el límite de red: " + total);
                }
                break;
            }
        }
        CargadorVE.setPrefijoSalida("");
    }
}