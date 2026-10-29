package com.cipa.nomina.empleado;

import com.cipa.nomina.util.Validaciones;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
public abstract class Empleado {

    private final String nombre;
    private final String documento;
    private final LocalDate fechaIngreso;

    protected Empleado(String nombre, String documento, LocalDate fechaIngreso) {
        this.nombre = Validaciones.texto(nombre, "El nombre");
        this.documento = Validaciones.texto(documento, "El documento");
        this.fechaIngreso = fechaIngreso;
    }

    public abstract TipoEmpleado getTipo();

    /** Devuelve {concepto: valor} con todo lo que el empleado devenga (salario bruto). */
    public abstract Map<String, BigDecimal> calcularDevengado(LocalDate fechaCalculo);

    /** Solo los empleados por horas pueden aceptar el fondo de ahorro; por defecto no. */
    public boolean aceptaFondoAhorro() {
        return false;
    }

    public boolean superaAntiguedad(int anios, LocalDate fechaCalculo) {
        return fechaCalculo.isAfter(fechaIngreso.plusYears(anios));
    }

    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
}
