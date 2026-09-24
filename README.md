## Proyecto CargadorVE

## Extensión personalizada: cargador C6

Valores calculados para **N = 47** (d1 = 4, d2 = 7).
Si tu cédula termina en otros dígitos, cambia `ULTIMOS_DOS_DIGITOS` en `App.java` y actualiza la columna "Valor".

| Atributo | Regla | Valor |
|---|---|---|
| fabricante | `"USC-" + N` | USC-47 |
| anioInstalacion | 2015 + d2 | 2022 |
| voltajeNominal | 220 si N es par; 400 si es impar | 400 V |
| tipoConector | `TipoConector.values()[N % 5]` | CCS2 |
| tipoCargador | `TipoCargador.values()[N % 6]` | BIDIRECCIONAL_V2G |
| numeroConectores | d1 % 3 + 1 | 2 |
| puestosParqueo | d2 % 4 + 1 | 4 |
| potenciaMaxima | 20 + N (kW) | 67 kW |
| ubicacion | `Ubicacion.values()[N % 8]` | FLOTA_CORPORATIVA |

## Cómo ejecutar

```
javac -encoding UTF-8 CargadorVE.java App.java
java App          # usa ULTIMOS_DOS_DIGITOS
java App 47       # o pasa N como argumento
```