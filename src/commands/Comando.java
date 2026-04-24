package commands;

import memento.EstadoCalculadora;

/**
 * ─────────────────────────────────────────────────────────────
 *  PATRÓN COMMAND — interfaz Command
 *
 *  Cada operación aritmética implementa esta interfaz.
 *  La integración con Memento está en ejecutar():
 *    el método recibe el estado actual (para guardarlo antes),
 *    realiza la operación y devuelve el nuevo valor.
 *
 *  El undo/redo lo gestiona el Caretaker con los Mementos,
 *  NO mediante inversión matemática en el Command.
 * ─────────────────────────────────────────────────────────────
 */
public interface Comando {

    /**
     * Ejecuta la operación sobre el valor actual.
     * @param valorActual valor antes de la operación
     * @return nuevo valor resultante
     */
    double ejecutar(double valorActual);

    /** Nombre legible de la operación, p. ej. "+ 5" */
    String getDescripcion();

    /**
     * Crea el Memento que representa el estado ANTES de ejecutar.
     * Cada Command sabe qué descripción tiene, pero el estado
     * (el valor) lo proporciona la Calculadora.
     */
    default EstadoCalculadora crearMemento(double valorAntes) {
        return new EstadoCalculadora(valorAntes, getDescripcion());
    }
}
