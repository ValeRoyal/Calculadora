package ui;

import commands.*;
import memento.EstadoCalculadora;
import model.Calculadora;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interfaz gráfica (Swing) — Client de ambos patrones.
 * Sustituye a CalculadoraConsola manteniendo la misma lógica
 * de Command + Memento en model.Calculadora.
 */
public class CalculadoraGUI extends JFrame {

    private final Calculadora calc = new Calculadora();

    // ── Colores ───────────────────────────────────────
    private static final Color C_FONDO      = new Color(30, 30, 35);
    private static final Color C_DISPLAY    = new Color(18, 18, 22);
    private static final Color C_BTN_NUM    = new Color(55, 58, 64);
    private static final Color C_BTN_NUM_H  = new Color(75, 78, 86);
    private static final Color C_BTN_OP     = new Color(255, 149, 0);
    private static final Color C_BTN_OP_H   = new Color(255, 175, 60);
    private static final Color C_BTN_FUNC   = new Color(80, 84, 92);
    private static final Color C_BTN_FUNC_H = new Color(105, 110, 120);
    private static final Color C_BTN_IGUAL  = new Color(48, 186, 82);
    private static final Color C_BTN_IGUAL_H= new Color(68, 210, 105);
    private static final Color C_TEXTO      = Color.WHITE;
    private static final Color C_ACENTO     = new Color(255, 215, 0);

    // ── Constantes ───────────────────────────────────
    private static final String VERSION             = "v3.0";
    /** Threshold above which doubles are shown in scientific notation instead of as long integers */
    private static final double MAX_SAFE_INT_DISPLAY = 1e15;

    // ── Fuentes ───────────────────────────────────────
    private static final Font FONT_DISPLAY = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FONT_SMALL   = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_BTN     = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_BTN_SM  = new Font("SansSerif", Font.BOLD, 13);

    // ── Estado UI ────────────────────────────────────
    private String entradaActual = "";
    private boolean limpiarEntrada = false;   // true tras ejecutar operación

    // ── Componentes ──────────────────────────────────
    private JLabel lblAcumulador;
    private JLabel lblEntrada;
    private JLabel lblHistorialInline;
    private JLabel lblUndoRedoCount;
    private JButton btnDeshacer;
    private JButton btnRehacer;

    // ── Constructor ──────────────────────────────────

    public CalculadoraGUI() {
        super("Calculadora Educativa " + VERSION);
        construirUI();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    // ── Construcción de la UI ─────────────────────────

    private void construirUI() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(C_FONDO);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        root.add(crearPanelTitulo(),   BorderLayout.NORTH);
        root.add(crearPanelDisplay(),  BorderLayout.CENTER);
        root.add(crearPanelBotones(),  BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel crearPanelTitulo() {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 2));
        p.setBackground(C_FONDO);
        p.setBorder(new EmptyBorder(0, 0, 6, 0));

        JLabel titulo = new JLabel("CALCULADORA EDUCATIVA  " + VERSION, SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        titulo.setForeground(C_ACENTO);

        JLabel subtitulo = new JLabel("Patrones: Command + Memento", SwingConstants.CENTER);
        subtitulo.setFont(FONT_SMALL);
        subtitulo.setForeground(new Color(170, 170, 190));

        p.add(titulo);
        p.add(subtitulo);
        return p;
    }

    private JPanel crearPanelDisplay() {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(C_DISPLAY);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 85), 1),
                new EmptyBorder(10, 14, 10, 14)));

        // Acumulador (valor guardado)
        JLabel lblTituloAcum = etiquetaInfo("Acumulador:");
        lblAcumulador = new JLabel(fmt(calc.getValorActual()), SwingConstants.RIGHT);
        lblAcumulador.setFont(FONT_DISPLAY);
        lblAcumulador.setForeground(C_ACENTO);

        // Entrada en curso
        JLabel lblTituloEnt = etiquetaInfo("Entrada:");
        lblEntrada = new JLabel("0", SwingConstants.RIGHT);
        lblEntrada.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblEntrada.setForeground(new Color(200, 200, 215));

        // Historial compacto (última operación)
        lblHistorialInline = new JLabel(" ", SwingConstants.LEFT);
        lblHistorialInline.setFont(FONT_SMALL);
        lblHistorialInline.setForeground(new Color(130, 200, 130));

        JPanel filasDisplay = new JPanel(new GridLayout(4, 2, 4, 2));
        filasDisplay.setBackground(C_DISPLAY);
        filasDisplay.add(lblTituloAcum);  filasDisplay.add(lblAcumulador);
        filasDisplay.add(lblTituloEnt);   filasDisplay.add(lblEntrada);
        JLabel lblTituloHist = etiquetaInfo("Última op.:");
        filasDisplay.add(lblTituloHist);  filasDisplay.add(lblHistorialInline);

        // Deshacer / Rehacer inline
        JLabel lblTituloUR = etiquetaInfo("Historial:");
        JPanel pUndoRedo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pUndoRedo.setBackground(C_DISPLAY);
        lblUndoRedoCount = new JLabel("↩ 0   ↪ 0");
        lblUndoRedoCount.setFont(FONT_SMALL);
        lblUndoRedoCount.setForeground(new Color(130, 200, 130));
        pUndoRedo.add(lblUndoRedoCount);
        filasDisplay.add(lblTituloUR);
        filasDisplay.add(pUndoRedo);

        p.add(filasDisplay, BorderLayout.CENTER);
        return p;
    }

    private JLabel etiquetaInfo(String texto) {
        JLabel l = new JLabel(texto, SwingConstants.LEFT);
        l.setFont(FONT_SMALL);
        l.setForeground(new Color(130, 130, 155));
        return l;
    }

    private JPanel crearPanelBotones() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(C_FONDO);
        p.setBorder(new EmptyBorder(6, 0, 0, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.fill    = GridBagConstraints.BOTH;
        g.insets  = new Insets(3, 3, 3, 3);
        g.weightx = 1;
        g.weighty = 1;
        g.ipadx   = 10;
        g.ipady   = 12;

        // Fila 0: C, Deshacer, Rehacer, Historial
        btnDeshacer = btnFunc("↩ Deshacer", e -> accionDeshacer());
        btnRehacer  = btnFunc("↪ Rehacer",  e -> accionRehacer());
        JButton btnC    = btnFunc("C",        e -> accionBorrar());
        JButton btnHist = btnFunc("Historial", e -> mostrarHistorial());

        btnC.setBackground(new Color(195, 60, 60));
        btnC.addMouseListener(hoverListener(btnC, new Color(220, 80, 80), new Color(195, 60, 60)));

        agregar(p, g, btnC,          0, 0, 1, 1);
        agregar(p, g, btnDeshacer,   1, 0, 1, 1);
        agregar(p, g, btnRehacer,    2, 0, 1, 1);
        agregar(p, g, btnHist,       3, 0, 1, 1);

        // Filas 1-4: teclado numérico + operaciones
        String[][] layout = {
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"0", ".", "=", "+"},
        };

        for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout[row].length; col++) {
                String lbl = layout[row][col];
                JButton btn;
                if (lbl.equals("=")) {
                    btn = crearBoton(lbl, C_BTN_IGUAL, C_BTN_IGUAL_H, FONT_BTN);
                    btn.addActionListener(e -> accionIgual());
                } else if (lbl.matches("[+\\-*/]")) {
                    btn = crearBoton(lbl, C_BTN_OP, C_BTN_OP_H, FONT_BTN);
                    final String op = lbl;
                    btn.addActionListener(e -> accionOperacion(op));
                } else {
                    btn = crearBoton(lbl, C_BTN_NUM, C_BTN_NUM_H, FONT_BTN);
                    final String digito = lbl;
                    btn.addActionListener(e -> accionDigito(digito));
                }
                agregar(p, g, btn, col, row + 1, 1, 1);
            }
        }

        actualizarEstadoBotones();
        return p;
    }

    // ── Acciones ─────────────────────────────────────

    private void accionDigito(String d) {
        if (limpiarEntrada) {
            entradaActual = "";
            limpiarEntrada = false;
        }
        // Solo un punto decimal permitido
        if (d.equals(".") && entradaActual.contains(".")) return;
        // Evitar varios ceros iniciales
        if (d.equals("0") && entradaActual.equals("0")) return;
        entradaActual += d;
        lblEntrada.setText(entradaActual.isEmpty() ? "0" : entradaActual);
    }

    private void accionOperacion(String op) {
        double n = parsearEntrada();
        if (Double.isNaN(n)) return;
        try {
            double resultado = switch (op) {
                case "+" -> calc.ejecutar(new ComandoSuma(n));
                case "-" -> calc.ejecutar(new ComandoResta(n));
                case "*" -> calc.ejecutar(new ComandoMultiplicacion(n));
                case "/" -> calc.ejecutar(new ComandoDivision(n));
                default  -> calc.getValorActual();
            };
            actualizarDisplay(resultado);
            lblHistorialInline.setText(op + " " + fmt(n) + " = " + fmt(resultado));
            limpiarEntrada = true;
        } catch (ArithmeticException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /** "=" simplemente muestra el acumulador actual sin nueva operación */
    private void accionIgual() {
        actualizarDisplay(calc.getValorActual());
        limpiarEntrada = true;
    }

    private void accionBorrar() {
        calc.ejecutar(new ComandoBorrar());
        entradaActual = "";
        limpiarEntrada = false;
        lblHistorialInline.setText("Borrado");
        actualizarDisplay(calc.getValorActual());
    }

    private void accionDeshacer() {
        try {
            double r = calc.deshacer();
            actualizarDisplay(r);
            lblHistorialInline.setText("↩ → " + fmt(r));
            limpiarEntrada = true;
        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void accionRehacer() {
        try {
            double r = calc.rehacer();
            actualizarDisplay(r);
            lblHistorialInline.setText("↪ → " + fmt(r));
            limpiarEntrada = true;
        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // ── Historial ────────────────────────────────────

    private void mostrarHistorial() {
        List<EstadoCalculadora> hist = calc.getGestor().getHistorialDeshacer();

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(C_FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Historial de estados (Mementos)", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setForeground(C_ACENTO);
        panel.add(titulo, BorderLayout.NORTH);

        String[] columnas = {"#", "Operación", "Valor antes"};
        Object[][] filas;

        if (hist.isEmpty()) {
            filas = new Object[][]{{"—", "(ninguna operación realizada aún)", "—"}};
        } else {
            filas = new Object[hist.size() + 1][3];
            filas[0] = new Object[]{"0", "inicio →", fmt(0)};
            for (int i = 0; i < hist.size(); i++) {
                EstadoCalculadora e = hist.get(i);
                filas[i + 1] = new Object[]{
                        String.valueOf(i + 1),
                        e.getDescripcionOperacion(),
                        fmt(e.getValor())
                };
            }
        }

        JTable tabla = new JTable(filas, columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla.setBackground(C_DISPLAY);
        tabla.setForeground(C_TEXTO);
        tabla.setGridColor(new Color(60, 60, 75));
        tabla.getTableHeader().setBackground(C_BTN_FUNC);
        tabla.getTableHeader().setForeground(C_TEXTO);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(22);
        tabla.setSelectionBackground(new Color(255, 149, 0, 120));
        tabla.setFocusable(false);

        // Columnas de ancho fijo
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(360, Math.min(hist.size() * 22 + 80, 260)));
        scroll.getViewport().setBackground(C_DISPLAY);
        panel.add(scroll, BorderLayout.CENTER);

        JLabel estadoActual = new JLabel(
                "Estado actual: " + fmt(calc.getValorActual()), SwingConstants.RIGHT);
        estadoActual.setFont(new Font("SansSerif", Font.BOLD, 13));
        estadoActual.setForeground(new Color(130, 200, 130));
        panel.add(estadoActual, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel,
                "Historial de operaciones", JOptionPane.PLAIN_MESSAGE);
    }

    // ── Helpers de UI ────────────────────────────────

    private void actualizarDisplay(double valor) {
        lblAcumulador.setText(fmt(valor));
        lblEntrada.setText(fmt(valor));
        lblUndoRedoCount.setText(
                "↩ " + calc.getGestor().totalPasosDeshacer() +
                "   ↪ " + calc.getGestor().totalPasosRehacer());
        actualizarEstadoBotones();
    }

    private void actualizarEstadoBotones() {
        btnDeshacer.setEnabled(calc.puedeDeshacer());
        btnRehacer.setEnabled(calc.puedeRehacer());
    }

    private double parsearEntrada() {
        String s = entradaActual.trim();
        if (s.isEmpty() || s.equals("-")) {
            mostrarError("Ingresa un número primero.");
            return Double.NaN;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            mostrarError("Número inválido: " + s);
            return Double.NaN;
        }
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ── Fábrica de botones ───────────────────────────

    private JButton btnFunc(String texto, ActionListener al) {
        JButton b = crearBoton(texto, C_BTN_FUNC, C_BTN_FUNC_H, FONT_BTN_SM);
        b.addActionListener(al);
        return b;
    }

    private JButton crearBoton(String texto, Color fondo, Color hover, Font fuente) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(fuente);
        b.setForeground(C_TEXTO);
        b.setBackground(fondo);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(hoverListener(b, hover, fondo));
        return b;
    }

    private java.awt.event.MouseAdapter hoverListener(JButton b, Color hover, Color normal) {
        return new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (b.isEnabled()) b.setBackground(hover);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(normal);
            }
        };
    }

    private void agregar(JPanel p, GridBagConstraints g, JButton b,
                         int col, int row, int w, int h) {
        g.gridx = col; g.gridy = row; g.gridwidth = w; g.gridheight = h;
        p.add(b, g);
    }

    // ── Formato de números ───────────────────────────

    private static String fmt(double n) {
        if (n == Math.floor(n) && !Double.isInfinite(n) && Math.abs(n) < MAX_SAFE_INT_DISPLAY)
            return String.valueOf((long) n);
        return String.format("%.8g", n);
    }
}
