public class NominaV1 {
    static double calcularNomina(String tipo, double salario, double horas, double tarifa,
                                 double ventas, int anios) {
        double bruto, neto;
        if (tipo.equals("asalariado")) {
            bruto = salario;
            if (anios > 5) { bruto = bruto + salario * 0.1; }
            neto = bruto - bruto * 0.04 - bruto * 0.00522 + 1000000;
        } else if (tipo.equals("horas")) {
            if (horas > 40) { bruto = 40 * tarifa + (horas - 40) * tarifa * 1.5; }
            else { bruto = horas * tarifa; }
            neto = bruto - bruto * 0.04 - bruto * 0.00522;
        } else if (tipo.equals("comision")) {
            bruto = salario + ventas * 0.05;
            if (ventas > 20000000) { bruto = bruto + ventas * 0.03; }
            neto = bruto - bruto * 0.04 - bruto * 0.00522 + 1000000;
        } else {
            bruto = salario;
            neto = bruto - bruto * 0.04 - bruto * 0.00522;
        }
        return neto;
    }
}
