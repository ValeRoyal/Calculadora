package memento;

/**
 * ─────────────────────────────────────────────────────────────
 *  PATRÓN MEMENTO — clase Memento
 *
 *  Guarda el estado completo de la Calculadora en un instante:
 *    · el valor acumulado
 *    · la descripción de la operación que lo produjo
 *
 *  Es INMUTABLE: una vez creado, no cambia.
 *  Solo el Originator (Calculadora) interactúa con su interior.
 *  El Caretaker (GestorHistorial) lo almacena sin examinarlo.
 * ─────────────────────────────────────────────────────────────
 */
public final class EstadoCalculadora {

    private final double valor;
    private final String descripcionOperacion;

    /** Solo Calculadora debe llamar este constructor */
    public EstadoCalculadora(double valor, String descripcionOperacion) {
        this.valor                = valor;
        this.descripcionOperacion = descripcionOperacion;
    }

    public double getValor()              { return valor; }
    public String getDescripcionOperacion() { return descripcionOperacion; }

    @Override
    public String toString() {
        String v = (valor == Math.floor(valor) && !Double.isInfinite(valor) && Math.abs(valor) < 1e15)
                ? String.valueOf((long) valor)
                : String.format("%.8g", valor);
        return String.format("%-14s → %s", descripcionOperacion, v);
    }
}
