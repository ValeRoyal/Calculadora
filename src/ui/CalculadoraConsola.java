package ui;

import commands.*;
import memento.EstadoCalculadora;
import model.Calculadora;

import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de consola — Client de ambos patrones.
 * Crea los Comandos concretos y se los pasa a la Calculadora.
 */
public class CalculadoraConsola {

    private final Calculadora calc = new Calculadora();
    private final Scanner sc = new Scanner(System.in);

    private static final String RESET   = "\u001B[0m";
    private static final String CYAN    = "\u001B[36m";
    private static final String GREEN   = "\u001B[32m";
    private static final String YELLOW  = "\u001B[33m";
    private static final String RED     = "\u001B[31m";
    private static final String BOLD    = "\u001B[1m";
    private static final String DIM     = "\u001B[2m";
    private static final String MAGENTA = "\u001B[35m";

    public void iniciar() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        banner();

        boolean corriendo = true;
        while (corriendo) {
            mostrarEstado();
            mostrarMenu();
            String op = sc.nextLine().trim().toLowerCase();

            switch (op) {
                case "1", "+"     -> operacion("+");
                case "2", "-"     -> operacion("-");
                case "3", "*","x" -> operacion("*");
                case "4", "/"     -> operacion("/");
                case "5", "c"     -> accionBorrar();
                case "6", "u"     -> accionDeshacer();
                case "7", "r"     -> accionRehacer();
                case "8", "h"     -> mostrarHistorial();
                case "0", "q"     -> { System.out.println(CYAN + "\n  ¡Hasta luego!\n" + RESET); corriendo = false; }
                default           -> System.out.println(RED + "  Opción no reconocida." + RESET);
            }
        }
        sc.close();
    }

    // ── Operaciones ──────────────────────────────────

    private void operacion(String tipo) {
        System.out.print(YELLOW + "  Número: " + RESET);
        try {
            double n = Double.parseDouble(sc.nextLine().trim());
            double resultado = switch (tipo) {
                case "+" -> calc.ejecutar(new ComandoSuma(n));
                case "-" -> calc.ejecutar(new ComandoResta(n));
                case "*" -> calc.ejecutar(new ComandoMultiplicacion(n));
                case "/" -> calc.ejecutar(new ComandoDivision(n));
                default  -> calc.getValorActual();
            };
            System.out.printf("%s  = %s%s%s%n", GREEN, BOLD, fmt(resultado), RESET);
        } catch (NumberFormatException e) {
            System.out.println(RED + "  Número inválido." + RESET);
        } catch (ArithmeticException e) {
            System.out.println(RED + "  Error: " + e.getMessage() + RESET);
        }
    }

    private void accionBorrar() {
        calc.ejecutar(new ComandoBorrar());
        System.out.println(YELLOW + "  Borrado. Acumulador = 0." + RESET);
    }

    private void accionDeshacer() {
        try {
            double r = calc.deshacer();
            System.out.printf("%s  ↩ Deshecho → %s%s%n", YELLOW, fmt(r), RESET);
        } catch (IllegalStateException e) {
            System.out.println(RED + "  " + e.getMessage() + RESET);
        }
    }

    private void accionRehacer() {
        try {
            double r = calc.rehacer();
            System.out.printf("%s  ↪ Rehecho → %s%s%n", CYAN, fmt(r), RESET);
        } catch (IllegalStateException e) {
            System.out.println(RED + "  " + e.getMessage() + RESET);
        }
    }

    // ── Historial ────────────────────────────────────

    private void mostrarHistorial() {
        List<EstadoCalculadora> hist = calc.getGestor().getHistorialDeshacer();
        System.out.println(BOLD + CYAN + "\n  ╔══ HISTORIAL DE ESTADOS (Mementos) ════════╗" + RESET);
        if (hist.isEmpty()) {
            System.out.println(DIM + "  │  (ninguna operación realizada aún)        │" + RESET);
        } else {
            System.out.printf("  │ %s#%-3s%s %-16s  %s%n",
                    YELLOW, "0", RESET, "inicio →", fmt(0));
            for (int i = 0; i < hist.size(); i++) {
                EstadoCalculadora e = hist.get(i);
                System.out.printf("  │ %s#%-3d%s %s%n",
                        YELLOW, i + 1, RESET, e.toString());
            }
            System.out.printf("  │       %sEstado actual: %s%s%n",
                    MAGENTA, fmt(calc.getValorActual()), RESET);
        }
        System.out.println(BOLD + CYAN + "  ╚════════════════════════════════════════════╝" + RESET);
        System.out.print("  ENTER para continuar...");
        sc.nextLine();
    }

    // ── UI ───────────────────────────────────────────

    private void mostrarEstado() {
        System.out.println();
        System.out.printf("  %s┌──────────────────────────────────────────┐%s%n", DIM, RESET);
        System.out.printf("  %s│%s Acumulador: %s%-29s%s%s│%s%n",
                DIM, RESET, BOLD + GREEN, fmt(calc.getValorActual()), RESET, DIM, RESET);
        String ud = calc.puedeDeshacer() ? GREEN + "Sí (" + calc.getGestor().totalPasosDeshacer() + ")" + RESET : RED + "No" + RESET;
        String rd = calc.puedeRehacer()  ? GREEN + "Sí (" + calc.getGestor().totalPasosRehacer() + ")" + RESET  : RED + "No" + RESET;
        System.out.printf("  %s│%s Deshacer: %-16s Rehacer: %-10s%s│%s%n",
                DIM, RESET, ud, rd, DIM, RESET);
        System.out.printf("  %s└──────────────────────────────────────────┘%s%n", DIM, RESET);
    }

    private void mostrarMenu() {
        System.out.println(BOLD + "\n  ── Operaciones ──────────────────────────────" + RESET);
        System.out.println("  [1] Sumar      [2] Restar");
        System.out.println("  [3] Multiplicar  [4] Dividir  [5] Borrar (C)");
        System.out.println(BOLD + "\n  ── Historial ────────────────────────────────" + RESET);
        System.out.println("  [6] Deshacer (↩)   [7] Rehacer (↪)   [8] Historial");
        System.out.println(BOLD + "\n  ── Sistema ──────────────────────────────────" + RESET);
        System.out.println("  [0] Salir");
        System.out.print(CYAN + "\n  Opción: " + RESET);
    }

    private void banner() {
        System.out.println(CYAN + BOLD);
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║   CALCULADORA EDUCATIVA  v3.0                ║");
        System.out.println("  ║   Patrones: Command + Memento                ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    private static String fmt(double n) {
        if (n == Math.floor(n) && !Double.isInfinite(n) && Math.abs(n) < 1e15)
            return String.valueOf((long) n);
        return String.format("%.8g", n);
    }
}
