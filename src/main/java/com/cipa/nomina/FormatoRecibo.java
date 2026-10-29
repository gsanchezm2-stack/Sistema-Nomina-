package com.cipa.nomina;

import com.cipa.nomina.recibo.ReciboNomina;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public final class FormatoRecibo {

    private static final String LINEA_DOBLE = "=".repeat(52);
    private static final String LINEA_SIMPLE = "-".repeat(52);

    private FormatoRecibo() { }

    /** 1234567.5 -> $1.234.567,50 ; -500 -> -$500,00 */
    public static String moneda(BigDecimal valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator('.');
        simbolos.setDecimalSeparator(',');
        String texto = new DecimalFormat("#,##0.00", simbolos).format(valor.abs());
        return (valor.signum() < 0 ? "-$" : "$") + texto;
    }

    public static String formatear(ReciboNomina r) {
        List<String> lineas = new ArrayList<>();
        lineas.add(LINEA_DOBLE);
        lineas.add(r.nombre() + " (" + r.tipo().getEtiqueta() + ") - Doc. " + r.documento());
        lineas.add("Fecha de liquidación: " + r.fechaCalculo());
        lineas.add(LINEA_SIMPLE);

        agregarSeccion(lineas, "DEVENGADO", r.devengado(), false);
        lineas.add(String.format("%-30s%20s", "SALARIO BRUTO", moneda(r.salarioBruto())));
        agregarSeccion(lineas, "DEDUCCIONES", r.deducciones(), true);
        agregarSeccion(lineas, "BENEFICIOS (al empleado)", r.beneficios(), false);
        agregarSeccion(lineas, "APORTES A FONDOS (no suman al neto)", r.aportes(), false);

        lineas.add(LINEA_SIMPLE);
        lineas.add(String.format("%-30s%20s", "SALARIO NETO", moneda(r.salarioNeto())));
        lineas.add(LINEA_DOBLE);
        return String.join(System.lineSeparator(), lineas);
    }

    private static void agregarSeccion(List<String> lineas, String titulo,
                                       Map<String, BigDecimal> datos, boolean negativos) {
        if (datos.isEmpty()) {
            return;
        }
        lineas.add(titulo);
        datos.forEach((concepto, valor) -> lineas.add(
                String.format("  %-28s%20s", concepto, moneda(negativos ? valor.negate() : valor))));
    }
}
