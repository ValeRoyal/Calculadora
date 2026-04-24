package commands;

/**
 * Borrado del resultado actual (C / Clear).
 * Reinicia el acumulador a 0.
 * El enunciado lo pide explícitamente como operación soportada.
 */
public class ComandoBorrar implements Comando {
    @Override public double ejecutar(double v) { return 0; }
    @Override public String getDescripcion()   { return "C (borrar)"; }
}
