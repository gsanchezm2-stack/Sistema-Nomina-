package com.cipa.nomina.beneficio;

import com.cipa.nomina.config.Constantes;
import com.cipa.nomina.empleado.Empleado;
import com.cipa.nomina.empleado.TipoEmpleado;
import com.cipa.nomina.util.Dinero;
import com.cipa.nomina.util.Validaciones;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;
public class FondoAhorro implements Beneficio {

    private final BigDecimal porcentaje;
    private final int aniosMinimos;
    private final Set<TipoEmpleado> tipos;

    public FondoAhorro() {
        this(Constantes.PORCENTAJE_FONDO_AHORRO, Constantes.ANIOS_MINIMOS_FONDO_AHORRO,
                EnumSet.of(TipoEmpleado.POR_HORAS));
    }

    public FondoAhorro(BigDecimal porcentaje, int aniosMinimos, Set<TipoEmpleado> tipos) {
        this.porcentaje = Validaciones.porcentaje(porcentaje, "El porcentaje del fondo de ahorro");
        this.aniosMinimos = aniosMinimos;
        this.tipos = Set.copyOf(tipos);
    }

    @Override
    public String nombre() {
        return "Fondo de ahorro";
    }

    @Override
    public boolean aplica(Empleado empleado, LocalDate fechaCalculo) {
        return tipos.contains(empleado.getTipo())
                && empleado.aceptaFondoAhorro()
                && empleado.superaAntiguedad(aniosMinimos, fechaCalculo);
    }

    @Override
    public BigDecimal calcular(BigDecimal salarioBruto) {
        return Dinero.redondear(salarioBruto.multiply(porcentaje));
    }

    @Override
    public boolean sePagaAlEmpleado() {
        return false;
    }
}
