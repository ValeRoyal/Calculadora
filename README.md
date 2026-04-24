# Calculadora

Aplicación de calculadora en Java que ilustra la implementación simultánea de dos patrones de diseño clásicos: **Command** y **Memento**.

---

## Patrones de diseño

### Command
Cada operación aritmética (+, −, ×, ÷, C) es un objeto que implementa la interfaz `Comando`.  
La `Calculadora` actúa como **Invoker**: recibe comandos y los ejecuta sin conocer su lógica interna.

| Rol | Clase |
|-----|-------|
| Interfaz Command | `commands/Comando.java` |
| Comandos concretos | `ComandoSuma`, `ComandoResta`, `ComandoMultiplicacion`, `ComandoDivision`, `ComandoBorrar` |
| Invoker | `model/Calculadora.java` |
| Client | `ui/CalculadoraGUI.java` / `ui/CalculadoraConsola.java` |

### Memento
Antes de ejecutar cada operación, la `Calculadora` crea un **Memento** (`EstadoCalculadora`) que contiene el valor acumulado en ese instante. El `GestorHistorial` los almacena en dos pilas (undo/redo) sin acceder a su interior.

| Rol | Clase |
|-----|-------|
| Originator | `model/Calculadora.java` |
| Memento | `memento/EstadoCalculadora.java` |
| Caretaker | `memento/GestorHistorial.java` |
