package agrosense.gui;

import agrosense.model.LoteSimple;
import agrosense.monitoring.SistemaMonitoreoSimple;
import agrosense.recommendations.MotorRecomendacionesSimple;
import agrosense.recommendations.RecomendacionSimple;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interfaz grafica para el sistema AgroSense (version simplificada).
 * Proporciona una interfaz visual intuitiva para el monitoreo agricola.
 */
public class AgroSenseGUISimple extends JFrame {
    private SistemaMonitoreoSimple sistemaMonitoreo;
    private MotorRecomendacionesSimple motorRecomendaciones;
    
    // Componentes principales
    private JTabbedPane tabbedPane;
    private JPanel panelLotes;
    private JPanel panelAlertas;
    private JPanel panelRecomendaciones;
    private JPanel panelEstadisticas;
    private JPanel panelMonitoreo;
    private JTextArea textAreaLog;
    private Timer timerMonitoreo;
    
    public AgroSenseGUISimple() {
        this.sistemaMonitoreo = new SistemaMonitoreoSimple();
        this.motorRecomendaciones = new MotorRecomendacionesSimple();
        
        inicializarComponentes();
        configurarVentana();
        inicializarLotesEjemplo();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        // Crear pestanas
        tabbedPane = new JTabbedPane();
        
        // Panel de Lotes
        panelLotes = crearPanelLotes();
        tabbedPane.addTab("Lotes", panelLotes);
        
        // Panel de Alertas
        panelAlertas = crearPanelAlertas();
        tabbedPane.addTab("Alertas", panelAlertas);
        
        // Panel de Recomendaciones
        panelRecomendaciones = crearPanelRecomendaciones();
        tabbedPane.addTab("Recomendaciones", panelRecomendaciones);
        
        // Panel de Estadisticas
        panelEstadisticas = crearPanelEstadisticas();
        tabbedPane.addTab("Estadisticas", panelEstadisticas);
        
        // Panel de Monitoreo
        panelMonitoreo = crearPanelMonitoreo();
        tabbedPane.addTab("Monitoreo", panelMonitoreo);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Panel de log en la parte inferior
        textAreaLog = new JTextArea(8, 50);
        textAreaLog.setEditable(false);
        textAreaLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        textAreaLog.setBackground(Color.BLACK);
        textAreaLog.setForeground(Color.GREEN);
        
        JScrollPane scrollLog = new JScrollPane(textAreaLog);
        scrollLog.setBorder(new TitledBorder("Log del Sistema"));
        
        add(scrollLog, BorderLayout.SOUTH);
        
        // Configurar timer para monitoreo automatico
        timerMonitoreo = new Timer(5000, new ActionListener() { // 5 segundos
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarMonitoreo();
            }
        });
    }
    
    private JPanel crearPanelLotes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarLotes());
        
        JButton btnAgregarLote = new JButton("Agregar Lote");
        btnAgregarLote.addActionListener(e -> mostrarDialogoAgregarLote());
        
        JButton btnLecturaManual = new JButton("Lectura Manual");
        btnLecturaManual.addActionListener(e -> realizarLecturaManual());
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnAgregarLote);
        panelBotones.add(btnLecturaManual);
        
        panel.add(panelBotones, BorderLayout.NORTH);
        
        // Panel de lotes con scroll
        JPanel panelLotesLista = new JPanel();
        panelLotesLista.setLayout(new BoxLayout(panelLotesLista, BoxLayout.Y_AXIS));
        panelLotesLista.setBorder(new TitledBorder("Lotes de Cultivo"));
        
        JScrollPane scrollLotes = new JScrollPane(panelLotesLista);
        scrollLotes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollLotes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        panel.add(scrollLotes, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelAlertas() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea textAreaAlertas = new JTextArea(20, 50);
        textAreaAlertas.setEditable(false);
        textAreaAlertas.setFont(new Font("Consolas", Font.PLAIN, 12));
        textAreaAlertas.setBorder(new TitledBorder("Alertas Activas"));
        
        JScrollPane scrollAlertas = new JScrollPane(textAreaAlertas);
        
        JButton btnActualizarAlertas = new JButton("Actualizar Alertas");
        btnActualizarAlertas.addActionListener(e -> {
            textAreaAlertas.setText("");
            mostrarAlertas(textAreaAlertas);
        });
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnActualizarAlertas);
        
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollAlertas, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelRecomendaciones() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea textAreaRecomendaciones = new JTextArea(20, 50);
        textAreaRecomendaciones.setEditable(false);
        textAreaRecomendaciones.setFont(new Font("Consolas", Font.PLAIN, 12));
        textAreaRecomendaciones.setBorder(new TitledBorder("Recomendaciones"));
        
        JScrollPane scrollRecomendaciones = new JScrollPane(textAreaRecomendaciones);
        
        JButton btnActualizarRecomendaciones = new JButton("Actualizar Recomendaciones");
        btnActualizarRecomendaciones.addActionListener(e -> {
            textAreaRecomendaciones.setText("");
            mostrarRecomendaciones(textAreaRecomendaciones);
        });
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnActualizarRecomendaciones);
        
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollRecomendaciones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea textAreaEstadisticas = new JTextArea(20, 50);
        textAreaEstadisticas.setEditable(false);
        textAreaEstadisticas.setFont(new Font("Consolas", Font.PLAIN, 12));
        textAreaEstadisticas.setBorder(new TitledBorder("Estadisticas del Sistema"));
        
        JScrollPane scrollEstadisticas = new JScrollPane(textAreaEstadisticas);
        
        JButton btnActualizarEstadisticas = new JButton("Actualizar Estadisticas");
        btnActualizarEstadisticas.addActionListener(e -> {
            textAreaEstadisticas.setText(sistemaMonitoreo.obtenerEstadisticas());
            mostrarEstadisticasDetalladas(textAreaEstadisticas);
        });
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnActualizarEstadisticas);
        
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollEstadisticas, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelMonitoreo() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel panelControles = new JPanel(new FlowLayout());
        
        JButton btnIniciarMonitoreo = new JButton("Iniciar Monitoreo Automatico");
        btnIniciarMonitoreo.addActionListener(e -> iniciarMonitoreoAutomatico());
        
        JButton btnDetenerMonitoreo = new JButton("Detener Monitoreo");
        btnDetenerMonitoreo.addActionListener(e -> detenerMonitoreoAutomatico());
        
        JButton btnLecturaCompleta = new JButton("Lectura Completa");
        btnLecturaCompleta.addActionListener(e -> realizarLecturaCompleta());
        
        JLabel lblEstado = new JLabel("Estado: INACTIVO");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 14));
        lblEstado.setForeground(Color.RED);
        
        panelControles.add(btnIniciarMonitoreo);
        panelControles.add(btnDetenerMonitoreo);
        panelControles.add(btnLecturaCompleta);
        panelControles.add(lblEstado);
        
        JTextArea textAreaMonitoreo = new JTextArea(15, 50);
        textAreaMonitoreo.setEditable(false);
        textAreaMonitoreo.setFont(new Font("Consolas", Font.PLAIN, 12));
        textAreaMonitoreo.setBorder(new TitledBorder("Monitoreo en Tiempo Real"));
        
        JScrollPane scrollMonitoreo = new JScrollPane(textAreaMonitoreo);
        
        panel.add(panelControles, BorderLayout.NORTH);
        panel.add(scrollMonitoreo, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void configurarVentana() {
        setTitle("AgroSense - Sistema de Monitoreo Agricola Inteligente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }
    
    private void inicializarLotesEjemplo() {
        log("Inicializando lotes de ejemplo...");
        
        LoteSimple lote1 = new LoteSimple("LOTE001", "Lote Norte - Tomates", 100.0, "Tomate");
        LoteSimple lote2 = new LoteSimple("LOTE002", "Lote Sur - Lechugas", 75.0, "Lechuga");
        LoteSimple lote3 = new LoteSimple("LOTE003", "Lote Este - Papas", 150.0, "Papa");
        LoteSimple lote4 = new LoteSimple("LOTE004", "Lote Oeste - Maiz", 200.0, "Maiz");
        
        sistemaMonitoreo.agregarLote(lote1);
        sistemaMonitoreo.agregarLote(lote2);
        sistemaMonitoreo.agregarLote(lote3);
        sistemaMonitoreo.agregarLote(lote4);
        
        log("Se han inicializado " + sistemaMonitoreo.getCantidadLotes() + " lotes de ejemplo.");
        actualizarLotes();
    }
    
    private void actualizarLotes() {
        JScrollPane scrollPane = (JScrollPane) panelLotes.getComponent(1);
        JPanel panelLotesLista = (JPanel) scrollPane.getViewport().getView();
        panelLotesLista.removeAll();
        
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        
        for (LoteSimple lote : lotes) {
            JPanel panelLote = crearPanelLote(lote);
            panelLotesLista.add(panelLote);
            panelLotesLista.add(Box.createVerticalStrut(10));
        }
        
        panelLotesLista.revalidate();
        panelLotesLista.repaint();
        
        log("Lotes actualizados: " + lotes.size() + " lotes");
    }
    
    private JPanel crearPanelLote(LoteSimple lote) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            lote.getNombre()
        ));
        
        // Informacion basica
        JPanel panelInfo = new JPanel(new GridLayout(4, 2));
        panelInfo.add(new JLabel("ID:"));
        panelInfo.add(new JLabel(lote.getId()));
        panelInfo.add(new JLabel("Area:"));
        panelInfo.add(new JLabel(String.format("%.1f m2", lote.getArea())));
        panelInfo.add(new JLabel("Cultivo:"));
        panelInfo.add(new JLabel(lote.getTipoCultivo()));
        panelInfo.add(new JLabel("Estado:"));
        
        JLabel lblEstado = new JLabel(lote.getEstadoGeneral());
        lblEstado.setFont(new Font("Arial", Font.BOLD, 12));
        
        switch (lote.getEstadoGeneral()) {
            case "OPTIMO":
                lblEstado.setForeground(Color.GREEN);
                break;
            case "ATENCION":
                lblEstado.setForeground(Color.ORANGE);
                break;
            case "CRITICO":
                lblEstado.setForeground(Color.RED);
                break;
        }
        
        panelInfo.add(lblEstado);
        
        // Boton de lectura
        JButton btnLeer = new JButton("Leer Sensores");
        btnLeer.addActionListener(e -> {
            lote.realizarLectura();
            actualizarLotes();
            log("Lectura realizada para " + lote.getNombre());
        });
        
        panel.add(panelInfo, BorderLayout.CENTER);
        panel.add(btnLeer, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void mostrarAlertas(JTextArea textArea) {
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        boolean hayAlertas = false;
        
        for (LoteSimple lote : lotes) {
            List<String> alertas = lote.getAlertas();
            if (!alertas.isEmpty()) {
                hayAlertas = true;
                textArea.append("Lote: " + lote.getNombre() + " (" + lote.getId() + ")\n");
                for (String alerta : alertas) {
                    textArea.append("   ALERTA: " + alerta + "\n");
                }
                textArea.append("\n");
            }
        }
        
        if (!hayAlertas) {
            textArea.append("No hay alertas activas en el sistema.\n");
            textArea.append("Todos los lotes estan funcionando correctamente.\n");
        }
    }
    
    private void mostrarRecomendaciones(JTextArea textArea) {
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        
        for (LoteSimple lote : lotes) {
            textArea.append("Lote: " + lote.getNombre() + " (" + lote.getId() + ")\n");
            List<RecomendacionSimple> recomendaciones = motorRecomendaciones.generarRecomendacionesLote(lote);
            
            if (recomendaciones.isEmpty()) {
                textArea.append("   No hay recomendaciones especificas en este momento.\n");
            } else {
                for (RecomendacionSimple rec : recomendaciones) {
                    textArea.append("   RECOMENDACION: " + rec.obtenerVistaResumida() + "\n");
                    textArea.append("      " + rec.getAccion() + "\n\n");
                }
            }
            textArea.append("-----------------------------------------\n\n");
        }
    }
    
    private void mostrarEstadisticasDetalladas(JTextArea textArea) {
        textArea.append("ESTADISTICAS DETALLADAS DEL SISTEMA\n");
        textArea.append("=====================================\n\n");
        
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        
        if (!lotes.isEmpty()) {
            long lotesOptimos = sistemaMonitoreo.obtenerLotesPorEstado("OPTIMO").size();
            long lotesAtencion = sistemaMonitoreo.obtenerLotesPorEstado("ATENCION").size();
            long lotesCriticos = sistemaMonitoreo.obtenerLotesPorEstado("CRITICO").size();
            
            textArea.append("DISTRIBUCION DE ESTADOS:\n");
            textArea.append("   Optimos: " + lotesOptimos + "\n");
            textArea.append("   Atencion: " + lotesAtencion + "\n");
            textArea.append("   Criticos: " + lotesCriticos + "\n\n");
            
            textArea.append("DETALLE POR LOTE:\n");
            for (LoteSimple lote : lotes) {
                textArea.append("   • " + lote.getNombre() + " - " + lote.getTipoCultivo() + " - " + lote.getEstadoGeneral() + "\n");
            }
        }
    }
    
    private void mostrarDialogoAgregarLote() {
        JDialog dialog = new JDialog(this, "Agregar Nuevo Lote", true);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtArea = new JTextField();
        JTextField txtCultivo = new JTextField();
        
        dialog.add(new JLabel("ID del Lote:"));
        dialog.add(txtId);
        dialog.add(new JLabel("Nombre:"));
        dialog.add(txtNombre);
        dialog.add(new JLabel("Area (m2):"));
        dialog.add(txtArea);
        dialog.add(new JLabel("Tipo de Cultivo:"));
        dialog.add(txtCultivo);
        
        JButton btnAgregar = new JButton("Agregar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnAgregar.addActionListener(e -> {
            try {
                String id = txtId.getText().trim();
                String nombre = txtNombre.getText().trim();
                double area = Double.parseDouble(txtArea.getText().trim());
                String cultivo = txtCultivo.getText().trim();
                
                if (!id.isEmpty() && !nombre.isEmpty() && !cultivo.isEmpty()) {
                    LoteSimple nuevoLote = new LoteSimple(id, nombre, area, cultivo);
                    sistemaMonitoreo.agregarLote(nuevoLote);
                    actualizarLotes();
                    log("Nuevo lote agregado: " + nombre);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "El area debe ser un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        dialog.add(btnAgregar);
        dialog.add(btnCancelar);
        
        dialog.setVisible(true);
    }
    
    private void realizarLecturaManual() {
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        for (LoteSimple lote : lotes) {
            lote.realizarLectura();
        }
        actualizarLotes();
        log("Lectura manual completada para todos los lotes");
    }
    
    private void realizarLecturaCompleta() {
        sistemaMonitoreo.realizarMonitoreoCompleto();
        actualizarLotes();
        log("Lectura completa del sistema realizada");
    }
    
    private void iniciarMonitoreoAutomatico() {
        timerMonitoreo.start();
        log("Monitoreo automatico iniciado (cada 5 segundos)");
    }
    
    private void detenerMonitoreoAutomatico() {
        timerMonitoreo.stop();
        log("Monitoreo automatico detenido");
    }
    
    private void actualizarMonitoreo() {
        realizarLecturaCompleta();
    }
    
    private void log(String mensaje) {
        String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
        textAreaLog.append("[" + timestamp + "] " + mensaje + "\n");
        textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AgroSenseGUISimple().setVisible(true);
        });
    }
}
