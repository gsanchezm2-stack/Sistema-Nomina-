package com.cipa.nomina.deduccion;

import java.math.BigDecimal;
public interface Deduccion {

    String nombre();

    BigDecimal calcular(BigDecimal salarioBruto);
}
