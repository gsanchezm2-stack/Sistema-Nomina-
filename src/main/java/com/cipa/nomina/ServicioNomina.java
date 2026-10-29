package com.cipa.nomina;

import com.cipa.nomina.beneficio.Beneficio;
import com.cipa.nomina.beneficio.BonoAlimentacion;
import com.cipa.nomina.beneficio.FondoAhorro;
import com.cipa.nomina.deduccion.Arl;
import com.cipa.nomina.deduccion.Deduccion;
import com.cipa.nomina.deduccion.SeguroSocialPension;
import com.cipa.nomina.empleado.Empleado;
import com.cipa.nomina.excepcion.SalarioNetoNegativoException;
import com.cipa.nomina.recibo.ReciboNomina;
import com.cipa.nomina.util.Dinero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ServicioNomina {

    private final List<Deduccion> deducciones;
    private final List<Beneficio> beneficios;

    public ServicioNomina(List<Deduccion> deducciones, List<Beneficio> beneficios) {
        this.deducciones = List.copyOf(deducciones);
        this.beneficios = List.copyOf(beneficios);
    }
    public static ServicioNomina conReglasPorDefecto() {
        return new ServicioNomina(
                List.of(new SeguroSocialPension(), new Arl()),
                List.of(new BonoAlimentacion(), new FondoAhorro()));
    }

    public ReciboNomina liquidar(Empleado empleado, LocalDate fechaCalculo) {
        Map<String, BigDecimal> devengado = empleado.calcularDevengado(fechaCalculo);
        BigDecimal salarioBruto = sumar(devengado);

        Map<String, BigDecimal> descuentos = new LinkedHashMap<>();
        for (Deduccion deduccion : deducciones) {
            descuentos.put(deduccion.nombre(), deduccion.calcular(salarioBruto));
        }

        Map<String, BigDecimal> alEmpleado = new LinkedHashMap<>();
        Map<String, BigDecimal> aFondos = new LinkedHashMap<>();
        for (Beneficio beneficio : beneficios) {
            if (beneficio.aplica(empleado, fechaCalculo)) {
                Map<String, BigDecimal> destino = beneficio.sePagaAlEmpleado() ? alEmpleado : aFondos;
                destino.put(beneficio.nombre(), beneficio.calcular(salarioBruto));
            }
        }

        BigDecimal salarioNeto = Dinero.redondear(
                salarioBruto.subtract(sumar(descuentos)).add(sumar(alEmpleado)));
        if (salarioNeto.signum() < 0) {
            throw new SalarioNetoNegativoException(
                    "El salario neto de " + empleado.getNombre() + " sería negativo (" + salarioNeto + ").");
        }

        return new ReciboNomina(empleado.getNombre(), empleado.getDocumento(), empleado.getTipo(),
                fechaCalculo, devengado, salarioBruto, descuentos, alEmpleado, aFondos, salarioNeto);
    }

    public List<ReciboNomina> liquidarTodos(List<? extends Empleado> empleados, LocalDate fechaCalculo) {
        return empleados.stream().map(e -> liquidar(e, fechaCalculo)).toList();
    }

    private static BigDecimal sumar(Map<String, BigDecimal> valores) {
        return valores.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
