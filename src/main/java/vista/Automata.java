package vista;

import com.mycompany.proyectoautomatas.DFA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class Automata extends JPanel {
    private JButton btnSeleccionar;
    private JButton btnEstado;
    private JButton btnTransicion;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnCambiarPanel;
    private JButton btnComparar;

    private JPanel panel1;
    private JPanel panel2;
    private JPanel currentPanel; // Panel activo
    private JLabel lblCurrentPanel; // Etiqueta para indicar el panel actual

    // Listas separadas para cada panel
    private ArrayList<State> statesPanel1;
    private ArrayList<Transition> transitionsPanel1;
    private ArrayList<State> statesPanel2;
    private ArrayList<Transition> transitionsPanel2;

    private ArrayList<State> currentStates; // Lista de estados del panel actual
    private ArrayList<Transition> currentTransitions; // Lista de transiciones del panel actual

    private String currentAction = ""; // Acción actual seleccionada
    private State selectedState = null; // Estado seleccionado para mover o conectar
    private State draggedState = null; // Estado que se está arrastrando
    private int offsetX, offsetY; // Desplazamiento del mouse al arrastrar
    private long lastRightClickTime = 0;
    private final int doubleClickThreshold = 300; // Umbral en milisegundos

    public Automata() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        // Inicializar listas para cada panel
        statesPanel1 = new ArrayList<>();
        transitionsPanel1 = new ArrayList<>();
        statesPanel2 = new ArrayList<>();
        transitionsPanel2 = new ArrayList<>();

        // Por defecto, trabajar con las listas del panel1
        currentStates = statesPanel1;
        currentTransitions = transitionsPanel1;

        // Panel superior con botones
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        btnSeleccionar = new JButton("Seleccionar");
        btnEstado = new JButton("Estado");
        btnTransicion = new JButton("Transición");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnCambiarPanel = new JButton("Cambiar Panel");
        lblCurrentPanel = new JLabel("Panel Actual: panel1");
        btnComparar = new JButton("Comparar Autómatas");

        topPanel.add(btnSeleccionar);
        topPanel.add(btnEstado);
        topPanel.add(btnTransicion);
        topPanel.add(btnEliminar);
        topPanel.add(btnLimpiar);
        topPanel.add(btnCambiarPanel);
        topPanel.add(lblCurrentPanel);
        topPanel.add(btnComparar);

        this.add(topPanel, BorderLayout.NORTH);

        // Crear panel1
        panel1 = createDrawingPanel();
        panel1.setBackground(new Color(155, 155, 155));

        // Crear panel2
        panel2 = createDrawingPanel();
        panel2.setBackground(new Color(200, 200, 200));

        // Por defecto, mostrar panel1
        currentPanel = panel1;
        this.add(currentPanel, BorderLayout.CENTER);

        // Listeners para los botones
        btnEstado.addActionListener(e -> currentAction = "Estado");
        btnSeleccionar.addActionListener(e -> currentAction = "Seleccionar");
        btnTransicion.addActionListener(e -> currentAction = "Transición");
        btnLimpiar.addActionListener(e -> clearAll());
        btnCambiarPanel.addActionListener(e -> switchPanel());
        btnComparar.addActionListener(e -> {
            compararAutomatas();
        });
    }

    private JPanel createDrawingPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawStatesAndTransitions(g);
            }
        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedState = null; // Terminar arrastre
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
        return panel;
    }

    private void handleMousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRightClickTime < doubleClickThreshold) {
                toggleStateRoles(e.getX(), e.getY());
                lastRightClickTime = 0; // Resetear tiempo
            } else {
                lastRightClickTime = currentTime;
            }
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            switch (currentAction) {
                case "Estado":
                    createState(e.getX(), e.getY());
                    break;
                case "Seleccionar":
                    selectState(e.getX(), e.getY());
                    break;
                case "Transición":
                    createTransition(e.getX(), e.getY());
                    break;
            }
        }
        currentPanel.repaint();
    }

    private void toggleStateRoles(int x, int y) {
        for (State state : currentStates) {
            if (state.contains(x, y)) {
                state.toggleRole();
                break;
            }
        }
    }


    private void handleMouseDragged(MouseEvent e) {
        if (draggedState != null) {
            draggedState.x = e.getX() - offsetX;
            draggedState.y = e.getY() - offsetY;
            currentPanel.repaint();
        }
    }

    private void createState(int x, int y) {
        String stateLabel = "q" + currentStates.size();
        currentStates.add(new State(x, y, stateLabel));
    }

    private void selectState(int x, int y) {
        draggedState = null;
        for (State state : currentStates) {
            if (state.contains(x, y)) {
                draggedState = state; // Seleccionado para mover
                offsetX = x - state.x;
                offsetY = y - state.y;
                break;
            }
        }
    }

    private void createTransition(int x, int y) {
        for (State state : currentStates) {
            if (state.contains(x, y)) {
                if (selectedState == null) {
                    selectedState = state; // Primer clic: seleccionar origen
                } else {
                    String value = JOptionPane.showInputDialog("Ingrese el valor de la transición:");
                    if (value != null && !value.isEmpty()) {
                        currentTransitions.add(new Transition(selectedState, state, value)); // Crear transición
                    }
                    selectedState = null; // Reiniciar selección
                }
                break;
            }
        }
    }

    private void drawStatesAndTransitions(Graphics g) {
        for (Transition transition : currentTransitions) {
            transition.draw(g);
        }

        for (State state : currentStates) {
            state.draw(g);
        }
    }

    private void clearAll() {
        currentStates.clear();
        currentTransitions.clear();
        selectedState = null;
        draggedState = null;
        currentPanel.repaint();
    }

    private void switchPanel() {
        // Guardar la referencia de las listas actuales al cambiar de panel
        this.remove(currentPanel);
        if (currentPanel == panel1) {
            currentPanel = panel2;
            currentStates = statesPanel2;
            currentTransitions = transitionsPanel2;
            lblCurrentPanel.setText("Panel Actual: panel2");
        } else {
            currentPanel = panel1;
            currentStates = statesPanel1;
            currentTransitions = transitionsPanel1;
            lblCurrentPanel.setText("Panel Actual: panel1");
        }
        this.add(currentPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    private static class State {
        private int x, y;
        private final int radius = 30;
        private final String label;
        private boolean isInitial = false;
        private boolean isFinal = false;

        public State(int x, int y, String label) {
            this.x = x;
            this.y = y;
            this.label = label;
        }

        public boolean contains(int px, int py) {
            int dx = px - x;
            int dy = py - y;
            return dx * dx + dy * dy <= radius * radius;
        }

        public void toggleRole() {
            if (!isInitial && !isFinal) {
                isInitial = true; // Primera vez, lo hacemos inicial
            } else if (isInitial && !isFinal) {
                isFinal = true; // Segunda vez, lo hacemos inicial y final
            } else if (isInitial && isFinal) {
                isInitial = false; // Tercera vez, eliminamos ambos roles
                isFinal = false;
            }
        }

        public void draw(Graphics g) {
            if (isInitial) {
                g.setColor(Color.GREEN);
                g.drawOval(x - radius - 5, y - radius - 5, (radius + 5) * 2, (radius + 5) * 2);
            }

            g.setColor(Color.WHITE);
            g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
            g.setColor(Color.BLACK);
            g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

            if (isFinal) {
                g.setColor(Color.RED);
                g.drawOval(x - radius + 5, y - radius + 5, (radius - 5) * 2, (radius - 5) * 2);
            }

            g.setColor(Color.BLACK);
            g.drawString(label, x - 10, y + 5);
        }
    }

    private void compararAutomatas() {
        DFA dfa1 = construirDFA(statesPanel1, transitionsPanel1);
        DFA dfa2 = construirDFA(statesPanel2, transitionsPanel2);

        if (dfa1.isEquivalent(dfa2)) {
            JOptionPane.showMessageDialog(this, "Los autómatas son equivalentes.");
        } else {
            JOptionPane.showMessageDialog(this, "Los autómatas no son equivalentes.");
        }
    }

    private DFA construirDFA(ArrayList<State> states, ArrayList<Transition> transitions) {
        Set<String> stateNames = new HashSet<>();
        Set<String> alphabet = new HashSet<>();
        Map<String, Map<String, String>> transitionMap = new HashMap<>();
        String startState = null; // Define el estado inicial según tu lógica
        Set<String> acceptingStates = new HashSet<>();

        for (State state : states) {
            stateNames.add(state.label);
            // Asume que los estados finales tienen alguna marca especial
            if (state.label.startsWith("F")) acceptingStates.add(state.label);
        }

        for (Transition transition : transitions) {
            alphabet.add(transition.label);
            transitionMap
                    .computeIfAbsent(transition.from.label, k -> new HashMap<>())
                    .put(transition.label, transition.to.label);
        }

        return new DFA(stateNames, alphabet, transitionMap, startState, acceptingStates);
    }

    private static class Transition {
        private final State from;
        private final State to;
        private final String label;

        public Transition(State from, State to, String label) {
            this.from = from;
            this.to = to;
            this.label = label;
        }

        public void draw(Graphics g) {
            if (from == to) {
                drawLoop(g);
            } else {
                int x1 = from.x, y1 = from.y;
                int x2 = to.x, y2 = to.y;

                double angle = Math.atan2(y2 - y1, x2 - x1);
                int fromX = (int) (x1 + from.radius * Math.cos(angle));
                int fromY = (int) (y1 + from.radius * Math.sin(angle));
                int toX = (int) (x2 - to.radius * Math.cos(angle));
                int toY = (int) (y2 - to.radius * Math.sin(angle));

                g.setColor(Color.BLACK);
                g.drawLine(fromX, fromY, toX, toY);

                drawArrow(g, toX, toY, angle);

                g.drawString(label, (fromX + toX) / 2, (fromY + toY) / 2 - 10);
            }
        }

        private void drawLoop(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            int arcWidth = 30;   // Ancho del arco
            int arcHeight = 55;  // Altura del arco
            int centerX = from.x - arcWidth / 2; // Centro X del arco
            int centerY = from.y - arcHeight;   // Centro Y del arco
            int startAngle = 0;  // Ángulo inicial del arco
            int arcAngle = 270;  // Ángulo del arco (lazo)

            // Dibujar el arco (lazo)
            g2d.setColor(Color.BLACK);
            g2d.drawArc(centerX, centerY, arcWidth, arcHeight, startAngle, arcAngle);

            // Coordenadas del extremo derecho del arco
            double endAngle = Math.toRadians(startAngle + 80); // Convertir a radianes (inicio del arco)
            int arrowTipX = (int) (centerX + arcWidth);   // Extremo derecho del arco
            int arrowTipY = centerY + arcHeight / 2;      // Coordenada y en el centro vertical del arco

            // Calcular los puntos de las líneas de la flecha
            double arrowAngle = Math.toRadians(30); // Ángulo entre las líneas de la flecha
            int arrowLength = 10; // Longitud de las líneas de la flecha

            // Línea izquierda de la flecha
            int arrowX1 = (int) (arrowTipX - arrowLength * Math.cos(endAngle + arrowAngle));
            int arrowY1 = (int) (arrowTipY - arrowLength * Math.sin(endAngle + arrowAngle));

            // Línea derecha de la flecha
            int arrowX2 = (int) (arrowTipX - arrowLength * Math.cos(endAngle - arrowAngle));
            int arrowY2 = (int) (arrowTipY - arrowLength * Math.sin(endAngle - arrowAngle));

            // Dibujar la flecha
            g2d.drawLine(arrowTipX, arrowTipY, arrowX1, arrowY1); // Línea izquierda
            g2d.drawLine(arrowTipX, arrowTipY, arrowX2, arrowY2); // Línea derecha

            // Etiqueta para el lazo
            g2d.drawString(label, from.x - 5, centerY - 5); // Ajustar según la posición del lazo
        }

        private void drawArrow(Graphics g, int x, int y, double angle) {
            int arrowSize = 10;
            int x1 = (int) (x - arrowSize * Math.cos(angle - Math.PI / 6));
            int y1 = (int) (y - arrowSize * Math.sin(angle - Math.PI / 6));
            int x2 = (int) (x - arrowSize * Math.cos(angle + Math.PI / 6));
            int y2 = (int) (y - arrowSize * Math.sin(angle + Math.PI / 6));

            g.drawLine(x, y, x1, y1);
            g.drawLine(x, y, x2, y2);
        }
    }
}
