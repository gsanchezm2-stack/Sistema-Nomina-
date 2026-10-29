package com.cipa.nomina;

import com.cipa.nomina.empleado.Empleado;
import com.cipa.nomina.empleado.EmpleadoAsalariado;
import com.cipa.nomina.empleado.EmpleadoPorComision;
import com.cipa.nomina.empleado.EmpleadoPorHoras;
import com.cipa.nomina.empleado.EmpleadoTemporal;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;


public class Main {

    private static final LocalDate FECHA = LocalDate.of(2026, 10, 28);

    public static void main(String[] args) {
        
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        List<Empleado> empleados = List.of(
                new EmpleadoAsalariado("Ana Pérez", "1001", LocalDate.of(2019, 3, 1),
                        new BigDecimal("3000000")),
                new EmpleadoPorHoras("Luis Gómez", "1002", LocalDate.of(2024, 1, 15),
                        new BigDecimal("10000"), new BigDecimal("45"), true),
                new EmpleadoPorComision("Marta Ríos", "1003", LocalDate.of(2022, 6, 1),
                        new BigDecimal("2000000"), new BigDecimal("0.05"), new BigDecimal("30000000")),
                new EmpleadoTemporal("Carlos Díaz", "1004", LocalDate.of(2026, 9, 1),
                        new BigDecimal("1500000"), LocalDate.of(2027, 2, 28)));

        ServicioNomina servicio = ServicioNomina.conReglasPorDefecto();
        servicio.liquidarTodos(empleados, FECHA)
                .forEach(recibo -> System.out.println(FormatoRecibo.formatear(recibo)));
    }
}
