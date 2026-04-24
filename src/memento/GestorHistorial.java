package memento;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────
 *  PATRÓN MEMENTO — clase Caretaker
 *
 *  Administra DOS pilas de estados (Mementos):
 *    · pilaDeshacer : estados a los que se puede volver
 *    · pilaRehacer  : estados que se deshicieron y se pueden repetir
 *
 *  El Caretaker NO conoce el contenido interno de cada Memento;
 *  solo los apila y los entrega cuando el Originator los pide.
 * ─────────────────────────────────────────────────────────────
 */
public class GestorHistorial {

    private final Deque<EstadoCalculadora> pilaDeshacer = new ArrayDeque<>();
    private final Deque<EstadoCalculadora> pilaRehacer  = new ArrayDeque<>();

    /**
     * Guarda el estado ANTES de ejecutar una operación.
     * Toda operación nueva invalida la pila de rehacer.
     */
    public void registrar(EstadoCalculadora estado) {
        pilaDeshacer.push(estado);
        pilaRehacer.clear();          // nueva acción borra el futuro
    }

    /** Devuelve el último estado guardado para deshacer */
    public EstadoCalculadora popDeshacer(EstadoCalculadora estadoActual) {
        if (pilaDeshacer.isEmpty())
            throw new IllegalStateException("No hay operaciones para deshacer");
        EstadoCalculadora anterior = pilaDeshacer.pop();
        pilaRehacer.push(estadoActual);   // guarda el actual para posible redo
        return anterior;
    }

    /** Devuelve el último estado deshecho para rehacerlo */
    public EstadoCalculadora popRehacer(EstadoCalculadora estadoActual) {
        if (pilaRehacer.isEmpty())
            throw new IllegalStateException("No hay operaciones para rehacer");
        EstadoCalculadora siguiente = pilaRehacer.pop();
        pilaDeshacer.push(estadoActual);  // guarda el actual para posible undo
        return siguiente;
    }

    public boolean puedeDeshacer() { return !pilaDeshacer.isEmpty(); }
    public boolean puedeRehacer()  { return !pilaRehacer.isEmpty(); }

    /** Vista completa del historial (pila undo, de más antiguo a más reciente) */
    public List<EstadoCalculadora> getHistorialDeshacer() {
        List<EstadoCalculadora> lista = new ArrayList<>(pilaDeshacer);
        java.util.Collections.reverse(lista);
        return lista;
    }

    public int totalPasosDeshacer() { return pilaDeshacer.size(); }
    public int totalPasosRehacer()  { return pilaRehacer.size(); }
}
