package commands;

/** Divide el acumulador entre un operando */
public class ComandoDivision implements Comando {
    private final double operando;

    public ComandoDivision(double operando) {
        if (operando == 0) throw new ArithmeticException("División por cero no permitida");
        this.operando = operando;
    }

    @Override public double ejecutar(double v) { return v / operando; }
    @Override public String getDescripcion()   { return "÷ " + fmt(operando); }

    private String fmt(double n) {
        return (n == Math.floor(n) && !Double.isInfinite(n)) ? String.valueOf((long) n) : String.valueOf(n);
    }
}
