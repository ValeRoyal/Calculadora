package commands;

/** Multiplica el acumulador por un operando */
public class ComandoMultiplicacion implements Comando {
    private final double operando;
    public ComandoMultiplicacion(double operando) { this.operando = operando; }

    @Override public double ejecutar(double v) { return v * operando; }
    @Override public String getDescripcion()   { return "× " + fmt(operando); }

    private String fmt(double n) {
        return (n == Math.floor(n) && !Double.isInfinite(n)) ? String.valueOf((long) n) : String.valueOf(n);
    }
}
