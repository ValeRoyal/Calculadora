package model;

import commands.Comando;
import memento.EstadoCalculadora;
import memento.GestorHistorial;

/**
 * ═══════════════════════════════════════════════════════════════
 *  Calculadora — núcleo del sistema
 *
 *  Roles en los dos patrones:
 *
 *  COMMAND  →  Invoker
 *    Recibe objetos Comando y los ejecuta.
 *    No sabe QUÉ hace cada comando, solo lo llama.
 *
 *  MEMENTO  →  Originator
 *    ANTES de ejecutar cada comando, crea un Memento
 *    (EstadoCalculadora) con su estado actual y se lo entrega
 *    al Caretaker (GestorHistorial) para que lo guarde.
 *    Al deshacer/rehacer, pide un Memento al Caretaker y
 *    restaura su estado interno con él.
 *
 *  Integración clave:
 *    Cada operación dispara automáticamente guardar→ejecutar.
 *    El undo/redo restaura el estado exacto sin invertir matemáticas.
 * ═══════════════════════════════════════════════════════════════
 */
public class Calculadora {

    private double valorActual;
    private final GestorHistorial gestor;   // Caretaker

    public Calculadora() {
        this.valorActual = 0;
        this.gestor      = new GestorHistorial();
    }

    // ──────────────────────────────────────────────────
    //  COMMAND + MEMENTO integrados en ejecutar()
    // ──────────────────────────────────────────────────

    /**
     * Ejecuta un comando sobre el acumulador.
     * Flujo:
     *   1. El Originator crea un Memento del estado ACTUAL (antes de cambiar).
     *   2. El Caretaker guarda ese Memento en la pila undo.
     *   3. El Command ejecuta la operación y actualiza el valor.
     */
    public double ejecutar(Comando cmd) {
        // 1 + 2: Memento creado y entregado al Caretaker
        gestor.registrar(cmd.crearMemento(valorActual));
        // 3: el Command hace su trabajo
        valorActual = cmd.ejecutar(valorActual);
        return valorActual;
    }

    // ──────────────────────────────────────────────────
    //  UNDO — restaurar estado previo via Memento
    // ──────────────────────────────────────────────────

    /**
     * Deshace la última operación.
     * El Originator le pide al Caretaker el Memento anterior,
     * pasa su propio estado actual (para el redo) y restaura.
     */
    public double deshacer() {
        EstadoCalculadora estadoActual = new EstadoCalculadora(valorActual, "estado actual");
        EstadoCalculadora anterior     = gestor.popDeshacer(estadoActual);
        valorActual = anterior.getValor();
        return valorActual;
    }

    // ──────────────────────────────────────────────────
    //  REDO — volver a aplicar lo deshecho
    // ──────────────────────────────────────────────────

    public double rehacer() {
        EstadoCalculadora estadoActual = new EstadoCalculadora(valorActual, "estado actual");
        EstadoCalculadora siguiente    = gestor.popRehacer(estadoActual);
        valorActual = siguiente.getValor();
        return valorActual;
    }

    // ──────────────────────────────────────────────────
    //  Getters
    // ──────────────────────────────────────────────────

    public double getValorActual()    { return valorActual; }
    public GestorHistorial getGestor() { return gestor; }
    public boolean puedeDeshacer()    { return gestor.puedeDeshacer(); }
    public boolean puedeRehacer()     { return gestor.puedeRehacer(); }
}
